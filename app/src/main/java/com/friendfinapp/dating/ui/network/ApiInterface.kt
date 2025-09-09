package com.friendfinapp.dating.ui.network

import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatMessageDeleteResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatPostingModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatResponseModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatTokenResponseModel
import com.friendfinapp.dating.ui.chatsearch.model.SearchResultResponseModel
import com.friendfinapp.dating.ui.chatsearch.model.SuggestionResponseModel
import com.friendfinapp.dating.ui.fetchprofile.FetchProfileResponseModel
import com.friendfinapp.dating.ui.forgetpassword.model.ForgetPasswordResponseModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchNewResponseModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchPostingModel
import com.friendfinapp.dating.ui.individualsearch.model.IndividualSearchResponseModel
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultPostingModel
import com.friendfinapp.dating.ui.individualsearch.serachresultmodel.IndividualSearchResultResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.chatfragment.responsemodel.ChatResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.profilefragment.model.LogOutResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchPostingModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.SearchResponseModel
import com.friendfinapp.dating.ui.landingpage.fragments.serachfragment.model.UserOnlineUpdateModel
import com.friendfinapp.dating.ui.landingpage.model.NotificationPostingModel
import com.friendfinapp.dating.ui.landingpage.model.NotificationResponseModel
import com.friendfinapp.dating.ui.othersprofile.model.BlockedUnblockedPostingModel
import com.friendfinapp.dating.ui.othersprofile.model.ForwardMessagePostingModel
import com.friendfinapp.dating.ui.othersprofile.model.SendMessagePostingModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.BlockUnblockResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.MessageResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.OthersProfileResponseModel
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbuseResponseModel
import com.friendfinapp.dating.ui.reportanabuse.model.ReportAbusedPostingModel
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfilePostingModel
import com.friendfinapp.dating.ui.saveprofile.model.SaveProfileResponseModel
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {


    // @Headers("Accept: application/json")
    @POST("v1/Register")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun registerUser(@Body body: PostingModel): Call<RegisterResponseModel>


    @POST("v1/Login")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun loginUser(@Body body: LogInPostingModel): Call<LoginResponseModel>

    @POST("v1/LoginWithGoogle")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun loginGoogleUser(@Query("email") email: String): Call<LoginResponseModel>


    @POST("v1/ForgotPassword")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun forgetPasswordChange(@Query("email") email: String): Call<ForgetPasswordResponseModel>


    @POST("v1/SaveProfile")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun saveProfile(@Body body: MutableList<SaveProfilePostingModel>): Call<SaveProfileResponseModel>

    @POST("v1/NotificationTokenUpdate")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getNotificationUpdate(@Body body: NotificationPostingModel): Call<NotificationResponseModel>

    @GET("v1/FetchProfile")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun fetchProfile(@Query("username") username: String?): Call<FetchProfileResponseModel>


 @GET("v1/GetNotificationTokenByUsername")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getUserNotificationToken(@Query("username") username: String?): Call<LiveChatTokenResponseModel>

    @GET("v1/OtherFetchProfile")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun otherFetchProfile(@Query("ownUsername") ownUserName: String?,
                          @Query("otherUsername") otherUserName: String?): Call<OthersProfileResponseModel>


    @GET("v1/GetPersonalSettings")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun personalSettingsGetData(@Query("username") username: String?): Call<PersonalSettingsGetDataResponseModel>


//    @GET("v1/GetPersonalSettings")
//    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
//    fun fetchProfile(@Query("username") username: String?): Call<FetchProfileResponseModel>
//


    @GET("v1/SendMessageToListSearchSuggestionUsername")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getServerChatDataHintByKeyword(@Query("fromUsername") fromUsername: String?,
                          @Query("toUsername") toUsername: String?): Call<SuggestionResponseModel>


    @POST("v1/SendMessageToListSearch")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getSearchResult(@Query("fromUsername") fromUsername: String?,
                          @Query("toUsername") toUsername: String?): Call<SearchResultResponseModel>





    @POST("v1/PasswordChange")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getPasswordChange(@Body body: PasswordChangePostingModel): Call<PasswordChangeResponseModel>

    @POST("v1/UpdatePersonalSettings")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getUpdatePersonalSettings(@Body body: UpdatePersonalSettingsPostingModel): Call<UpdatePersonalSettingsResponseModel>

    @POST("v1/FetchUserOnlineUpdate")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getUserOnlineUpdate(@Query("username") userName: String?): Call<UserOnlineUpdateModel>



    @POST("v1/Logout")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getLogOut(): Call<LogOutResponseModel>


    @POST("v1/TopUserSearchV2")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getTopSearch(@Body body: SearchPostingModel): Call<SearchResponseModel>

    @GET("v1/FetchUserSearchByUserProfileAnswers")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getTopSearchWithLocationAndInterest(@Query("username") userName: String?): Call<SearchResponseModel>


    @POST("v1/FetchUserSearch")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getIndividualSearchResult(@Body body: IndividualSearchResultPostingModel): Call<IndividualSearchResultResponseModel>


    @Multipart
    @POST("v1/AddPhoto")
// @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun uploadPhoto(
        @Part("Username") username: RequestBody,
        @Part("PhotoAlbumID") photoAlbum: RequestBody,
        @Part("Name") name: RequestBody,
        @Part("Description") description: RequestBody,
        @Part("Approved") approve: RequestBody,
        @Part("ApprovedDate") approveDate: RequestBody,
        @Part Image: MultipartBody.Part
    ): Call<UploadPhotoResponseModel>

    @Multipart
    @POST("v1/SendMessageWithFile")
// @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendMessageWithFile(
        @Part("FromUsername") fromUsername: RequestBody,
        @Part("ToUsername") toUsername: RequestBody,
        @Part("Body") body: RequestBody,
        @Part("RepliedTo") repliedTo: Int,
        @Part("PendingApproval") pendingApproval: Int,
        @Part FImage: MultipartBody.Part
    ): Call<MessageResponseModel>


    @Multipart
    @POST("v1/SendMessageWithAudio")
// @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendMessageWithAudio(
        @Part("FromUsername") fromUsername: RequestBody,
        @Part("ToUsername") toUsername: RequestBody,
        @Part FAudio: MultipartBody.Part,
        @Part("AudioDuration") audioDuration: RequestBody
    ): Call<MessageResponseModel>

    @Multipart
    @POST("v1/SendMessageWithVideo")
// @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendMessageWithVideo(
        @Part("FromUsername") fromUsername: RequestBody,
        @Part("ToUsername") toUsername: RequestBody,
        @Part FVideo: MultipartBody.Part,
        @Part("VideoDuration") videoDuration: RequestBody
    ): Call<MessageResponseModel>


    @Multipart
    @POST("v1/SendMessageWithFile")
// @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendMessageWithOnlyText(
        @Part("FromUsername") fromUsername: RequestBody,
        @Part("ToUsername") toUsername: RequestBody,
        @Part("Body") body: RequestBody,
        @Part("RepliedTo") repliedTo: Int,
        @Part("PendingApproval") pendingApproval: Int
    ): Call<MessageResponseModel>

    @POST("v1/SendMessage")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendMessage(@Body body: SendMessagePostingModel): Call<MessageResponseModel>

    //https://friendfin.com/friendfinapi/v1/ForwardMessage
    //https://friendfin.com/friendfinapi/v1/ForwardMessage

    @POST("v1/ForwardMessage")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun forwardMessageToMultipleUser(@Body body: ForwardMessagePostingModel): Call<MessageResponseModel>

    @POST("v1/SendMessageToList")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getChatList(@Query("fromUsername") username: String?,
                    @Query("pageNo") page: Int?) : Call<ChatResponseModel>

    @POST("v1/SendMessageHistory")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getChatConversation(@Body body: LiveChatPostingModel): Call<LiveChatResponseModel>

    @POST("v1/Search")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getIndividualUser(@Body body: IndividualSearchPostingModel): Call<IndividualSearchResponseModel>


    @GET("v1/FetchUserSearchByUsername")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getIndividualNewUser(@Query("username") username: String?): Call<IndividualSearchNewResponseModel>


    @POST("v1/BlockUnblockUser")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getBlockedUnblocked(@Body body: BlockedUnblockedPostingModel): Call<BlockUnblockResponseModel>

    @POST("v1/SendAbuseReport")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun sendReport(@Body body: ReportAbusedPostingModel): Call<ReportAbuseResponseModel>

    @POST("v1/DeleteUser")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun delete(@Body body: PersonalSettingsPostingModel): Call<PersonalSettingsResponseModel>

    @POST("v1/SelectedMessageHistoryClean")
    @Headers(*["Content-Type: application/json;charset=utf-8", "Accept: application/json;charset=utf-8"])
    fun getChatMessageDelete(@Body body: List<String>): Call<LiveChatMessageDeleteResponseModel>



//    @POST("authenticate")
//    @FormUrlEncoded
//    fun googleLogInUser(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("uid") socialUid: String,
//        @Field("profile_url") defaultUid: String
//    ): Call<RegisterResponseModel>
//
//
//    @GET("topic/get-all")
//    fun getAllTopic(): Call<TopicResponseModel>
//
//    @GET("advisor/featured")
//    fun getFeatureAdvisor(): Call<FeatureAdvisorModel>
//
//
//    @GET("clips/advisor/{id}")
//    fun getIndividualAdvisor(
//        @Path("id") id: String?,
//        @Query("page") page: Int
//    ): Call<AdvisorResponseModel>?
//
//    @GET("question/clips")
//    fun getAllRecentClips(
//        @Query("page") page: Int,
//        @Header("authorization") token: String
//    ): Call<AllRecentClipsResponseModel>
//
//    @GET("question/topic-clips/{id}")
//    fun getAllRecentTopicIndividual(
//        @Path("id") id: String?,
//        @Query("page") page: Int,
//        @Header("authorization") token: String
//    ): Call<AllRecentClipsResponseModel>
//
//
//    @Headers("Accept: application/json")
//    @POST("upvote/{id}")
//    fun getLikeDislike(
//        @Path("id") id: String,
//        @Header("authorization") token: String
//    ): Call<LikeDislikeResponseModel>
//
//    @Headers("Accept: application/json")
//    @POST("listening/{id}")
//    fun getListening(
//        @Path("id") id: String,
//        @Header("authorization") token: String
//    ): Call<LikeDislikeResponseModel>

}