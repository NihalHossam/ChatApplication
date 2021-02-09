package com.nihal.chatapplication.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nihal.chatapplication.adapters.UsersAdapter
import com.nihal.chatapplication.databinding.FragmentUsersBinding
import com.nihal.chatapplication.ui.activities.MessageActivity
import com.nihal.chatapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var adapter: UsersAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        initRecyclerView()
        displayUsers()
        setSearchListeners()
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = UsersAdapter()
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.usersRecyclerView.adapter = adapter
        setUserClickListener()
    }

    private fun displayUsers() {
        userViewModel.getAllUsers().observe(viewLifecycleOwner, { usersList ->
            adapter.submitList(usersList)
        })
    }

    /**
     * Sends a bundle to the Details fragment with the house that was clicked from the list.
     */
    private fun setUserClickListener() {
        adapter.setOnItemClickListener { user ->
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun setSearchListeners(){

        binding.searchInUsers.setOnClickListener{ setSearchBarVisibility() }

        binding.searchInUsers.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) setSearchBarVisibility()
        }

        binding.cancelButton.setOnClickListener{ view ->
            setCancelSearchVisibility(view)
        }

        binding.searchInUsers.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                searchUsers(s.toString().toLowerCase(Locale.ROOT))
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun searchUsers(searchName: String) {
        userViewModel.searchUsers(searchName).observe(viewLifecycleOwner, { searchResult ->
            adapter.submitList(searchResult)
        })
    }

    private fun setSearchBarVisibility(){
        binding.apply {
            cancelButton.visibility = View.VISIBLE
            searchInUsers.isFocusable = true
            searchInUsers.isFocusableInTouchMode = true
            searchInUsers.requestFocus()
        }
    }

    private fun setCancelSearchVisibility(view: View){
        binding.searchInUsers.text.clear()
        binding.cancelButton.visibility = View.GONE
        binding.userFragmentLayout.clearFocus()
        displayUsers()
        hideKeyboard(view)
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


}