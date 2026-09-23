package com.btjnonbrokerage.Fragment.MainFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlin.math.roundToInt
import java.math.BigDecimal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.BhkTypeDialogBoxFragment
import com.btjnonbrokerage.DialogBox.NormalPaymentDialogBoxFragment
import com.btjnonbrokerage.DialogBox.StandardPaymentFragment
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.Model.landLoadNameAndNumber
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentPayUpiBinding
import com.btjnonbrokerage.extensions.getName
import com.btjnonbrokeragee.Activity.RazorPaymentActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PayUpiFragment(
    private val payToken: String,
    private val landlordNameAndNumber: () -> landLoadNameAndNumber
) : BaseFragment<FragmentPayUpiBinding>() {

    @Inject
    lateinit var viewModel: PaymentViewModel

    private var amountGlobal: String = ""

    private var normalPayment = true


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPayUpiBinding = FragmentPayUpiBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()
        setupFileChooser()
        setupPaymentButtonClickListener()
        observePaymentDetails()

        binding.tvBhk.setOnClickListener {
            BhkTypeDialogBoxFragment { selectedBhk ->
                binding.tvBhk.text = selectedBhk
            }.show(parentFragmentManager, "BhkTypeDialog")
        }

        binding.llNormalPayment.setOnClickListener {
            NormalPaymentDialogBoxFragment(selectOrNot = {

            }).show(parentFragmentManager, "Normal Payment")
            normalPayment = true
            updateRadioButtonState(normalPayment = true)
        }

        binding.llStanderPayment.setOnClickListener {
            StandardPaymentFragment(selectOrNot = {

            }).show(parentFragmentManager, "Standard Payment")
            normalPayment = false
            updateRadioButtonState(normalPayment = false)
        }
    }

    private fun setupTextWatchers() {
        with(binding) {
            upiid.addTextChangedListener(createTextWatcher("[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}", "Invalid UPI ID", upiid))
            etBeneficiaryPan.addTextChangedListener(createTextWatcher("[A-Z]{5}[0-9]{4}[A-Z]{1}", "Invalid PAN", etBeneficiaryPan))
            tenantpanupi.addTextChangedListener(createTextWatcher("[A-Z]{5}[0-9]{4}[A-Z]{1}", "Invalid PAN", tenantpanupi))
        }
    }

    private fun createTextWatcher(regex: String, errorMsg: String, textView: TextView) = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setErrorMessage(textView, if (s.toString().matches(regex.toRegex())) null else errorMsg)
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun setErrorMessage(textView: TextView, message: String?) {
        textView.error = message
    }

    private fun setupFileChooser() {
        val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    binding.tvFileName.text = uri.getName(requireContext()) ?: "No File Chosen"
                }
            }
        }

        binding.tvChooseFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/pdf"
            }
            galleryResult.launch(intent)
        }
    }

    private fun setupPaymentButtonClickListener() {
        binding.tvSavePayment.setOnClickListener {
            if(normalPayment==true){
                validateAndSubmitPaymentDetails(true)
            }else{
                validateAndSubmitPaymentDetails(false)
            }

        }
    }

    private fun validateAndSubmitPaymentDetails(normalPayment : Boolean) {
        val landlordInfo = landlordNameAndNumber()

        if (!validateLandlordDetails(landlordInfo)) return

        val upiId = binding.upiid.text.toString()
        val address = binding.address.text.toString()
        val amount = binding.rentamountupi.text.toString()
        val beneficiaryPan = binding.etBeneficiaryPan.text.toString()
        val tenantPan = binding.tenantpanupi.text.toString()
        val rentAmount = amount.toDoubleOrNull() ?: 0.0
        val userNumber = MySharedPreferences(requireContext()).getUserNumber()

        if (!validateLandlordCompareUserNumber(landlordInfo.landlordNumber.toString(),userNumber.toString(),"Landlord number and Tenant number can't be same")||
            !validateField(upiId, "Please enter UPI ID") ||
            !validateField(binding.tvBhk.text.toString(), "Please enter BHK type") ||
            !validateField(address, "Please enter property address") ||
            !validateField(amount, "Please enter rent amount") ||
            !validateField(beneficiaryPan, "Please enter beneficiary's PAN") ||
            !validateTenantPan(rentAmount, tenantPan)||
            !validateComparePan(beneficiaryPan,tenantPan,"Beneficiary's PAN and Tenant's PAN cannot be same")

        ) return


        submitPaymentDetails(
            payToken = payToken,
            address = address,
            amount = amount,
            tenantPan = tenantPan,
            beneficiarPan = beneficiaryPan,
            landlordName = landlordInfo.landlordName!!,
            landlordNo = landlordInfo.landlordNumber!!,
            normalPayment = normalPayment
        )
    }

    private fun validateField(field: String, errorMessage: String): Boolean {
        return if (field.isEmpty()) {
            showError(errorMessage)
            false
        } else {
            true
        }
    }

    private fun validateTenantPan(amount: Double, tenantPan: String): Boolean {
        return if (amount > 50000 && tenantPan.isEmpty()) {
            showError("Tenant's PAN is required if rent is more than ₹50,000")
            false
        }
//        else if (tenantPan.isEmpty()) {
//            showError("Please enter tenant's PAN")
//            false
//        }
        else {
            true
        }
    }

    private fun validateLandlordDetails(landlordInfo: landLoadNameAndNumber): Boolean {
        return when {
            landlordInfo.landlordName.isNullOrEmpty() -> {
                showError("Please enter landlord name")
                false
            }
            landlordInfo.landlordNumber.isNullOrEmpty() -> {
                showError("Please enter landlord number")
                false
            }
            else -> true
        }
    }

    private fun submitPaymentDetails(
        payToken: String,
        address: String,
        amount: String,  // Input as a String
        tenantPan: String,
        beneficiarPan: String,
        landlordName: String,
        landlordNo: String,
        normalPayment: Boolean  // I assume this flag is part of the class
    ) {
        var newAmount: String

        // Convert amount String to BigDecimal for precise arithmetic
        val amountDecimal = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO

        if (normalPayment) {
            // Calculate 1% of the amount
            val onePercent = amountDecimal.multiply(BigDecimal("0.01"))
            // Add 1% to the original amount
            newAmount = amountDecimal.add(onePercent).toInt().toString()
        } else {
            // Calculate 2.5% of the amount
            val twoAndHalfPercent = amountDecimal.multiply(BigDecimal("0.025"))
            // Add 2.5% to the original amount
            newAmount = amountDecimal.add(twoAndHalfPercent).toInt().toString()
        }

        amountGlobal = newAmount
        // Submit the payment details with the updated amount
        viewModel.addPaymentDetails(
            PaymentDetailsRequest(
                acc_num = "",
                agr_img_url = "",
                beneficiar_pan = beneficiarPan,
                ifsc = "",
                landlord_name = landlordName,
                landlord_phone = landlordNo,
                pay_mode = "upi",
                pay_token = payToken,
                property_address = address,
                rent_amt = newAmount,
                tenant_pan = tenantPan,
                upi_id = binding.upiid.text.toString(),
                bhk_type = ""
            )
        )
    }
    private fun observePaymentDetails() {
        viewModel.addPaymentDetailsLiveData.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                startRazorPaymentActivity()
            } else {
                showError(response.msg)
            }
        }
    }

    private fun startRazorPaymentActivity() {
        Toast.makeText(requireContext(), "Mock Payment Successful", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), com.btjnonbrokerage.Activity.MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateRadioButtonState(normalPayment: Boolean) {
        binding.apply {
            ivNormalPayment.setImageResource(if (normalPayment) R.drawable.radioselected else R.drawable.radiounselected)
            ivStanderPayment.setImageResource(if (normalPayment) R.drawable.radiounselected else R.drawable.radioselected)
        }
    }

    private fun validateComparePan(beneficiaryPan: String, tenantPan: String, errorMessage: String):Boolean{
        val formattedBeneficiaryPan = beneficiaryPan.trim().uppercase()
        val formattedTenantPan = tenantPan.trim().uppercase()

        return if (formattedBeneficiaryPan == formattedTenantPan) {
            showError(errorMessage)
            false
        } else {
            true
        }

    }
    private fun validateLandlordCompareUserNumber(landlordNumber:String,userNumber:String,errorMessage: String):Boolean{

        return if (landlordNumber == userNumber){
            showError(errorMessage)
            false
        }else{
            true
        }

    }
}
