package com.nihal.chatapplication.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihal.chatapplication.adapters.ChatsAdapter
import com.nihal.chatapplication.databinding.FragmentChatsBinding
import com.nihal.chatapplication.ui.activities.MessageActivity
import com.nihal.chatapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatsFragment : Fragment() {

    lateinit var binding: FragmentChatsBinding
    private lateinit var adapter: ChatsAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        initRecyclerView()
        showUserChatList()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ChatsAdapter(userViewModel, viewLifecycleOwner)
        binding.chatsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.chatsRecyclerView.adapter = adapter
        setUserClickListener()
    }

    private fun setUserClickListener() {
        adapter.setOnItemClickListener { user ->
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun showUserChatList(){
        userViewModel.getUserChatList().observe(viewLifecycleOwner, { usersList ->
            adapter.submitList(usersList)
        })
    }

}