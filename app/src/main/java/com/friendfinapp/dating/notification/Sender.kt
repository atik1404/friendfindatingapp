package com.friendfinapp.dating.notification

class Sender {
    //this model for sender users
    private var notification: Data? = null
    var to: String? = null

    constructor() {}
    constructor(data: Data?, to: String?) {
        this.notification = data
        this.to = to
    }
}
