package com.nihal.chatapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nihal.chatapplication.ui.fragments.ChatsFragment
import com.nihal.chatapplication.ui.fragments.ProfileFragment
import com.nihal.chatapplication.ui.fragments.UsersFragment
import com.nihal.chatapplication.utils.Constants.numberOfFragments

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return numberOfFragments
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChatsFragment()
            1 -> UsersFragment()
            2 -> ProfileFragment()
            else -> ChatsFragment()
        }
    }


}