package com.nihal.chatapplication.model

data class Chat (
    var sender: String = "",
    var receiver: String = "",
    val message: String = "",
    val isSeen: Boolean = false
)