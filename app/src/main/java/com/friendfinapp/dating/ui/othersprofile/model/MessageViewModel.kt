package com.friendfinapp.dating.ui.othersprofile.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.friendfinapp.dating.ui.chatroom.responsemodel.LiveChatTokenResponseModel
import com.friendfinapp.dating.ui.mainrepo.MainRepo
import com.friendfinapp.dating.ui.othersprofile.responsemodel.BlockUnblockResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.MessageResponseModel
import com.friendfinapp.dating.ui.othersprofile.responsemodel.OthersProfileResponseModel
import retrofit2.http.POST
import java.io.File

class MessageViewModel: ViewModel() {

    private val repo = MainRepo()


    var failureResult = repo.failureResult
    fun getMessage(sendMessage: SendMessagePostingModel) : LiveData<MessageResponseModel> = repo.sendMessage(sendMessage)

    fun getMessageWithFile(
        fromUserName: String,
        toUserName: String,
        textMessageBody: String,
        repliedTo: Int,
        pendingApproval: Int,
        imageUri: Uri,
        file: File,
        progressCallback: (Int) -> Unit,

        ): LiveData<MessageResponseModel> = repo.sendMessageWithFile(fromUserName,toUserName, textMessageBody, repliedTo, pendingApproval, imageUri, file,progressCallback)

    fun getMessageWithVideoFile(
        fromUserName: String,
        toUserName: String,
        textMessageBody: String,
        repliedTo: Int,
        pendingApproval: Int,
        videoUri: Uri,
        file: File,
        progressCallback: (Int) -> Unit,

        ): LiveData<MessageResponseModel> = repo.sendMessageWithVideoFile(fromUserName,toUserName, textMessageBody, repliedTo, pendingApproval, videoUri, file,progressCallback)


    fun getMessageWithOnlyText(fromUserName:String,
                           toUserName:String,
                           textMessageBody:String,
                           repliedTo:Int,
                           pendingApproval:Int,

                           ): LiveData<MessageResponseModel> = repo.sendMessageWithOnlyText(fromUserName,toUserName, textMessageBody, repliedTo, pendingApproval)

    fun blockedUnblockedUser(model: BlockedUnblockedPostingModel) : LiveData<BlockUnblockResponseModel> = repo.blockedUnblockedUser(model)
    fun otherUserProfile(ownUserName: String?,OtherUserName: String?) : LiveData<OthersProfileResponseModel> = repo.otherUserProfile(ownUserName,OtherUserName)
    fun getUserNotificationToken(username: String?) :LiveData<LiveChatTokenResponseModel> = repo.getUserNotificationToken(username)

    fun messageForwardToMultipleUser(params: ForwardMessagePostingModel) :LiveData<MessageResponseModel> = repo.forwardMessageToMultipleUser(params)

    fun getMessageWithAudio(
        fromUserName: String,
        toUserName: String,
        fileUri: Uri,
        file: File,
        recordingDuration: Long,

        ): LiveData<MessageResponseModel> = repo.sendMessageWithAudio(fromUserName,toUserName,fileUri, file,recordingDuration)


}