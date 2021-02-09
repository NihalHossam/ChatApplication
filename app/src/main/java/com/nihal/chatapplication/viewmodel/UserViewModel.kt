package com.nihal.chatapplication.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nihal.chatapplication.model.Chat
import com.nihal.chatapplication.model.User
import com.nihal.chatapplication.repository.Repository
import com.nihal.chatapplication.utils.Resource
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class UserViewModel @ViewModelInject constructor(
    private val repository: Repository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val userLiveData = MutableLiveData<Resource<User>>()
    private val usersList = MutableLiveData<List<User>>()
    private val chat = MutableLiveData<List<Chat>>()
    lateinit var receiver: User
    lateinit var seenListener: ValueEventListener

    fun getUserID(): String{
        return firebaseAuth.currentUser!!.uid
    }

    fun getUserReference() : LiveData<Resource<User>> {
        val userID = getUserID()
        repository.getUserReference(userID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userLiveData.postValue(Resource.success(user))
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return userLiveData
    }

    fun getAllUsers(): MutableLiveData<List<User>> {
        repository.getAllUsers().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listOfUsers = ArrayList<User>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (!(user!!.id.equals(getUserID()))) {
                        listOfUsers.add(user)
                    }
                }
                usersList.value = listOfUsers
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return usersList
    }

    fun searchUsers(searchString: String): MutableLiveData<List<User>> {
        repository.getAllUsers()
            .startAt(searchString)
            .endAt(searchString + "\uf0ff")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfUsers = ArrayList<User>()
                    for (dataSnapshot in snapshot.children) {
                        val user = dataSnapshot.getValue(User::class.java)
                        if (!user!!.id.equals(getUserID())) {
                            listOfUsers.add(user)
                        }
                    }
                    usersList.value = listOfUsers
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return usersList
    }

    fun getAllMessagesInChat(): MutableLiveData<List<Chat>> {
        repository.getChat().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = ArrayList<Chat>()
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.receiver.equals(getUserID()) && chat.sender.equals(receiver.id)
                        || chat.receiver.equals(receiver.id) && chat.sender.equals(getUserID())
                    ) {
                        chatList.add(chat)
                    }
                }
                chat.value = chatList
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return chat
    }

    fun sendMessage(messageToSend: String) {
        val hashMap = HashMap<String, Any>()
        hashMap["sender"] = getUserID()
        hashMap["receiver"] = receiver.id
        hashMap["message"] = messageToSend
        hashMap["isSeen"] = false

        repository.getChat().push().setValue(hashMap)

        val senderChatReference = repository.getChatList().child(getUserID()).child(receiver.id)
        senderChatReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    senderChatReference.child("id").setValue(receiver.id)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val receiverChatReference = repository.getChatList().child(receiver.id).child(getUserID())
        receiverChatReference.child("id").setValue(getUserID())
    }

    fun setMessagesSeen(){
        seenListener = repository.getChat().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat!!.receiver.equals(getUserID()) && chat.sender
                            .equals(receiver.id)) {
                        val hashMap = HashMap<String, Any>()
                        hashMap["isSeen"] = true
                        dataSnapshot.ref.updateChildren(hashMap)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun removeSeenListener() {
        repository.getChat().removeEventListener(seenListener)
    }

}
