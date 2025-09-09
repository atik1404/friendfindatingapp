package com.friendfinapp.dating.notification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(

        "Authorization: key=AAAAKR_n_5E:APA91bEvK-tBXMWLYQUOAjU0bUNBGfolUCV-rwdHjOl4lw-W5MjHzJ6IfYcHUBdIPCopLSvZs4CyJbYjsuF6Fhq078jKS3_tLrSiDeCdxNDdxhfagx82CV6RG2pFXPL8tU7b-hqtADmx",
        "Content-Type:application/json"
    )
    //for initializing api service into the app
//    @POST("fcm/send")
//    fun sendNotification(@Body sender: Sender?): Call<MyResponse>?

    @POST("fcm/send")
    fun sendNotification2(@Body sender: Sender3?): Call<MyResponse>?
}