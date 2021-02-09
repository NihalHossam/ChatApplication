package com.nihal.chatapplication.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FirebaseSource @Inject constructor(private val firebaseAuth: FirebaseAuth,
     private val firebaseDatabase: FirebaseDatabase, private val firebaseStorage: FirebaseStorage){

    fun signUpUser(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun signInUser(email: String, password: String) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun getUserReference(userID: String) = firebaseDatabase.getReference("Users").child(userID)

    fun getAllUsers() = firebaseDatabase.getReference("Users")

}