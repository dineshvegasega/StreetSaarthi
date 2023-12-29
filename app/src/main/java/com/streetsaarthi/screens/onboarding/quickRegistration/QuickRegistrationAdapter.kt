package com.streetsaarthi.screens.onboarding.quickRegistration

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.streetsaarthi.screens.onboarding.register.Register3

class QuickRegistrationAdapter (fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return if (position === 0) {
            QuickRegistration1()
        } else if (position === 1) {
            QuickRegistration2()
        }  else {
            Register3()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}