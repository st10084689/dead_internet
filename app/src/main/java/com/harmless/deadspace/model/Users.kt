package com.harmless.deadspace.model

import java.util.Date

data class Users(
    val userId : String,
    val name: String,
    val isHost: Boolean,
    val isWaiting:Boolean,
    val matchId:String,
    val date : Date
    )
