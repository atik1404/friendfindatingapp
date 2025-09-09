package com.friendfinapp.dating.notification

class Sender3 {

    var to: String? = null
    private var data: Data? = null
    private var notification: Data? = null
    var priority : String?=null

    constructor() {}
    constructor(notification: Data?, data: Data?, to: String?,priority: String?) {
        this.notification = notification
        this.data = data
        this.to = to
        this.priority=priority
    }
}