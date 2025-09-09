package com.friendfinapp.dating.notification

class Token {
    //this is the model when user log in then one of log in token is created for each user
    var token: String? = null
    var isactive: String? = null

    constructor() {}
    constructor(token: String?,isactive:String?) {
        this.token = token
        this.isactive=isactive
    }
}
