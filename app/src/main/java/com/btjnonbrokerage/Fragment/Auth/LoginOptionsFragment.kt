package com.btjnonbrokerage.Fragment.Auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.databinding.FragmentLoginOptionsBinding


class LoginOptionsFragment : BaseFragment<FragmentLoginOptionsBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginOptionsBinding  = FragmentLoginOptionsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMailLogin.setOnClickListener {
          //  findNavController().navigate(R.id.action_loginOptionsFragment_to_logInFormFragment)
        }

      /*  binding.authWithNumber.setOnClickListener {
          //  findNavController().navigate(R.id.action_loginOptionsFragment_to_numberFragment)
        }*/

       /* binding.cvGoogleAuth.setOnClickListener {
            showErrorSnackBar("Please log-in with email",false)
        }*/

        binding.tvRegister.setOnClickListener {
          //  findNavController().navigate(R.id.action_loginOptionsFragment_to_registerFormFragment)
        }






    }

}