package com.btjnonbrokerage.Fragment.MainFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.btjnonbrokerage.Activity.CashFreePaymentActivity
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.BhkTypeDialogBoxFragment
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.Model.landLoadNameAndNumber
import com.btjnonbrokerage.databinding.FragmentPayAccountBinding
import com.btjnonbrokerage.extensions.getName
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PayAccountFragment(
    val payToken: String,
    val landlordNameAndNumber: () -> landLoadNameAndNumber,

) : BaseFragment<FragmentPayAccountBinding>() {

    @Inject
    lateinit var viewModel: PaymentViewModel

    var amountGlobal: String = ""
    private var bhk:String = ""

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPayAccountBinding = FragmentPayAccountBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // IFSC Code Validation
        binding.etIFSCCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val ifscRegex = Regex("^[A-Z]{4}0[A-Z0-9]{6}\$")
                if (ifscRegex.matches(s.toString())) {
                    binding.etIFSCCode.error = null
                } else {
                    binding.etIFSCCode.error = "Invalid IFSC code"
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Beneficiary PAN Validation
        binding.beneficiaryPan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val panRegex = Regex("^[A-Z]{5}[0-9]{4}[A-Z]?\$")
                if (panRegex.matches(s.toString())) {
                    binding.beneficiaryPan.error = null
                } else {
                    binding.beneficiaryPan.error = "Invalid PAN number"
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Tenant PAN Validation
        binding.tenantpan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val panRegex = Regex("^[A-Z]{5}[0-9]{4}[A-Z]?\$")
                if (panRegex.matches(s.toString())) {
                    binding.tenantpan.error = null
                } else {
                    binding.tenantpan.error = "Invalid PAN number"
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // File chooser setup
        val galleryResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.d("Result", result.data?.data.toString())
                    binding.tvFileName.text =
                        result.data?.data?.getName(requireContext()) ?: "No File Chosen"
                }
            }

        binding.tvChooseFile.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "application/pdf"
            galleryResult.launch(intent)
        }

        // Save payment button click listener
        binding.tvSavePayment.setOnClickListener {
            validateAndSubmitPaymentDetails()
        }
        binding.tvBhktype.setOnClickListener {
            val dialog = BhkTypeDialogBoxFragment { selectedBhk ->
                binding.tvBhktype.text = selectedBhk
                bhk = selectedBhk

            }
            dialog.show(parentFragmentManager, "BhkTypeDialog")
        }

        // Rent amount validation to check if tenant PAN is required
        binding.rentamount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateTenantPanBasedOnRent(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validateAndSubmitPaymentDetails() {
        val landlordInfo = landlordNameAndNumber()
        val accountNumber = binding.etBankAccountNo.text.toString()
        val confirmAccountNumber = binding.etConfirmAccountNo.text.toString()
        val ifscCode = binding.etIFSCCode.text.toString()
        val BhkType = binding.tvBhktype.text.toString()
        val address = binding.propertaddress.text.toString()
        val amount = binding.rentamount.text.toString()
        val beneficiaryPan = binding.beneficiaryPan.text.toString()
        val tenantPan = binding.tenantpan.text.toString()
        val userNumber = MySharedPreferences(requireContext()).getUserNumber()
        val formattedBenificiaryPan = beneficiaryPan.trim().uppercase()
        val formatedTenantPan = tenantPan.trim().uppercase()

        Log.d("PaymentAccount" ,landlordInfo.digittype.toString())

        if (landlordInfo.landlordName.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter landlord name", Toast.LENGTH_SHORT).show()
        } else if (landlordInfo.landlordNumber.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please enter landlord number", Toast.LENGTH_SHORT).show()
        }else if (landlordInfo.landlordNumber == userNumber){
            Toast.makeText(requireContext(),"Landlord number and Tenant number can't be same",Toast.LENGTH_SHORT).show()
        } else if (accountNumber.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter bank account number", Toast.LENGTH_SHORT).show()
        } else if (!isValidAccountNumber(accountNumber)) {
            Toast.makeText(requireContext(), "Invalid bank account number", Toast.LENGTH_SHORT).show()
        } else if (confirmAccountNumber.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter confirm account number", Toast.LENGTH_SHORT).show()
        } else if (confirmAccountNumber != accountNumber) {
            Toast.makeText(requireContext(), "Account number doesn't match", Toast.LENGTH_SHORT).show()
        } else if (ifscCode.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter IFSC code", Toast.LENGTH_SHORT).show()
        } else if (binding.etIFSCCode.error != null) {
            Toast.makeText(requireContext(), "Please enter valid IFSC code", Toast.LENGTH_SHORT).show()
        } else if (BhkType.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter BHK type", Toast.LENGTH_SHORT).show()
        } else if (address.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter property address", Toast.LENGTH_SHORT).show()
        } else if (amount.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter rent amount", Toast.LENGTH_SHORT).show()
        } else if (beneficiaryPan.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter beneficiary's PAN", Toast.LENGTH_SHORT).show()
        } else if (binding.beneficiaryPan.error != null) {
            Toast.makeText(requireContext(), "Please enter valid beneficiary's PAN", Toast.LENGTH_SHORT).show()
        } else if (amount.toInt() > 50000 && tenantPan.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter tenant's PAN as rent is more than ₹50,000", Toast.LENGTH_SHORT).show()
        } else if (binding.tenantpan.error != null) {
            Toast.makeText(requireContext(), "Please enter valid tenant's PAN", Toast.LENGTH_SHORT).show()
        }
        else if(formattedBenificiaryPan == formatedTenantPan){
            Toast.makeText(requireContext(),"Beneficiary and Tenant PAN can't be same",Toast.LENGTH_SHORT).show()
        }

        else {
            // If all validations pass, make the API call
            paymentDetailsRequestAPICall(
                payToken,
                address,
                amount,
                tenantPan,
                beneficiaryPan,
                landlordInfo.landlordName,
                landlordInfo.landlordNumber,
                accountNumber,
                ifscCode
            )
        }
    }

    private fun isValidAccountNumber(accountNumber: String): Boolean {
        // Check if the account number contains only digits and has between 9 and 18 digits.
        val accountNumberRegex = Regex("^[0-9]{9,18}\$")
        return accountNumber.matches(accountNumberRegex)
    }

    private fun paymentDetailsRequestAPICall(
        payToken: String,
        address: String,
        amount: String,
        tenantPan: String,
        beneficiaryPan: String,
        landlordName: String,
        landlordNo: String,
        accountNumber: String,
        ifscCode: String
    ) {
        amountGlobal = amount
        viewModel.addPaymentDetails(
            PaymentDetailsRequest(
                acc_num = accountNumber,
                agr_img_url = "",
                beneficiar_pan = beneficiaryPan,
                ifsc = ifscCode,
                landlord_name = landlordName,
                landlord_phone = landlordNo,
                pay_mode = "Account",
                pay_token = payToken,
                property_address = address,
                rent_amt = amount,
                tenant_pan = tenantPan,
                upi_id = "",
                bhk_type = bhk
            )
        )
        savePaymentDetailsResponse()
    }

    private fun savePaymentDetailsResponse() {
        viewModel.addPaymentDetailsLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                Toast.makeText(requireContext(), "Mock Payment Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), com.btjnonbrokerage.Activity.MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                showErrorSnackBar(it.msg, true)
            }
        }
    }

    // Method to check rent amount and validate Tenant PAN
    private fun validateTenantPanBasedOnRent(rent: String) {
        if (rent.isNotEmpty() && rent.toInt() > 50000) {
            if (binding.tenantpan.text.isNullOrEmpty()) {
                binding.tenantpan.error = "Tenant PAN is required if rent is more than ₹50,000"
            }
        } else {
            binding.tenantpan.error = null // Remove error if rent is less or equal to ₹50,000
        }
    }
}
