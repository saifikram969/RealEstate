package com.btjnonbrokerage.Fragment.OnboardingScreens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.NavigationViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentOnBoardingTour3Binding

class OnBoardingTour3Fragment : BaseFragment<FragmentOnBoardingTour3Binding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOnBoardingTour3Binding = FragmentOnBoardingTour3Binding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onboardFinishbt.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_logInFormFragment)
        }

        binding.ivBackBtn3.setOnClickListener {
            NavigationViewModel.onboardingBack.postValue(true)
        }




    }
}