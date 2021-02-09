package com.nihal.chatapplication.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihal.chatapplication.BR
import com.nihal.chatapplication.R
import com.nihal.chatapplication.adapters.MessagesAdapter
import com.nihal.chatapplication.databinding.ActivityMessageBinding
import com.nihal.chatapplication.model.User
import com.nihal.chatapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        initRecyclerView()
        getReceiverUser()
        setToolbar()
        setSendMessageListener()
        displayAllMessages()
        setMessagesSeen()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        adapter = MessagesAdapter()
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        binding.chatMessagesRecyclerView.layoutManager = linearLayoutManager
        binding.chatMessagesRecyclerView.adapter = adapter
    }

    private fun getReceiverUser() {
        val receiverUser: User = intent.getSerializableExtra("user") as User
        userViewModel.receiver = receiverUser
        binding.setVariable(BR.user, receiverUser)

    }

    private fun setSendMessageListener() {
        binding.sendButton.setOnClickListener {
            val messageToSend = binding.textToSendEditText.text.toString()
            if (messageToSend != "") {
                userViewModel.sendMessage(messageToSend)
            }
            binding.textToSendEditText.setText("")
        }
    }

    private fun displayAllMessages(){
        userViewModel.getAllMessagesInChat().observe(this, { chatList ->
            adapter.submitList(chatList)
            adapter.notifyDataSetChanged()
        })
    }

    private fun setMessagesSeen() {
        userViewModel.setMessagesSeen()
    }

    override fun onPause() {
        super.onPause()
        userViewModel.removeSeenListener()
    }

}