package com.nihal.chatapplication.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.nihal.chatapplication.R
import com.nihal.chatapplication.databinding.ActivityMessageBinding
import com.nihal.chatapplication.model.User
import com.nihal.chatapplication.BR
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_message)
        setContentView(binding.root)
        getReceiverUser()
        setToolbar()
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

    private fun getReceiverUser() {
        val receiverUser: User = intent.getSerializableExtra("user") as User
        binding.setVariable(BR.user, receiverUser)
    }
}