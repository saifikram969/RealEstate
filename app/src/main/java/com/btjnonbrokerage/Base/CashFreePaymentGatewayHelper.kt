package com.btjnonbrokerage.Base

import android.content.Context
import android.util.Log
import com.cashfree.pg.api.CFPaymentGatewayService
import com.cashfree.pg.core.api.CFSession
import com.cashfree.pg.core.api.CFTheme
import com.cashfree.pg.core.api.exception.CFException
import com.cashfree.pg.ui.api.CFDropCheckoutPayment
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class CashFreePaymentGatewayHelper(private val context: Context) {

    fun cashFreeCreateBankOrder(userId: String, userName: String, userEmail: String, userMobileNumber: String, amount: Double, bankAccountDetails: JsonObject) {
        val requestBody = generateBankPayload(userId, userName, userEmail, userMobileNumber, amount, bankAccountDetails)

        RetrofitInstance.apiService.cashFreeCreateBankOrder(requestBody).enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>, response: Response<JsonObject?>) {
                if (response.isSuccessful) {
                    val data = response.body()?.asJsonObject
                    val orderId = data?.get("order_id")?.asString ?: ""
                    val paymentSessionId = data?.get("payment_session_id")?.asString ?: ""

                    if (orderId.isNotBlank() && paymentSessionId.isNotBlank()) {
                        doPayment(paymentSessionId, orderId)
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("CashFreePayment", "Error: $errorMessage")
                    // Show user-friendly error message
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable) {
                Log.e("CashFreePayment", "Error: ${t.message}")
                // Show user-friendly error message
            }
        })
    }

    fun doPayment(paymentSessionId: String, orderId: String) {
        try {
            val cfSession: CFSession = CFSession.CFSessionBuilder()
                .setEnvironment(CFSession.Environment.SANDBOX)
                .setPaymentSessionID(paymentSessionId)
                .setOrderId(orderId)
                .build()

            val cfTheme = CFTheme.CFThemeBuilder()
                .setNavigationBarBackgroundColor("#73c700")
                .setNavigationBarTextColor("#ffffff")
                .setButtonBackgroundColor("#73c700")
                .setButtonTextColor("#FFFFFFFF")
                .setPrimaryTextColor("#73c700")
                .setSecondaryTextColor("#73c700")
                .build()

            val cfDropCheckoutPayment = CFDropCheckoutPayment.CFDropCheckoutPaymentBuilder()
                .setSession(cfSession)
                .setCFNativeCheckoutUITheme(cfTheme)
                .build()

            CFPaymentGatewayService.getInstance().doPayment(context, cfDropCheckoutPayment)
        } catch (exception: CFException) {
            Log.e("CashFreePayment", "Payment Error: ${exception.message}")
            // Show user-friendly error message
        }
    }

    private fun generateBankPayload(userId: String, userName: String, userEmail: String, userMobileNumber: String, amount: Double, bankAccountDetails: JsonObject): JsonObject {
        val orderMeta = JsonObject()
        val customerDetail = JsonObject().apply {
            addProperty("customer_id", userId)
            addProperty("customer_name", userName)
            addProperty("customer_email", userEmail)
            addProperty("customer_phone", userMobileNumber)
        }
        return JsonObject().apply {
            addProperty("order_amount", amount)
            addProperty("order_currency", "INR")
            add("customer_details", customerDetail)
            add("order_meta", orderMeta)
            add("bank_account_details", bankAccountDetails)  // Include bank account details
        }
    }

    interface ApiServices {
        @POST("orders")
        @Headers(
            "X-Client-Secret: 4d0b84759830a89376cdea63bc4a71296667caf0", "X-Client-Id: 138814f6cdcda1c974525953d2418831", "x-api-version: 2023-08-01", "Content-Type: application/json", "Accept: application/json"
        )
        fun cashFreeCreateBankOrder(@Body requestBody: JsonObject): Call<JsonObject>
    }

    object RetrofitInstance {
        private const val BASE_URL = "https://sandbox.cashfree.com/pg/"

        private val loggingInterceptor: HttpLoggingInterceptor by lazy {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder().addNetworkInterceptor(loggingInterceptor).build()
        }

        private val retrofitInstance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }

        val apiService: ApiServices by lazy { retrofitInstance.create(ApiServices::class.java) }
    }
}
