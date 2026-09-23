package com.btjnonbrokerage.Fragment.MainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Model.landLoadNameAndNumber
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentPayRentBinding

class PayRentFragment : BaseFragment<FragmentPayRentBinding>() {
    val args: PayRentFragmentArgs by navArgs()
    private var digittype: String? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPayRentBinding = FragmentPayRentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Extract token from arguments
        val payToken = args.payToken

        // Setup back navigation
        setupBackNavigation()

        // Setup Click Listeners
        setupRadioButtonListeners(payToken)

        // Handle system back press
        handleSystemBackPressed()
    }

    // Function to handle system back press
    private fun handleSystemBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(PayRentFragmentDirections.actionPayRentFragmentToHomeFragment())
                }
            }
        )
    }

    // Function to load fragments into the container
    private fun loadFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.container2, fragment)
            .commit()
    }

    // Function to get Landlord name and number
    private fun getLandlordNameAndNumber(): landLoadNameAndNumber {
        val landLordName = binding.etLandLordname.text.toString()
        val landLordNumber = binding.etPhoneNumber.text.toString()
        return landLoadNameAndNumber(landLordName, landLordNumber, digittype)
    }

    // Function to setup RadioButton listeners
    private fun setupRadioButtonListeners(payToken: String) {
        binding.llRbBankAccount.setOnClickListener {
            updateRadioButtonState(isUpiSelected = false)
            loadFragment(
                PayAccountFragment(payToken, landlordNameAndNumber = {
                    getLandlordNameAndNumber()
                })
            )
        }

        binding.llRbBankUpi.setOnClickListener {
            updateRadioButtonState(isUpiSelected = true)
            loadFragment(
                PayUpiFragment(payToken, landlordNameAndNumber = {
                    getLandlordNameAndNumber()
                })
            )
        }
    }

    // Function to update the state of RadioButtons
    private fun updateRadioButtonState(isUpiSelected: Boolean) {
        binding.apply {
            ivUpiId.setImageResource(if (isUpiSelected) R.drawable.radioselected else R.drawable.radiounselected)
            ivBankAccount.setImageResource(if (isUpiSelected) R.drawable.radiounselected else R.drawable.radioselected)
        }
    }

    // Function to setup back navigation
    private fun setupBackNavigation() {
        binding.imgBackBtn.setOnClickListener {
            findNavController().navigate(PayRentFragmentDirections.actionPayRentFragmentToHomeFragment())
        }
    }
}
