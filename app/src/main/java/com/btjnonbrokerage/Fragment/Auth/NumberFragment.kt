package com.btjnonbrokerage.Fragment.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.databinding.FragmentNumberBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NumberFragment : BaseFragment<FragmentNumberBinding>() {

    @Inject
    lateinit var viewModel: AuthViewModel

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNumberBinding = FragmentNumberBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvGetOtp.setOnClickListener {
            if (binding.etNumber.text.isNotEmpty() && binding.etNumber.text.length == 10 &&
                binding.etNumberName.text.isNotEmpty()
            ) {
                loader.show()
//                viewModel.registerWithPhoneViewModel(
//                    RegisterWithPhoneRequest(
//                        binding.etNumberName.text.toString(),
//                        "+91", binding.etNumber.text.toString(), "123456"
//                    )
//                )
            } else {
                if (binding.etNumber.text.isEmpty()) {
                    showErrorSnackBar("Please enter your number", false)
                } else if (binding.etNumber.text.length != 10) {
                    showErrorSnackBar("Please enter valid number", false)
                } else {
                    showErrorSnackBar("Please enter your name", false)
                }
            }
        }

        viewModel.registerWithPhoneRequestLiveData.observe(viewLifecycleOwner) {
            loader.dismiss()
            if (it.status == "success") {
              //  findNavController().navigate(R.id.action_numberFragment_to_OTP_Fragment)
            } else if (it.status == "failure") {
                showErrorSnackBar(it.msg, false)
            }
        }


    }

}
