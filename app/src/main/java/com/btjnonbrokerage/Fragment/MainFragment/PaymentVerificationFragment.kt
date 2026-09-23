package com.btjnonbrokerage.Fragment.MainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.ShowVerificationCodeDialogFragment
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.Model.DropdownItem
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.SpinnerItemAdapter
import com.btjnonbrokerage.databinding.FragmentPaymentVerificationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentVerificationFragment : BaseFragment<FragmentPaymentVerificationBinding>() {

    @Inject
    lateinit var paymentViewModel: PaymentViewModel

    private var pay_type = ""

    private var dropdownItems = listOf(

        DropdownItem(R.drawable.homey, "Select Payment Type"),
        DropdownItem(R.drawable.homey, "House Rent"),
        DropdownItem(R.drawable.homey, "Flat Rent"),
        DropdownItem(R.drawable.homey, "Apartment Rent"),

//        DropdownItem(R.drawable.school, "Office Rent"),
//        DropdownItem(R.drawable.tutionfees, "Security Deposit")
    )

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPaymentVerificationBinding =
        FragmentPaymentVerificationBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userNo = MySharedPreferences(requireContext()).getUserNumber()
        binding.tvUserNo.setText(userNo)

        binding.backToolbar.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
        // Create a custom ArrayAdapter for the Spinner
        val spinnerAdapter = SpinnerItemAdapter(requireContext(), dropdownItems)
        binding.spinner.adapter = spinnerAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = dropdownItems[position]
                pay_type = dropdownItems[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle if needed
            }
        }

        binding.tvTermCondition.setOnClickListener {
            findNavController().navigate(PaymentVerificationFragmentDirections.actionPaymentVerificationFragmentToWebViewFragment("https://btjnonbrokerage.com/term-conditions.php"))
        }

        binding.tvGetStarted.setOnClickListener {
            val name = binding.yourname.text.toString()
            val checkBox = binding.checkBox

            if (pay_type == "Select Payment Type") {
            showErrorSnackBar("Please Select Payment Type", true)
        }
            else if (name.isEmpty()) {
                showErrorSnackBar("Please Enter Your Name", true)
            }
            else if(!checkBox.isChecked){
                showErrorSnackBar("Please accept the terms and conditions",false)
            }

            else {
                paymentNumberVerifyAPICall(name, pay_type)
            }

        }

        binding.tvUserNo.setOnClickListener {
            showErrorSnackBar("Number already registered", false)
        }

        paymentNumberVerifyAPIResponse()
    }


    private fun paymentNumberVerifyAPICall(name: String, pay_type: String) {
        val uid = MySharedPreferences(requireContext()).getToken()
        val phone = MySharedPreferences(requireContext()).getUserNumber()
        if (uid != null && phone != null) {
            paymentViewModel.paymentNumberVerify(
                PaymentNumberVerifyRequest(
                    "+91",
                    name,
                    pay_type,
                    phone,
                    uid
                )
            )
        }
    }

    private fun paymentNumberVerifyAPIResponse() {
        paymentViewModel.paymentNumberVerifyLiveData.observe(viewLifecycleOwner) {
            try {
                if (it?.status == "success") {
                    if (findNavController().currentDestination?.id == R.id.paymentVerificationFragment) {
                        val token = it.pay_token ?: "mock_pay_token_12345"
                        val action =
                            PaymentVerificationFragmentDirections.actionPaymentVerificationFragmentToPayRentFragment(
                                token
                            )
                        findNavController().navigate(action)
                    }
                } else if (it != null) {
                    showErrorSnackBar(it.msg ?: "Entered Number is not registered", false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.widget.Toast.makeText(requireContext(), "Navigation Error: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }


}