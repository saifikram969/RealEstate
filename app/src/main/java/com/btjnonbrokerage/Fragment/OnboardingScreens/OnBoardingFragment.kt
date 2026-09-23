package com.btjnonbrokerage.Fragment.OnboardingScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.NavigationViewModel
import com.btjnonbrokerage.adapters.ViewPagerOnBoardingAdapter
import com.btjnonbrokerage.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnBoardingBinding = FragmentOnBoardingBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPagerAdapter = ViewPagerOnBoardingAdapter(requireActivity())
        viewPagerAdapter.addFragments(
            OnboardingTour1Fragment(),
            OnBoardingTour2Fragment(),
            OnBoardingTour3Fragment()
        )

        binding.viewPager.apply {
            adapter = viewPagerAdapter
        }




        NavigationViewModel.onboardingNext.observe(viewLifecycleOwner){
            if(it){
                nextFragment()
                NavigationViewModel.onboardingNext.postValue(false)
            }
        }
       NavigationViewModel.onboardingBack.observe(viewLifecycleOwner){
           if(it){
               previousFragment()
               NavigationViewModel.onboardingBack.postValue(false)
           }
       }


    }

    private fun nextFragment() {
        val currentItem = binding.viewPager.currentItem
        val maxItem = binding.viewPager.adapter?.itemCount ?: 0
        if (currentItem < maxItem - 1) {
            val nextItem = currentItem + 1
            binding.viewPager.setCurrentItem(nextItem, true)
            Log.d("ViewPager", "Navigated to fragment $nextItem")
        } else {
            Log.d("ViewPager", "Already at the last fragment")
        }
    }
    private fun previousFragment() {
        val currentItem = binding.viewPager.currentItem
        val maxItem = binding.viewPager.adapter?.itemCount ?: 0

        binding.viewPager.setCurrentItem(currentItem-1, true)

//        if (currentItem > 0) {
//
//
//            Log.d("ViewPager", "Navigated to fragment $nextItem")
//        } else {
//            Log.d("ViewPager", "Already at the last fragment")
//        }
    }
}