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

    private val orderID = "YOUR_ORDER_ID"  // Backend से लेना होगा
    private val paymentSessionID = "YOUR_PAYMENT_SESSION_ID"  // Backend से लेना होगा
    private val cfEnvironment = CFSession.Environment.SANDBOX  // या PRODUCTION
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cash_free_payment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Cashfree Payment Callback सेट करें
        try {
            CFPaymentGatewayService.getInstance().setCheckoutCallback(this)
            startWebCheckoutPayment() // Payment शुरू करें
        } catch (e: CFException) {
            e.printStackTrace()
            
        }
    }

    // ✅  Step 1: Web Checkout शुरू करें
    private fun startWebCheckoutPayment() {
        try {
            // Session बनाएं
            val cfSession = CFSession.CFSessionBuilder()
                .setEnvironment(cfEnvironment)
                .setPaymentSessionID(paymentSessionID)
                .setOrderId(orderID)
                .build()

            // Theme Customization (Optional)
            val cfTheme = CFWebCheckoutThemeBuilder()
                .setNavigationBarBackgroundColor("#000000") // Navbar का रंग
                .setNavigationBarTextColor("#FFFFFF")  // Navbar text color
                .build()

            // Web Checkout Payment Object बनाएं
            val cfWebCheckoutPayment = CFWebCheckoutPaymentBuilder()
                .setSession(cfSession)
                .setCFWebCheckoutUITheme(cfTheme)
                .build()

            // ✅ Step 2: Payment Screen खोलें
            CFPaymentGatewayService.getInstance().doPayment(this, cfWebCheckoutPayment)
        } catch (exception: CFException) {
            exception.printStackTrace()
        }
    }

    // ✅ Step 3: Payment Callback Handle करें
    override fun onPaymentVerify(orderID: String) {
        Log.d("onPaymentVerify", "Payment verification required for Order ID: $orderID")
        // Backend पर जाकर payment status check करें
    }

    override fun onPaymentFailure(cfErrorResponse: CFErrorResponse, orderID: String) {
        Log.e("onPaymentFailure", "Payment failed for Order ID: $orderID. Error: ${cfErrorResponse.message}")
    }
}