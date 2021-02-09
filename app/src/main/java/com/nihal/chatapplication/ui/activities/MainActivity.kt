package com.nihal.chatapplication.ui.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nihal.chatapplication.R
import com.nihal.chatapplication.adapters.ViewPagerAdapter
import com.nihal.chatapplication.databinding.ActivityMainBinding
import com.nihal.chatapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.title = ""
        binding.viewPager.adapter = ViewPagerAdapter(this)
        setUsername()
        setTabLayout()
        setTabLayoutListener()
    }

    private fun setUsername() {
        userViewModel.getUserReference().observe(this, {
            binding.username.text = it.data?.username
            if (it.data?.imageURL.equals("default")) {
                binding.profileImage.setImageResource(R.drawable.defaultprofilepicture);
            } else {
                Glide.with(applicationContext).load(it.data?.imageURL).into(binding.profileImage)
            }
        })
    }

    private fun setTabLayout() {
        val tabLayout = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Chats"
                    tab.setIcon(R.drawable.ic_chat)
                }
                1 -> {
                    tab.text = "Users"
                    tab.setIcon(R.drawable.ic_users)
                }
                2 -> {
                    tab.text = "Profile"
                    tab.setIcon(R.drawable.ic_account)
                }
            }
        }
        tabLayout.attach()
    }

    private fun setTabLayoutListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (currentFocus != null) {
                    val imm =
                        currentFocus!!.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
//                val cancelButton = findViewById<ImageButton>(R.id.cancelButton)
//                if (cancelButton != null) {
//                    cancelButton.visibility = View.GONE
//                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}