package com.friendfinapp.dating.notification

class Data {
    var click_action : String? = null
    var fromUserName: String? = null
    var toUserName: String? = null
    var id: String? = null
    var body: String? = null
    var title: String? = null
    var type: String? = null
    var senderName: String? = null
    var senderUser: String? = null
    var receiverUser: String? = null
    var toUserImage: String? = null
    var messageType: String? = null
    var sound: String? = null

    var icon = 0

    constructor() {}
//    constructor(
//        click_action : String?,
//        sender: String?,
//        receiver: String?,
//        id: String?,
//        body: String?,
//        title: String?,
//        type: String?,
//        sound: String?
//    ) {
//        this.click_action
//        this.sender = sender
//        this.receiver = receiver
//        this.id = id
//        this.body = body
//        this.title = title
//        this.type = type
//        this.sound = sound
//    }

    constructor(
        click_action : String?,
        fromUserName: String?,
        toUserName: String?,
        senderName: String?,
        senderUser: String?,
        receiverUser: String?,
        toUserImage: String?,
        icon: Int,
        body: String?,
        title: String?,
        type: String?,
        messageType: String?,
        sound: String?
    ) {
        this.click_action=click_action
        this.fromUserName = fromUserName
        this.toUserName = toUserName
        this.senderName = senderName
        this.senderUser = senderUser
        this.receiverUser = receiverUser
        this.toUserImage = toUserImage
        this.icon = icon
        this.body = body
        this.title = title
        this.type = type
        this.messageType = messageType
        this.sound = sound
    }
}
