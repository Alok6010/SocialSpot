package com.example.socialspot.DataClass

import android.content.IntentSender

class Message {

    val userName: String? = null
    var message: String? = null
    var senderId: String? = null

    constructor(){}

    constructor(message: String?,senderId: String?){

        this.message = message
        this.senderId = senderId

    }


}