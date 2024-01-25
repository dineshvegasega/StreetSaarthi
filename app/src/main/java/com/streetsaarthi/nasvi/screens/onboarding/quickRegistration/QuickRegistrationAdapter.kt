package com.streetsaarthi.nasvi.screens.onboarding.quickRegistration

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.streetsaarthi.nasvi.screens.onboarding.register.Register1
import com.streetsaarthi.nasvi.screens.onboarding.register.Register2
import com.streetsaarthi.nasvi.screens.onboarding.register.Register3

class QuickRegistrationAdapter (fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> QuickRegistration1()
            1 -> QuickRegistration2()
            else -> QuickRegistration1()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}