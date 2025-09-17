package com.friendfinapp.dating.helper

import com.friendfinapp.dating.ui.fetchprofile.FetchProfileResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.OthersProfileResponseModel
import com.friendfinapp.dating.ui.signup.model.RegisterResponseModel


object Constants {
    const val BaseUrl = "https://friendfin.com/friendfinapi/"
    const val TestBaseUrl = "https://test1.friendfin.com/friendfinapi/"
    // const val BaseUrl = "https://test1.friendfin.com/friendfinapi/"

    //    var TOPIC_ALL_AUDIO_LIST: MutableList<AllRecentClipsResponseModel.Clip>? = ArrayList()
    var USER_INFO = RegisterResponseModel.Data()

    var USER_PROFILE_INFO: MutableList<FetchProfileResponseModel.Data> = ArrayList()
    var USER_PROFILE_INFO_PICTURE: MutableList<FetchProfileResponseModel.Userimage> = ArrayList()
    var USER_OTHER_PROFILE_INFO: MutableList<OthersProfileResponseModel.Data> = ArrayList()
    var USER_ID = ""

    var IS_SUBSCRIBE = false

    var SKUS = ""

    var USER_BLOCKED: MutableList<FetchProfileResponseModel.BlockedUser> = ArrayList()
    var myProfileImage = ""


    //lastMessagescroll checl

    var fromUserName = ""
    var toUserName = ""
    var textMessage = ""
    var imageUrl = ""
    var audioUrl = ""
    var videoUrl = ""

    //    var AUTH_TOKEN = ""
    var length = 0

    //    var TOPIC_CONTENT = 0
//    var TOPIC_ALL_CHECK = true
//    var CLICK_FORWARD = false
//    var CLICK_BACKWARD = false
//    var CLICK_PLAY_PAUSE = false
//    var ADVISORPREVIOUSPOSITION = 0
//
//    //
    var TESTEXO = false
    var duration = 0L
    var hidden = true
    var LockAudio = false
    var finalRecordTime=0L

    var videoCheck = false
}