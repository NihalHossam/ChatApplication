package com.nihal.chatapplication.repository

import com.nihal.chatapplication.firebase.FirebaseSource
import javax.inject.Inject

class Repository @Inject constructor(private val fireBaseSource: FirebaseSource) {

    fun signUpUser(email: String, password: String) = fireBaseSource.signUpUser(email, password)

    fun signInUser(email: String, password: String) = fireBaseSource.signInUser(email, password)

    fun getUserReference(userID: String) = fireBaseSource.getUserReference(userID)

    fun getAllUsers() = fireBaseSource.getAllUsers()


}