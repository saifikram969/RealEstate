package com.btjnonbrokerage.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.MVVM.APIModel.Auth.OTP.VerifyOTPRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.AccountCreatedFragment
import com.btjnonbrokerage.MVVM.APIModel.Auth.ResendOTP.ResendOTPRequest
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentOTPBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OTP_Fragment : BaseFragment<FragmentOTPBinding>() {

    private val args: OTP_FragmentArgs by navArgs()
    private var timer = false

    @Inject
    lateinit var authViewModel: AuthViewModel

    lateinit var otpEditTexts: Array<EditText>

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOTPBinding = FragmentOTPBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Modified: Resend OTP button click listener
        binding.countDownTimer.setOnClickListener {
            if (!timer) {
                resendOTPApiFun() // Calls the function to resend the OTP
                timer().start() // Restarts the timer
            } else {
                showErrorSnackBar(
                    "Please wait for the timer to finish",
                    false
                ) // Displays a message if the timer is still running
            }
        }

        binding.tvLetSubmit.setOnClickListener {
            hideKeyboard()
            val otp = binding.pinviewotp.text.toString()
            if (otp.length == 6) {
                verifyOTPApiFun(otp.toString())
            } else {
                Toast.makeText(requireContext(), "Enter 6 Digit Code", Toast.LENGTH_SHORT).show()
            }
        }



        authViewModel.resendOTPResponse.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                showErrorSnackBar("OTP sent successfully", false) // Displays success message
            } else {
                showErrorSnackBar("Failed to resend OTP", true) // Displays failure message
            }
        }


        timer().start()
        otpAPIResponse()
    }



    private fun verifyOTPApiFun(otp: String) {
        authViewModel.verifyOTPViewModel(VerifyOTPRequest(otp, args.uid))
    }

    // Added: Method to call the resend OTP API
    private fun resendOTPApiFun() {
        authViewModel.resendOTPViewModel(ResendOTPRequest(args.uid))
    }

    private fun timer(): CountDownTimer {
        return object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val formattedSeconds = String.format("%02d", secondsRemaining)
                binding.countDownTimer.text = "00:$formattedSeconds"
                timer = true
            }

            override fun onFinish() {
                binding.countDownTimer.text = "Resend OTP"
                timer = false
            }
        }
    }

    private fun otpAPIResponse() {
        authViewModel.verifyOTPLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                MySharedPreferences(requireContext()).setToken(it.userdata.uid)
                MySharedPreferences(requireContext()).setUser(
                    it.userdata.name,
                    it.userdata.mobile,
                    it.userdata.icon)

             val action = OTP_FragmentDirections.actionOTPFragmentToHomeFragment()
                findNavController().navigate(action)

            } else {
                showErrorSnackBar("Invalid OTP", true)

            }
        }
    }
}