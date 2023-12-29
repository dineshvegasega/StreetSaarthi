package com.streetsaarthi.screens.onboarding.register

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RegisterAdapter (fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return if (position === 0) {
            Register1()
        } else if (position === 1) {
            Register2()
        }  else {
            Register3()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}