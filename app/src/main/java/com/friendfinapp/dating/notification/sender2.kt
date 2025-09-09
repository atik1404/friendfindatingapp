package com.friendfinapp.dating.notification

class Sender2 {
    private var data: Data? = null
    var to: String? = null

    constructor() {}
    constructor(data: Data?, to: String?) {

        this.data = data
        this.to = to
    }
}