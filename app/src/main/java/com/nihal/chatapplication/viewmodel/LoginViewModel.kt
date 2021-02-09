package com.nihal.chatapplication.viewmodel

import android.text.TextUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.nihal.chatapplication.model.User
import com.nihal.chatapplication.repository.Repository
import com.nihal.chatapplication.utils.NetworkControl
import com.nihal.chatapplication.utils.Resource
import java.util.*
import kotlin.collections.HashMap

class LoginViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val firebaseAuth: FirebaseAuth,
    private val networkControl: NetworkControl
) : ViewModel() {

    private val userLiveData = MutableLiveData<Resource<User>>()

    fun signUpUser(email: String, name: String, password: String): LiveData<Resource<User>> {
        when {
            TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) -> {
                userLiveData.postValue(Resource.error("All fields should be filled.", null))
            }

            password.length < 8 -> {
                userLiveData.postValue(Resource.error("Password must not be less than 8 characters.", null))
            }

            networkControl.isInternetConnected() -> {
                userLiveData.postValue(Resource.loading(null))
                repository.signUpUser(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = firebaseAuth.currentUser?.uid
                        val searchName = name.toLowerCase(Locale.ROOT)
                        val hashMap : HashMap<String, Any> = HashMap()
                        hashMap["id"] = userID!!
                        hashMap["username"] = name
                        hashMap["imageURL"] = "default"
                        hashMap["searchName"] = searchName
                        repository.getUserReference(userID).setValue(hashMap).addOnCompleteListener { task2 ->
                            if(task2.isSuccessful){
                                userLiveData.postValue(Resource.success(User(
                                    id = userID, username = name, imageURL = "default", searchName = searchName)))
                            }
                        }
                    } else {
                        userLiveData.postValue(Resource.error("Sign up failed. Email already exists.", null))
                    }
                }
            } else -> {
                 userLiveData.postValue(Resource.error("No internet connection.", null))
            }
        }
        return userLiveData
    }

    fun signInUser(email: String, password: String): LiveData<Resource<User>> {
        when {

            TextUtils.isEmpty(email) || TextUtils.isEmpty(password)  -> {
                userLiveData.postValue(Resource.error("All fields should be filled.", null))
            }

            networkControl.isInternetConnected() -> {
                userLiveData.postValue(Resource.loading(null))
                repository.signInUser(email, password).addOnCompleteListener{ task ->
                    if(task.isSuccessful){
                        getUserReference()
                    }else{
                        userLiveData.postValue(Resource.error("Logging in failed.", null))
                    }
                 }
            } else -> { userLiveData.postValue(Resource.error("No internet connection.", null)) }
        }
        return userLiveData
    }

    fun getUserReference() : LiveData<Resource<User>>{
        val userID = firebaseAuth.currentUser?.uid
        repository.getUserReference(userID!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userLiveData.postValue(Resource.success(user))
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        return userLiveData
    }

}