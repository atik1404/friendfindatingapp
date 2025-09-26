package com.friendfinapp.dating.ui.mainrepo

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.friendfinapp.dating.helper.ProgressCustomDialog
import com.friendfinapp.dating.helper.showErrorLog
import com.friendfinapp.dating.ui.chatroom.ChatRoomActivity
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatMessageDeleteResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatPostingModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatSearchModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatTokenResponseModel
import com.friendfinapp.dating.ui.chatsearch.model.SearchResultResponseModel
import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel
import com.friendfinapp.dating.ui.fetchprofile.FetchProfileResponseModel
import com.friendfinapp.dating.ui.forgetpassword.ForgetPasswordActivity
import com.friendfinapp.dating.ui.forgetpassword.model.ForgetPasswordResponseModel
import com.friendfinapp.dating.ui.individualsearch.IndividualSearchInterface
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchNewResponseModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchPostingModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchResponseModel
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultPostingModel
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.model.LogOutResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.SearchInterface
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchPostingModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.UserOnlineUpdateModel
import com.friendfinapp.dating.ui.landingpage.model.NotificationPostingModel
import com.friendfinapp.dating.ui.landingpage.model.NotificationResponseModel
import com.friendfinapp.dating.ui.network.RetrofitClient
import com.friendfinapp.dating.ui.othersprofile.model.BlockedUnblockedPostingModel
import com.friendfinapp.dating.ui.othersprofile.model.ForwardMessagePostingModel
import com.friendfinapp.dating.ui.othersprofile.model.SendMessagePostingModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.BlockUnblockResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.MessageResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.OthersProfileResponseModel
import com.friendfinapp.dating.ui.reportanabuse.ReportAnAbuse
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbuseResponseModel
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbusedPostingModel
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfilePostingModel
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfileResponseModel
import com.friendfinapp.dating.ui.settingspersonal.PersonalSettingsActivity
import com.friendfinapp.dating.ui.settingspersonal.model.PasswordChangePostingModel
import com.friendfinapp.dating.ui.settingspersonal.model.PasswordChangeResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsGetDataResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsPostingModel
import com.friendfinapp.dating.ui.settingspersonal.model.PersonalSettingsResponseModel
import com.friendfinapp.dating.ui.settingspersonal.model.UpdatePersonalSettingsPostingModel
import com.friendfinapp.dating.ui.settingspersonal.model.UpdatePersonalSettingsResponseModel
import com.friendfinapp.dating.ui.signin.model.LogInPostingModel
import com.friendfinapp.dating.ui.signin.model.LoginResponseModel
import com.friendfinapp.dating.ui.signup.model.PostingModel
import com.friendfinapp.dating.ui.signup.model.RegisterResponseModel
import com.friendfinapp.dating.ui.uploadphoto.responsemodel.UploadPhotoResponseModel
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.Sink
import okio.buffer
import reduceImageSize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.util.Arrays





class MainRepo {
    fun registerUser(
        userName: String,
        password: String,
        email: String,
        fullName: String,
        gen: Int,
        active: Int,
        receiveEmails: Int,
        interest: Int,
        dateofbirth: String,
        dateofbirth2: String,
        country: String,
        state: String,
        postalcode: String,
        city: String,
        ip: String,
        messageVerificationsLeft: Int,
        languageId: Int,
        billingDetails: String?,
        invitedBy: String,
        incomingMessagesRestrictions: String?,
        affiliateID: Int,
        options: Int,
        longitude: Int,
        latitude: Int,
        tokenUniqueId: String?,
        credits: Int,
        moderationScore: Int,
        spamSuspected: Int,
        faceControlApproved: Int,
        profileSkin: String,
        statusText: String,
        featuredMember: Int,
        mySpaceID: String,
        facebookID: Int,
        eventsSettings: Int
    ): LiveData<RegisterResponseModel> {

        val registerResponse= MutableLiveData<RegisterResponseModel>()



        var postingModel:PostingModel=PostingModel(userName,password,email,fullName,gen,active,receiveEmails,interest,dateofbirth,dateofbirth2,country,state,postalcode,city,ip,messageVerificationsLeft,languageId)



        RetrofitClient.instance?.api?.registerUser(postingModel)?.enqueue(object  :Callback<RegisterResponseModel>{
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<RegisterResponseModel>,
                response: Response<RegisterResponseModel>
            ) {
                registerResponse.value=response.body()
                                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
                Log.d("TAG", "onResponse: " +registerResponse.value.toString() )
            }

            override fun onFailure(call: Call<RegisterResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return registerResponse
    }

    fun loginUser(username: String, password: String): LiveData<LoginResponseModel> {

        val loginResponse = MutableLiveData<LoginResponseModel>()


        var postingModel:LogInPostingModel= LogInPostingModel(username,password)

        RetrofitClient.instance?.api?.loginUser(postingModel)?.enqueue(object  : Callback<LoginResponseModel>{
            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {

                if (response.isSuccessful){
                    loginResponse.value=response.body()
                }else{
                    Log.d("TAG", "onFailure: "+response.errorBody().toString())
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailuress: " + t.message.toString())

            }

        })

        return loginResponse
    }


    fun loginGoogleUser(email: String): LiveData<LoginResponseModel> {
        val loginResponse = MutableLiveData<LoginResponseModel>()


       // var postingModel:LogInPostingModel= LogInPostingModel(username,password)

        RetrofitClient.instance?.api?.loginGoogleUser(email)?.enqueue(object  : Callback<LoginResponseModel>{
            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {

                if (response.isSuccessful){
                    loginResponse.value=response.body()
                }else{
                    Log.d("TAG", "onFailure: "+response.errorBody().toString())
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())

            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)


            }

        })

        return loginResponse
    }

    fun forgetPasswordChange(
        email: String,
        customDialog: ProgressCustomDialog,
        forgetPasswordActivity: ForgetPasswordActivity
    ): LiveData<ForgetPasswordResponseModel> {
        val forgetPasswordResponse = MutableLiveData<ForgetPasswordResponseModel>()

        RetrofitClient.instance?.api?.forgetPasswordChange(email)?.enqueue(object : Callback<ForgetPasswordResponseModel>{
            override fun onResponse(
                call: Call<ForgetPasswordResponseModel>,
                response: Response<ForgetPasswordResponseModel>
            ) {
                if (response.isSuccessful){
                    forgetPasswordResponse.value=response.body()
                }else{
                    customDialog.dismiss()
                    Toast.makeText(forgetPasswordActivity,""+response.message()+ ". Please Enter correct Email or check internet!",Toast.LENGTH_SHORT).show()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<ForgetPasswordResponseModel>, t: Throwable) {

                customDialog.dismiss()

                Toast.makeText(forgetPasswordActivity,""+t.message,Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: " + t.message)
            }
        })



        return forgetPasswordResponse
    }

    fun saveProfile(saveProfileList: MutableList<SaveProfilePostingModel>): LiveData<SaveProfileResponseModel> {
        val saveProfile = MutableLiveData<SaveProfileResponseModel>()



        RetrofitClient.instance?.api?.saveProfile(saveProfileList)?.enqueue( object : Callback<SaveProfileResponseModel>{
            override fun onResponse(
                call: Call<SaveProfileResponseModel>,
                response: Response<SaveProfileResponseModel>
            ) {

                if (response.isSuccessful){
                    saveProfile.value=response.body()
                }else{

                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<SaveProfileResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return saveProfile

    }

    fun fetchProfile(username: String?): LiveData<FetchProfileResponseModel> {
        val fetchProfile = MutableLiveData<FetchProfileResponseModel>()

        RetrofitClient.instance?.api?.fetchProfile(username)?.enqueue(object : Callback<FetchProfileResponseModel>{
            override fun onResponse(
                call: Call<FetchProfileResponseModel>,
                response: Response<FetchProfileResponseModel>
            ) {
               if (response.isSuccessful){
                   fetchProfile.value=response.body()
               }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<FetchProfileResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return fetchProfile
    }

    fun getLogoutUser(): LiveData<LogOutResponseModel> {
        val logout = MutableLiveData<LogOutResponseModel>()


        RetrofitClient.instance?.api?.getLogOut()?.enqueue(object : Callback<LogOutResponseModel>{
            override fun onResponse(
                call: Call<LogOutResponseModel>,
                response: Response<LogOutResponseModel>
            ) {
                if (response.isSuccessful){
                    logout.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<LogOutResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }


        })

        return logout

    }
    val SearchData = MutableLiveData<SearchResponseModel>()
    fun getTopSearch(postingModel: SearchPostingModel, searchItem: SearchInterface) {
      //  val topSearch = MutableLiveData<SearchResponseModel>()

        searchItem.onProductLoading()

        RetrofitClient.instance?.api?.getTopSearch(postingModel)?.enqueue(object : Callback<SearchResponseModel>{
            override fun onResponse(
                call: Call<SearchResponseModel>,
                response: Response<SearchResponseModel>
            ) {
                if (response.isSuccessful){
                    SearchData.value=response.body()
                    endHasBeenReached.value = !response.body()?.data?.isNotEmpty()!!
                    searchItem.onProductLoadingFinish()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<SearchResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)

                searchItem.onProductLoadError(t.message!!)
            }
        })

    }

 val searchDataWithLocationAndInterest = MutableLiveData<SearchResponseModel>()
    fun getTopSearchWithLocationAndInterest(myUserName: String) {
      //  val topSearch = MutableLiveData<SearchResponseModel>()

      //  searchItem.onProductLoading()

        RetrofitClient.instance?.api?.getTopSearchWithLocationAndInterest(myUserName)?.enqueue(object : Callback<SearchResponseModel>{
            override fun onResponse(
                call: Call<SearchResponseModel>,
                response: Response<SearchResponseModel>
            ) {
                if (response.isSuccessful){
                    searchDataWithLocationAndInterest.value=response.body()
                    endHasBeenReached.value = !response.body()?.data?.isNotEmpty()!!
                    //searchItem.onProductLoadingFinish()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<SearchResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)

              //  searchItem.onProductLoadError(t.message!!)
            }
        })

    }


    val individualSearchData = MutableLiveData<IndividualSearchResultResponseModel>()
    fun getIndividualSearchResult(
        postingModel: IndividualSearchResultPostingModel,
        searchItem: IndividualSearchInterface
    ) {
        searchItem.onProductLoading()

        RetrofitClient.instance?.api?.getIndividualSearchResult(postingModel)?.enqueue(object : Callback<IndividualSearchResultResponseModel>{
            override fun onResponse(
                call: Call<IndividualSearchResultResponseModel>,
                response: Response<IndividualSearchResultResponseModel>
            ) {
                if (response.isSuccessful){
                    individualSearchData.value=response.body()
                    endHasBeenReached.value = !response.body()?.data?.isNotEmpty()!!
                    searchItem.onProductLoadingFinish()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<IndividualSearchResultResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)

                searchItem.onProductLoadError(t.message!!)
            }
        })

    }

    fun photoUpload(
        id: String,
        username: String?,
        photoalbum: String,
        imageUri: Uri,
        file: File,
        name: String?,
        s: String,
        approved: String,
        approvedDate: String,
        primary: String,
        explicit: String,
        private: String,
        faceCrop: String,
        manualApproval: String,
        salute: String
    ): LiveData<UploadPhotoResponseModel> {
        val upLoadPhotoResponse = MutableLiveData<UploadPhotoResponseModel>()

        Log.d("TAG", "photoUpload: "+file.absolutePath)
        Log.d("TAG", "photoUpload: "+imageUri.toString())
        val requestFile =
            file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("Image", file.name, requestFile)

//        var files = File("android.resource://com.friendfin.dating/drawable/chat.png")
//        val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
//            "img",
//            file.name,
//            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//        )


        Log.d("TAG", "photoUpload:11 "+imageUri.toString())
        //USER_ID.toRequestBody(MultipartBody.FORM)
        val username = username!!.toRequestBody(MultipartBody.FORM)
        val photoAlbum = photoalbum.toRequestBody(MultipartBody.FORM)
        val name = name!!.toRequestBody(MultipartBody.FORM)
        val description = s.toRequestBody(MultipartBody.FORM)
        val approve = approved.toRequestBody(MultipartBody.FORM)
        val approveDate = approvedDate.toRequestBody(MultipartBody.FORM)
//        val Id = RequestBody.create(MultipartBody.FORM, id)
//        val primary = RequestBody.create(MultipartBody.FORM, primary)
//        val explicit = RequestBody.create(MultipartBody.FORM, explicit)
//        val private = RequestBody.create(MultipartBody.FORM, private)
//        val faceCrop = RequestBody.create(MultipartBody.FORM, faceCrop)
//        val manualApproval = RequestBody.create(MultipartBody.FORM, manualApproval)
//        val salute = RequestBody.create(MultipartBody.FORM, salute)


        RetrofitClient.instance?.api?.uploadPhoto(username,photoAlbum,name,description,approve,approveDate,body)?.enqueue(object : Callback<UploadPhotoResponseModel>{
            override fun onResponse(
                call: Call<UploadPhotoResponseModel>,
                response: Response<UploadPhotoResponseModel>
            ) {

                if (response.isSuccessful){
                    upLoadPhotoResponse.value=response.body()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<UploadPhotoResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return upLoadPhotoResponse
    }

    fun sendMessageWithAudio(
        fromUserName: String,
        toUserName: String,
        fileUri: Uri,
        file: File,
        recordingDuration: Long
    ): LiveData<MessageResponseModel> {
        val messageResponse = MutableLiveData<MessageResponseModel>()
        Log.d("TAG", "photoUpload: "+file.absolutePath)
        Log.d("TAG", "photoUpload: "+fileUri.toString())

        val requestFile =
            file.asRequestBody("audio/mp3".toMediaType())
        val body = MultipartBody.Part.createFormData("FAudio", file.name, requestFile)

        val fromUserName = fromUserName.toRequestBody(MultipartBody.FORM)
        val toUserName = toUserName.toRequestBody(MultipartBody.FORM)
        val audioDuration = recordingDuration.toString().toRequestBody(MultipartBody.FORM)


        RetrofitClient.instance?.api?.sendMessageWithAudio(fromUserName,toUserName,body,audioDuration)?.enqueue(object : Callback<MessageResponseModel>{
            override fun onResponse(
                call: Call<MessageResponseModel>,
                response: Response<MessageResponseModel>
            ) {

                if (response.isSuccessful){
                    messageResponse.value=response.body()
                }

                Log.d("TAG", "onResponsess: " + response.raw().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.errorBody()?.string())
                Log.d("TAG", "onResponsess: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return messageResponse
    }


    fun sendMessageWithFile(
        fromUserName: String,
        toUserName: String,
        textMessageBody: String,
        repliedTo: Int,
        pendingApproval: Int,
        imageUri: Uri,
        file: File,
        progressCallback: (Int) -> Unit // Progress callback function
    ): LiveData<MessageResponseModel> {
        val messageResponse = MutableLiveData<MessageResponseModel>()
        Log.d("TAG", "photoUpload: "+file.absolutePath)
        Log.d("TAG", "photoUpload: "+imageUri.toString())

        val progressListener = object : ProgressRequestBody.ProgressListener {
            override fun onProgress(progress: Int) {
                // Update your UI with the progress value
                // For example, progressBar.progress = progress
                progressCallback(progress)
            }
        }

        val requestFile = ProgressRequestBody(
            file.asRequestBody("image/*".toMediaType()),
            progressListener
        )

//        val requestFile =
//            file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("FImage", file.name, requestFile)


        Log.d("TAG", "photoUpload:11 "+imageUri.toString())
        //USER_ID.toRequestBody(MultipartBody.FORM)
        val fromUserName = fromUserName.toRequestBody(MultipartBody.FORM)
        val toUserName = toUserName.toRequestBody(MultipartBody.FORM)
        val textMessageBody = textMessageBody.toRequestBody(MultipartBody.FORM)

        RetrofitClient.instance?.api?.sendMessageWithFile(fromUserName,toUserName,textMessageBody,repliedTo,pendingApproval,body)?.enqueue(object : Callback<MessageResponseModel>{
            override fun onResponse(
                call: Call<MessageResponseModel>,
                response: Response<MessageResponseModel>
            ) {

                if (response.isSuccessful){
                    messageResponse.value=response.body()
                }

                Log.d("TAG", "onResponsess: " + response.raw().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.errorBody()?.string())
                Log.d("TAG", "onResponsess: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return messageResponse
    }
    fun sendMessageWithVideoFile(
        fromUserName: String,
        toUserName: String,
        textMessageBody: String,
        repliedTo: Int,
        pendingApproval: Int,
        video: Uri,
        file: File,
        progressCallback: (Int) -> Unit // Progress callback function
    ): LiveData<MessageResponseModel> {
        val messageResponse = MutableLiveData<MessageResponseModel>()
        Log.d("TAG", "photoUpload: "+file.absolutePath)
        Log.d("TAG", "photoUpload: "+video.toString())


        val progressListener = object : ProgressRequestBody.ProgressListener {
            override fun onProgress(progress: Int) {
                // Update your UI with the progress value
                // For example, progressBar.progress = progress
                progressCallback(progress)
            }
        }

        val requestFile = ProgressRequestBody(
            file.asRequestBody("video/*".toMediaType()),
            progressListener
        )

//        val requestFile =
//            file.asRequestBody("video/*".toMediaType())


        val body = MultipartBody.Part.createFormData("FVideo", file.name, requestFile)


        Log.d("TAG", "photoUpload:11 "+video.toString())
        //USER_ID.toRequestBody(MultipartBody.FORM)
        val fromUserName = fromUserName.toRequestBody(MultipartBody.FORM)
        val toUserName = toUserName.toRequestBody(MultipartBody.FORM)
        val textMessageBody = textMessageBody.toRequestBody(MultipartBody.FORM)
        val videoDuration = "12".toString().toRequestBody(MultipartBody.FORM)

        RetrofitClient.instance?.api?.sendMessageWithVideo(fromUserName,toUserName,body,videoDuration)?.enqueue(object : Callback<MessageResponseModel>{
            override fun onResponse(
                call: Call<MessageResponseModel>,
                response: Response<MessageResponseModel>
            ) {

                if (response.isSuccessful){
                    messageResponse.value=response.body()
                }

                Log.d("TAG", "onResponsess: " + response.raw().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.errorBody()?.string())
                Log.d("TAG", "onResponsess: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return messageResponse
    }



    fun sendMessageWithOnlyText(
        fromUserName: String,
        toUserName: String,
        textMessageBody: String,
        repliedTo: Int,
        pendingApproval: Int
    ): LiveData<MessageResponseModel> {
        val messageResponse = MutableLiveData<MessageResponseModel>()

        val fromUserName = fromUserName.toRequestBody(MultipartBody.FORM)
        val toUserName = toUserName.toRequestBody(MultipartBody.FORM)
        val textMessageBody = textMessageBody.toRequestBody(MultipartBody.FORM)

        RetrofitClient.instance?.api?.sendMessageWithOnlyText(fromUserName,toUserName,textMessageBody,repliedTo,pendingApproval)?.enqueue(object : Callback<MessageResponseModel>{
            override fun onResponse(
                call: Call<MessageResponseModel>,
                response: Response<MessageResponseModel>
            ) {

                if (response.isSuccessful){
                    messageResponse.value=response.body()
                }

                Log.d("TAG", "onResponsess: " + response.raw().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.body().toString())
                Log.d("TAG", "onResponsess: " + response.errorBody()?.string())
                Log.d("TAG", "onResponsess: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })
        return messageResponse
    }

    fun sendMessage(sendMessage: SendMessagePostingModel): LiveData<MessageResponseModel> {
        val message = MutableLiveData<MessageResponseModel>()


        RetrofitClient.instance?.api?.sendMessage(sendMessage)?.enqueue(object : Callback<MessageResponseModel>{
            override fun onResponse(
                call: Call<MessageResponseModel>,
                response: Response<MessageResponseModel>
            ) {
                if (response.isSuccessful){
                    message.value=response.body()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })


        return message


    }

    fun forwardMessageToMultipleUser(params: ForwardMessagePostingModel): LiveData<MessageResponseModel> {
        val message = MutableLiveData<MessageResponseModel>()


        RetrofitClient.instance?.api?.forwardMessageToMultipleUser(params)
            ?.enqueue(object : Callback<MessageResponseModel> {
                override fun onResponse(
                    call: Call<MessageResponseModel>,
                    response: Response<MessageResponseModel>
                ) {
                    if (response.isSuccessful) {
                        message.value = response.body()
                    }

                    Log.d("TAG", "onResponse: " + response.raw().toString())
                    Log.d("TAG", "onResponse: " + response.body().toString())
                    Log.d("TAG", "onResponse: " + response.body().toString())
                    Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                    Log.d("TAG", "onResponse: " + response.message() + response.code())
                    showErrorLog(Gson().toJson(response.body()))
                }

                override fun onFailure(call: Call<MessageResponseModel>, t: Throwable) {
                    Log.d("TAG", "onFailure: " + t.message)
                }
            })

        return message
    }

    fun getChatUser(username: String?, page: Int): LiveData<ChatResponseModel> {
        val userList = MutableLiveData<ChatResponseModel>()


        RetrofitClient.instance?.api?.getChatList(username,page)?.enqueue(object : Callback<ChatResponseModel>{
            override fun onResponse(
                call: Call<ChatResponseModel>,
                response: Response<ChatResponseModel>
            ) {
                if (response.isSuccessful){
                    userList.value=response.body()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))

            }

            override fun onFailure(call: Call<ChatResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })


        return userList
    }

    fun getChatList(chatPostingModel: LiveChatPostingModel): LiveData<LiveChatResponseModel> {
        val chatList = MutableLiveData<LiveChatResponseModel>()


        RetrofitClient.instance?.api?.getChatConversation(chatPostingModel)?.enqueue(object : Callback<LiveChatResponseModel>{
            override fun onResponse(
                call: Call<LiveChatResponseModel>,
                response: Response<LiveChatResponseModel>
            ) {
                if (response.isSuccessful){
                    chatList.value=response.body()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<LiveChatResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return chatList
    }

    fun getChatListSearchResult(chatPostingModel: LiveChatSearchModel): LiveData<LiveChatResponseModel> {
        val chatList = MutableLiveData<LiveChatResponseModel>()
        RetrofitClient.instance?.api?.getChatListSearchResult(chatPostingModel,)?.enqueue(object : Callback<LiveChatResponseModel>{
            override fun onResponse(
                call: Call<LiveChatResponseModel>,
                response: Response<LiveChatResponseModel>
            ) {
                if (response.isSuccessful){
                    chatList.value=response.body()
                }else {
                    chatList.value = null
                }
            }

            override fun onFailure(call: Call<LiveChatResponseModel>, t: Throwable) {
                chatList.value = null
            }
        })

        return chatList
    }

    fun getIndividualSearch(
        gender: String,
        seeking: String,
        minAge: String,
        maxAge: String,
        country: String,
        state: String,
        city: String,
        userName: String
    ): LiveData<IndividualSearchResponseModel> {
        val userDetails = MutableLiveData<IndividualSearchResponseModel>()

        var model: IndividualSearchPostingModel= IndividualSearchPostingModel(gender,seeking, minAge.toInt(),maxAge.toInt(),country,state,city,userName)

        RetrofitClient.instance?.api?.getIndividualUser(model)?.enqueue(object : Callback<IndividualSearchResponseModel>{
            override fun onResponse(
                call: Call<IndividualSearchResponseModel>,
                response: Response<IndividualSearchResponseModel>
            ) {
                userDetails.value=response.body()
                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<IndividualSearchResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return userDetails
    }

    fun getNewIndividualSearch(userName: String): LiveData<IndividualSearchNewResponseModel> {
        val userDetails = MutableLiveData<IndividualSearchNewResponseModel>()

        RetrofitClient.instance?.api?.getIndividualNewUser(userName)?.enqueue(object : Callback<IndividualSearchNewResponseModel>{
            override fun onResponse(
                call: Call<IndividualSearchNewResponseModel>,
                response: Response<IndividualSearchNewResponseModel>
            ) {
                userDetails.value=response.body()
                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<IndividualSearchNewResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return userDetails
    }

    fun blockedUnblockedUser(model: BlockedUnblockedPostingModel): LiveData<BlockUnblockResponseModel> {
        val blockedUnblocked = MutableLiveData<BlockUnblockResponseModel>()


        RetrofitClient.instance?.api?.getBlockedUnblocked(model)?.enqueue(object : Callback<BlockUnblockResponseModel>{
            override fun onResponse(
                call: Call<BlockUnblockResponseModel>,
                response: Response<BlockUnblockResponseModel>
            ) {
                if (response.isSuccessful){
                    blockedUnblocked.value=response.body()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<BlockUnblockResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return blockedUnblocked

    }

    fun reportAnAbuse(
        model: ReportAbusedPostingModel,
        customDialog: ProgressCustomDialog?,
        reportAnAbuse: ReportAnAbuse
    ): LiveData<ReportAbuseResponseModel> {
        val report = MutableLiveData<ReportAbuseResponseModel>()

        RetrofitClient.instance?.api?.sendReport(model)?.enqueue(object :Callback<ReportAbuseResponseModel>{
            override fun onResponse(
                call: Call<ReportAbuseResponseModel>,
                response: Response<ReportAbuseResponseModel>
            ) {
                if (response.isSuccessful){
                    report.value=response.body()
                }else{
                    customDialog?.dismiss()
                    Toast.makeText(reportAnAbuse,""+response.message(),Toast.LENGTH_SHORT).show()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<ReportAbuseResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                customDialog?.dismiss()
                Toast.makeText(reportAnAbuse,""+t.message,Toast.LENGTH_SHORT).show()
            }

        })

        return report
    }

    fun delete(
        model: PersonalSettingsPostingModel,
        customDialog: ProgressCustomDialog?,
        personalSettingsActivity: PersonalSettingsActivity
    ): LiveData<PersonalSettingsResponseModel> {

        val delete = MutableLiveData<PersonalSettingsResponseModel>()

        RetrofitClient.instance?.api?.delete(model)?.enqueue(object :Callback<PersonalSettingsResponseModel>{
            override fun onResponse(
                call: Call<PersonalSettingsResponseModel>,
                response: Response<PersonalSettingsResponseModel>
            ) {
                if (response.isSuccessful){
                    delete.value=response.body()
                }else{
                    customDialog?.dismiss()
                    Toast.makeText(personalSettingsActivity,""+response.message(),Toast.LENGTH_SHORT).show()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<PersonalSettingsResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                customDialog?.dismiss()
                Toast.makeText(personalSettingsActivity,""+t.message,Toast.LENGTH_SHORT).show()
            }

        })

        return delete

    }

    fun otherUserProfile(ownUserName: String?,OtherUserName: String?): LiveData<OthersProfileResponseModel> {
        val otherFetchProfile = MutableLiveData<OthersProfileResponseModel>()

        Log.d("TAG", "otherUserProfile: call ")

        RetrofitClient.instance?.api?.otherFetchProfile(ownUserName,OtherUserName)?.enqueue(object : Callback<OthersProfileResponseModel>{
            override fun onResponse(
                call: Call<OthersProfileResponseModel>,
                response: Response<OthersProfileResponseModel>
            ) {
                if (response.isSuccessful){
                    otherFetchProfile.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<OthersProfileResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                failureResult.value=true
            }

        })

        return otherFetchProfile
    }



    val searchResult = MutableLiveData<SearchResultResponseModel>()
    val suggetionData = MutableLiveData<SuggestionResponseModel>()

    fun getServerChatDataHintByKeyword(userName: String, newText: String) {



        RetrofitClient.instance?.api?.getServerChatDataHintByKeyword(userName,newText)?.enqueue(object : Callback<SuggestionResponseModel>{
            override fun onResponse(
                call: Call<SuggestionResponseModel>,
                response: Response<SuggestionResponseModel>
            ) {
                if (response.isSuccessful){
                    suggetionData.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<SuggestionResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })


    }

    fun getSearchResult(myUserName: String?, otherUserName: String) {
        RetrofitClient.instance?.api?.getSearchResult(myUserName,otherUserName)?.enqueue(object : Callback<SearchResultResponseModel>{
            override fun onResponse(
                call: Call<SearchResultResponseModel>,
                response: Response<SearchResultResponseModel>
            ) {
                if (response.isSuccessful){
                    searchResult.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<SearchResultResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

    }

    fun getPasswordChange(username: String?, oldPassword: String?,newPassword: String): LiveData<PasswordChangeResponseModel>{

        val getPasswordChange = MutableLiveData<PasswordChangeResponseModel>()

        var postingModel:PasswordChangePostingModel= PasswordChangePostingModel(username,oldPassword,newPassword)

        RetrofitClient.instance?.api?.getPasswordChange(postingModel)?.enqueue(object : Callback<PasswordChangeResponseModel>{
            override fun onResponse(
                call: Call<PasswordChangeResponseModel>,
                response: Response<PasswordChangeResponseModel>
            ) {
                if (response.isSuccessful){
                    getPasswordChange.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<PasswordChangeResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return getPasswordChange

    }




    fun getUserOnlineUpdate(userName: String): LiveData<UserOnlineUpdateModel> {
        val online = MutableLiveData<UserOnlineUpdateModel>()
        RetrofitClient.instance?.api?.getUserOnlineUpdate(userName)?.enqueue(object : Callback<UserOnlineUpdateModel>{
            override fun onResponse(
                call: Call<UserOnlineUpdateModel>,
                response: Response<UserOnlineUpdateModel>
            ) {
                if (response.isSuccessful){
                    online.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<UserOnlineUpdateModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })


        return online
    }

    fun notificationTokenUpdate(token: String, userName: String): LiveData<NotificationResponseModel> {
        val notification = MutableLiveData<NotificationResponseModel>()

        var model: NotificationPostingModel = NotificationPostingModel(userName,token)


        RetrofitClient.instance?.api?.getNotificationUpdate(model)?.enqueue(object : Callback<NotificationResponseModel>{
            override fun onResponse(
                call: Call<NotificationResponseModel>,
                response: Response<NotificationResponseModel>
            ) {
                if (response.isSuccessful){
                    notification.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<NotificationResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })
        return notification
    }

    fun getUserNotificationToken(username: String?): LiveData<LiveChatTokenResponseModel> {
        val token = MutableLiveData<LiveChatTokenResponseModel>()

        RetrofitClient.instance?.api?.getUserNotificationToken(username)?.enqueue(object : Callback<LiveChatTokenResponseModel>{
            override fun onResponse(
                call: Call<LiveChatTokenResponseModel>,
                response: Response<LiveChatTokenResponseModel>
            ) {
                if (response.isSuccessful){
                    token.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().message)
                Log.d("TAG", "onResponse: " + response.message())
            }

            override fun onFailure(call: Call<LiveChatTokenResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
            }

        })

        return token

    }



    val endHasBeenReachedTopic = MutableLiveData<Boolean>()
    val endHasBeenReached = MutableLiveData<Boolean>()


    val failureResult = MutableLiveData<Boolean>()

    //google sign in call
//    fun socialLoginUser(
//        name: String,
//        email: String,
//        uid: String,
//        ProfileUrl: String
//    ): LiveData<RegisterResponseModel> {
//        val googleResponse = MutableLiveData<RegisterResponseModel>()
//
//        RetrofitClient.instance?.api?.googleLogInUser(name, email, uid, ProfileUrl)
//            ?.enqueue(object : Callback<RegisterResponseModel> {
//                override fun onResponse(
//                    call: Call<RegisterResponseModel>,
//                    response: Response<RegisterResponseModel>
//                ) {
//                    Log.d("TAG", "onResponse: " + response.raw().message)
//                    if (response.isSuccessful)
//                        googleResponse.value = response.body()
//                }
//
//                override fun onFailure(call: Call<RegisterResponseModel>, t: Throwable) {
//
//                    Log.d("TAG", "onFailure: " + t.message)
//                }
//            })
//
//        return googleResponse
//
//    }


    class ProgressRequestBody(
        private val requestBody: RequestBody,
        private val progressListener: ProgressListener
    ) : RequestBody() {

        override fun contentType(): MediaType? {
            return requestBody.contentType()
        }

        override fun contentLength(): Long {
            return requestBody.contentLength()
        }

        override fun writeTo(sink: BufferedSink) {
            val countingSink = CountingSink(sink)
            val bufferedSink = countingSink.buffer()
            requestBody.writeTo(bufferedSink)
            bufferedSink.flush()
        }

        private inner class CountingSink(delegate: Sink) : ForwardingSink(delegate) {
            private var bytesWritten = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                bytesWritten += byteCount
                val progress = (bytesWritten * 100 / contentLength()).toInt()
                progressListener.onProgress(progress)
            }
        }

        interface ProgressListener {
            fun onProgress(progress: Int)
        }
    }



    fun personalSettingsGetData(ownUserName: String?): LiveData<PersonalSettingsGetDataResponseModel> {

        val personalSettingsGetData = MutableLiveData<PersonalSettingsGetDataResponseModel>()

        Log.d("TAG", "otherUserProfile: call ")

        RetrofitClient.instance?.api?.personalSettingsGetData(ownUserName)?.enqueue(object : Callback<PersonalSettingsGetDataResponseModel>{
            override fun onResponse(
                call: Call<PersonalSettingsGetDataResponseModel>,
                response: Response<PersonalSettingsGetDataResponseModel>
            ) {
                if (response.isSuccessful){
                    personalSettingsGetData.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
                showErrorLog(Gson().toJson(response.raw().toString()))
                showErrorLog(Gson().toJson(response.body().toString()))
            }

            override fun onFailure(call: Call<PersonalSettingsGetDataResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                failureResult.value=true
            }

        })

        return personalSettingsGetData
    }

    fun getUpdatePersonalSettings(
        userName: String?,
        country: String,
        state: String,
        city: String,
        zip: String,
        fullName: String,
        interest: Int,
        dateOfBirth: String,
        email: String
    ): LiveData<UpdatePersonalSettingsResponseModel> {

        val personalSettingsUpdateData = MutableLiveData<UpdatePersonalSettingsResponseModel>()

        Log.d("TAG", "otherUserProfile: call ")

        var model: UpdatePersonalSettingsPostingModel = UpdatePersonalSettingsPostingModel(userName,country,state,city,zip,fullName,interest,dateOfBirth,email)

        RetrofitClient.instance?.api?.getUpdatePersonalSettings(model)?.enqueue(object : Callback<UpdatePersonalSettingsResponseModel>{
            override fun onResponse(
                call: Call<UpdatePersonalSettingsResponseModel>,
                response: Response<UpdatePersonalSettingsResponseModel>
            ) {
                if (response.isSuccessful){
                    personalSettingsUpdateData.value=response.body()
                }
                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
                showErrorLog(Gson().toJson(response.raw().toString()))
                showErrorLog(Gson().toJson(response.body().toString()))
            }

            override fun onFailure(call: Call<UpdatePersonalSettingsResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                failureResult.value=true
            }
        })

        return personalSettingsUpdateData

    }

    fun getMessageDelete(
        stringsFromLoop: MutableList<String>,
        customDialog: ProgressCustomDialog?,
        chatRoomActivity: ChatRoomActivity
    ): LiveData<LiveChatMessageDeleteResponseModel> {
        val messageDelete = MutableLiveData<LiveChatMessageDeleteResponseModel>()

        val values = listOf("407198", "407197")

        RetrofitClient.instance?.api?.getChatMessageDelete(stringsFromLoop)?.enqueue(object :Callback<LiveChatMessageDeleteResponseModel>{
            override fun onResponse(
                call: Call<LiveChatMessageDeleteResponseModel>,
                response: Response<LiveChatMessageDeleteResponseModel>
            ) {
                if (response.isSuccessful){
                    messageDelete.value=response.body()
                }else{
                    customDialog?.dismiss()
                    Toast.makeText(chatRoomActivity,""+response.message(),Toast.LENGTH_SHORT).show()
                }

                Log.d("TAG", "onResponse: " + response.raw().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.body().toString())
                Log.d("TAG", "onResponse: " + response.errorBody()?.string())
                Log.d("TAG", "onResponse: " + response.message()+response.code())
                showErrorLog(Gson().toJson(response.body()))
            }

            override fun onFailure(call: Call<LiveChatMessageDeleteResponseModel>, t: Throwable) {
                Log.d("TAG", "onFailure: " + t.message)
                customDialog?.dismiss()
                Toast.makeText(chatRoomActivity,""+t.message,Toast.LENGTH_SHORT).show()
            }

        })

        return messageDelete
    }


}