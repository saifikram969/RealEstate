package com.btjnonbrokerage.DialogBox

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP.VerifyPaymentNumberOTPRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentShowVerificationCodeDialogBinding
import com.btjnonbrokerage.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowVerificationCodeDialogFragment(val name:String, private val paymentType:String, val phone:String) : DialogFragment() {

    @Inject
    lateinit var viewModel: PaymentViewModel

    private var timer = false

    private  lateinit var  binding : FragmentShowVerificationCodeDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowVerificationCodeDialogBinding.inflate(layoutInflater,container,false)
        binding.tvNumber.text = "+91 $phone"

//        viewModel.paymentNumberVerify(PaymentNumberVerifyRequest(country_code = "+91",
//            name = arguments?.getString("name")!!,
//            pay_type = arguments?.getString("payment type")!!,
//            phone = arguments?.getString("phone")!!,
//            uid = MySharedPreferences(requireContext()).getToken().toString()))

        viewModel.paymentNumberVerify(
            PaymentNumberVerifyRequest(country_code = "+91",
            name = name,
            pay_type = paymentType,
            phone = phone,
            uid = MySharedPreferences(requireContext()).getToken().toString())
        )

//        binding.tvVerify.setOnClickListener {
//            val otp = ""
//            if(binding.pinview.text.toString() == "123456") {
//                viewModel.verifyPaymentNumberOTP(
//                    VerifyPaymentNumberOTPRequest("123456",
//                    MySharedPreferences(requireContext()).getToken().toString())
//                )
//                binding.progressBar.show()
//            }
//            else {
//                binding.pinview.setText("")
//                Toast.makeText(requireContext(), "Invalid OTP", Toast.LENGTH_SHORT).show()
//            }
//        }



//        viewModel.paymentNumberVerifyOTPLiveData.observe(viewLifecycleOwner){
//            if(it.status == "success"){
//                findNavController().navigate(R.id.payRentFragment)
//                dismiss()
//            }
//        }

        timer().start()
        return  binding.root
    }

    private fun timer(): CountDownTimer {
        return object : CountDownTimer(60000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val formattedSeconds = String.format("%02d", secondsRemaining)
                binding.tvOtpTimer.text = "00:$formattedSeconds"
                timer = true
            }

            override fun onFinish() {
                binding.tvOtpTimer.text = "Resend OTP"
                timer = false
            }
        }
    }


    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(MATCH_PARENT, -2)
//        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}