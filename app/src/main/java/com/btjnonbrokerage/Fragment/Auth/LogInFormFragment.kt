package com.btjnonbrokerage.Fragment.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.MVVM.APIModel.Auth.Login.LoginRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentLogInFormBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LogInFormFragment : BaseFragment<FragmentLogInFormBinding>() {

    private var countryCodeWithPlus = "+91"
    @Inject
    lateinit var viewModel: AuthViewModel
    var backPress = false

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLogInFormBinding = FragmentLogInFormBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set up click listeners
        binding.loginTv.setOnClickListener {
            hideKeyboard()

            val phoneNumber = binding.etPhoneNumber.text.toString()
            if (phoneNumber.isEmpty()){
                showErrorSnackBar("Please enter your number", true)
            }else if(phoneNumber.length!=10){
                showErrorSnackBar("Number Must Contain 10 Digit", true)
            }else{
               viewModel.loginViewModel(LoginRequest(phoneNumber, countryCodeWithPlus))
                backPress = true
            }

        }



        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_logInFormFragment_to_RegisterWithEmailFragment)
        }

        viewModel.loginLiveData.observe(viewLifecycleOwner){
            if (it.status == "success" && backPress){
                val action = LogInFormFragmentDirections.actionLogInFormFragmentToOTPFragment(it.uid)
                findNavController().navigate(action)
                backPress = false
            }
            else{

                showErrorSnackBar(it.msg, true)

                if (it.msg.equals("Mobile is not Registered")){
                    findNavController().navigate(R.id.action_logInFormFragment_to_RegisterWithEmailFragment)
                }

            }
        }


    }


}
