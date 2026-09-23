package com.btjnonbrokerage.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerOnBoardingAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentsList = mutableListOf<Fragment>()

    fun addFragments(vararg fragment: Fragment){
        fragmentsList.addAll(fragment)
    }
    override fun getItemCount(): Int = fragmentsList.size

    override fun createFragment(position: Int): Fragment {
        return  fragmentsList[position]
    }

}