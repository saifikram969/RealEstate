package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener
import com.btjnonbrokerage.Base.HashGenerationUtils
import com.btjnonbrokerage.databinding.FragmentPayUBinding

class PayUFragment : Fragment() {

    private val surl = "https://cbjs.payu.in/sdk/success" // Your success URL
    private val furl = "https://cbjs.payu.in/sdk/failure" // Your failure URL
    private val key = "Xn7ZbM" // Replace with actual key
    private val salt = "aUsxX0YBLDSnTBqB9sr3K6DUB9XlsgVh" // Replace with actual salt

    private lateinit var binding: FragmentPayUBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayUBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentSetup()
    }

    private fun paymentSetup() {
        val additionalParamsMap: HashMap<String, Any?> = HashMap()
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF1] = "udf1"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF2] = "udf2"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF3] = "udf3"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF4] = "udf4"
        additionalParamsMap[PayUCheckoutProConstants.CP_UDF5] = "udf5"
        additionalParamsMap[PayUCheckoutProConstants.SODEXO_SOURCE_ID] = "srcid123"

        val payUPaymentParams = PayUPaymentParams.Builder()
            .setAmount("1.0") // Ensure amount is in the correct format
            .setIsProduction(true)
            .setKey(key)
            .setProductInfo("Macbook Pro")
            .setPhone("8888888888")
            .setTransactionId(System.currentTimeMillis().toString())
            .setFirstName("John")
            .setEmail("john@yopmail.com")
            .setSurl(surl)
            .setFurl(furl)
            .build()

        PayUCheckoutPro.open(
            requireActivity(), payUPaymentParams,
            object : PayUCheckoutProListener {

                override fun onPaymentSuccess(response: Any) {
                    val responseMap = response as? HashMap<*, *>
                    val payUResponse =
                        responseMap?.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE) as? String
                    val merchantResponse =
                        responseMap?.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE) as? String
                    Log.d(
                        "PayU",
                        "Payment Success: PayU Response: $payUResponse, Merchant Response: $merchantResponse"
                    )
                    Toast.makeText(requireContext(), "Payment Success", Toast.LENGTH_SHORT).show()
                }

                override fun onPaymentFailure(response: Any) {
                    val responseMap = response as? HashMap<*, *>
                    val payUResponse =
                        responseMap?.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE) as? String
                    val merchantResponse =
                        responseMap?.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE) as? String
                    Log.d(
                        "PayU",
                        "Payment Failure: PayU Response: $payUResponse, Merchant Response: $merchantResponse"
                    )
                    Toast.makeText(requireContext(), "Payment Failure", Toast.LENGTH_SHORT).show()
                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    if (isTxnInitiated) {
                        // Optionally, you can initiate a verify transaction request here
                    } else {
                        Toast.makeText(requireContext(), "Payment Cancelled", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onError(errorResponse: ErrorResponse) {
                    val errorMessage = errorResponse.errorMessage ?: "Unknown error occurred"
                    Log.e("PayU", "Error: $errorMessage")
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                    // Customize WebView properties if needed
                }

                override fun generateHash(
                    valueMap: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    val hashData = valueMap[PayUCheckoutProConstants.CP_HASH_STRING] as? String
                    val hashName = valueMap[PayUCheckoutProConstants.CP_HASH_NAME] as? String

                    Log.d("PayU", "Hash Data: $hashData")
                    Log.d("PayU", "Hash Name: $hashName")

                    if (!hashData.isNullOrEmpty() && !hashName.isNullOrEmpty()) {
                        val hash = HashGenerationUtils.generateV2HashFromSDK(
                            hashData,
                            salt // Use your actual salt here
                        )
                        Log.d("PayU", "Generated Hash: $hash")
                        if (!hash.isNullOrEmpty()) {
                            val dataMap: HashMap<String, String?> = HashMap()
                            dataMap[hashName] = hash
                            hashGenerationListener.onHashGenerated(dataMap)
                        } else {
                            Log.e("PayU", "Hash generation failed")
                        }
                    } else {
                        Log.e("PayU", "Invalid hash data or name")
                    }
                }

            })
    }
}