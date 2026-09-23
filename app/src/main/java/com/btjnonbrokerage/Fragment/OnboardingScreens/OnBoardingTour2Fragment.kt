package com.btjnonbrokerage.Fragment.OnboardingScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.NavigationViewModel
import com.btjnonbrokerage.databinding.FragmentOnBoardingTour2Binding

class OnBoardingTour2Fragment : BaseFragment<FragmentOnBoardingTour2Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnBoardingTour2Binding = FragmentOnBoardingTour2Binding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvNextBtn2.setOnClickListener {
            NavigationViewModel.onboardingNext.postValue(true)
        }

        binding.ivBackBtn.setOnClickListener {
            NavigationViewModel.onboardingBack.postValue(true)

        }



    }
}