package com.btjnonbrokerage.Fragment.OnboardingScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.NavigationViewModel
import com.btjnonbrokerage.databinding.FragmentOnboardingTour1Binding

class OnboardingTour1Fragment : BaseFragment<FragmentOnboardingTour1Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnboardingTour1Binding = FragmentOnboardingTour1Binding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvNextBtn1.setOnClickListener {
            NavigationViewModel.onboardingNext.postValue(true)
        }



    }
}