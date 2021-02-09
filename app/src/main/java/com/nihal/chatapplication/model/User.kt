package com.nihal.chatapplication.model

import java.io.Serializable

data class User(
    var id: String = "",
    var username: String = "",
    val imageURL: String = "",
    val searchName: String = ""
): Serializable