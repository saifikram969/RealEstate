package com.btjnonbrokeragee.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.PaymentAPIModel.PaymentStatusRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.R
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class RazorPaymentActivity : AppCompatActivity(), PaymentResultListener {

    @Inject
    lateinit var viewModel: PaymentViewModel
    var payTokenActivity = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_razor_payment)

        val amount = intent.getStringExtra("amount")
        val payToken = intent.getStringExtra("payToken")

        if (amount != null && payToken != null) {
            payTokenActivity = payToken
            razorPaymentGateway(amount)
        }

        paymentStatusObserver()

    }

    private fun razorPaymentGateway(amount: String) {
        val checkout = Checkout()
        val userNumber = MySharedPreferences(this).getUserNumber()
        checkout.setKeyID("rzp_live_oTt9IZStLVWvRD")  // Set Razorpay Key ID

        if (amount.isNotEmpty()) {
            val amountInRupees = amount.toDoubleOrNull()
            if (amountInRupees != null) {
                val amountInPaise = (amountInRupees * 100).toInt()

                val paymentObject = JSONObject()
                try {
                    paymentObject.put("name", "Non brokerage")
                    paymentObject.put("description", "Pay Rent Payment")
                    paymentObject.put("theme.color", "#3399cc")
                    paymentObject.put("currency", "INR")
                    paymentObject.put("amount", amountInPaise)  // Amount is in Paise
                    paymentObject.put("prefill.contact", userNumber)

                    // Pass the correct context: requireActivity()
                    checkout.open(this, paymentObject)
                } catch (e: JSONException) {
                    Log.e("razorPaymentGateway", e.toString())
                }
            } else {
                Log.e("razorPaymentGateway", "Invalid amount format")
            }
        } else {
            Log.e("razorPaymentGateway", "Amount is empty")
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        if (razorpayPaymentID != null) {
            paymentStatusApiCall(razorpayPaymentID, "success", "", "")
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
        } else {
            // Handle null case if razorpayPaymentID is null
            Log.e("Payment", "Payment ID is null")
            Toast.makeText(
                this,
                "Payment Successful, but no Payment ID",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        val errorResponse = response ?: "No response from Razorpay"
        paymentStatusApiCall("", "failed", code.toString(), errorResponse)
        Toast.makeText(this, "Payment Failed: $errorResponse", Toast.LENGTH_SHORT)
            .show()
    }

    private fun paymentStatusApiCall(
        paymentId: String,
        paymentStatus: String,
        code: String,
        response: String
    ) {
        viewModel.paymentStatus(
            PaymentStatusRequest(
                payTokenActivity,
                paymentId,
                paymentStatus,
                code,
                response
            )
        )
    }

    private fun paymentStatusObserver() {
        viewModel.paymentStatusLiveData.observe(this) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

        }
    }
}