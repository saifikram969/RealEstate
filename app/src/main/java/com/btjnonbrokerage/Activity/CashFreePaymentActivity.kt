package com.btjnonbrokerage.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.callback.CFCheckoutResponseCallback
import com.cashfree.pg.core.api.utils.CFErrorResponse
import com.btjnonbrokerage.R
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutPayment.CFWebCheckoutPaymentBuilder
import com.cashfree.pg.core.api.webcheckout.CFWebCheckoutTheme.CFWebCheckoutThemeBuilder

class CashFreePaymentActivity : AppCompatActivity(),CFCheckoutResponseCallback {

    private val orderID = "YOUR_ORDER_ID"  // Needs to be fetched from Backend
    private val paymentSessionID = "YOUR_PAYMENT_SESSION_ID"  // Needs to be fetched from Backend
    private val cfEnvironment = CFSession.Environment.SANDBOX  // or PRODUCTION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cash_free_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Set Cashfree Payment Callback
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            startWebCheckoutPayment() // Start Payment
        } catch (e: CFException) {
            e.printStackTrace()
            
        }
    }

    // ✅  Step 1: Start Web Checkout
    private fun startWebCheckoutPayment() {
        try {
            // Create Session
            val cfSession = CFSession.CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()

            // Theme Customization (Optional)
            val cfTheme = CFWebCheckoutThemeBuilder()
                .setNavigationBarBackgroundColor("#000000") // Navbar color
                .setNavigationBarTextColor("#FFFFFF")  // Navbar text color
                .build()

            // Create Web Checkout Payment Object
            val cfWebCheckoutPayment = CFWebCheckoutPaymentBuilder()
                .setSession(cfSession)
                .setCFWebCheckoutUITheme(cfTheme)
                .build()

            // ✅ Step 2: Open Payment Screen
            CFPaymentGatewayService.getInstance().doPayment(this, cfWebCheckoutPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    // ✅ Step 3: Handle Payment Callback
    override fun onPaymentVerify(orderID: String) {
        Log.d("onPaymentVerify", "Payment verification required for Order ID: $orderID")
        // Check payment status on Backend
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e("onPaymentFailure", "Payment failed for Order ID: $orderID. Error: ${cfErrorResponse.message}")
    }
}