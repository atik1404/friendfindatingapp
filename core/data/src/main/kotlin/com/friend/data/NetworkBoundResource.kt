package com.friend.data

import com.friend.common.analytics.AnalyticsEvent
import com.friend.common.analytics.AnalyticsParam
import com.friend.common.analytics.AnalyticsService
import com.friend.domain.base.ApiResult
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class NetworkBoundResource @Inject constructor(
    private val analytics: AnalyticsService,
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun <ResultType> downloadData(api: suspend () -> Response<ResultType>): Flow<ApiResult<ResultType>> {
        return withContext(ioDispatcher) {
            flow {
                try {
                    emit(ApiResult.Loading(true))
                    val response: Response<ResultType> = api()
                    emit(ApiResult.Loading(false))
                    if (response.isSuccessful) {
                        response.body()?.let {
                            emit(ApiResult.Success(data = it))
                        } ?: emit(ApiResult.Error(message = "Unknown error occurred", code = 0))
                    } else {
                        // Report status code + endpoint path only. Never the
                        // request/response body (may contain PII).
                        reportApiFailure(
                            statusCode = response.code(),
                            endpoint = response.raw().request.url.encodedPath,
                        )
                        emit(
                            ApiResult.Error(
                                message = parserErrorBody(response.errorBody()),
                                code = response.code()
                            )
                        )
                    }
                } catch (e: Exception) {
                    emit(ApiResult.Loading(false))
                    reportNetworkError(e)
                    emit(ApiResult.Error(message = message(e), code = code(e)))
                }
            }
        }
    }

    private fun reportApiFailure(statusCode: Int, endpoint: String) {
        analytics.logEvent(
            AnalyticsEvent.API_FAILURE,
            mapOf(
                AnalyticsParam.STATUS_CODE to statusCode.toString(),
                AnalyticsParam.ENDPOINT to endpoint,
            ),
        )
    }

    private fun reportNetworkError(throwable: Throwable) {
        analytics.logEvent(
            AnalyticsEvent.ERROR_OCCURRED,
            mapOf(
                AnalyticsParam.ERROR_TYPE to (throwable.javaClass.simpleName ?: "Unknown"),
                AnalyticsParam.STATUS_CODE to code(throwable).toString(),
            ),
        )
    }

    private fun parserErrorBody(response: ResponseBody?): String {
        return response?.let {
            val errorMessage = JsonParser.parseString(it.string()).asJsonObject["message"].asString
            errorMessage.ifEmpty { "Whoops! Something went wrong" }
            errorMessage
        } ?: "Unknown error occur, please try again"
    }

    private fun message(throwable: Throwable?): String {
        when (throwable) {
            is SocketTimeoutException -> return "Whoops! The connection timed out. Please try again."
            is SSLHandshakeException -> return "Secure connection failed. Please try again."
            is UnknownHostException -> return "No internet connection. Please check your network and try again."
            is IOException -> return "No internet connection. Please check your network and try again."
            is HttpException -> return try {
                val errorJsonString = throwable.response()?.errorBody()?.string()
                val errorMessage =
                    JsonParser.parseString(errorJsonString).asJsonObject["message"].asString
                errorMessage.ifEmpty { "Something went wrong. Please try again." }
            } catch (e: Exception) {
                "Unknown error occur, please try again!"
            }
        }
        return "Unknown error occur, please try again!!!"
    }

    private fun code(throwable: Throwable?): Int {
        return when (throwable) {
            is HttpException -> (throwable).code()
            is IOException -> 100
            else -> 4001
        }
    }
}