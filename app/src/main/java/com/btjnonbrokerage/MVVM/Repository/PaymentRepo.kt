package com.btjnonbrokerage.MVVM.Repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.btjnonbrokerage.Fragment.Payment.MyPayment.MyPaymentRequest
import com.btjnonbrokerage.Fragment.Payment.MyPayment.MyPaymentResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP.VerifyPaymentNumberOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP.VerifyPaymentNumberOTPResponse
import com.btjnonbrokerage.MVVM.PaymentAPIModel.PaymentStatusRequest
import com.btjnonbrokerage.MVVM.PaymentAPIModel.PaymentStatusResponse
import com.btjnonbrokerage.MVVM.RequestBuilder.PaymentService
import com.btjnonbrokerage.MVVM.RequestBuilder.RequestBuilder
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PaymentRepo @Inject constructor(@ActivityContext val context: Context) {

    private val request: PaymentService = RequestBuilder.retrofit.create(PaymentService::class.java)


    //Payment Number Verify
    private val _paymentNumberVerifyResponse = MutableLiveData<PaymentNumberVerifyResponse>()
    val paymentNumberVerifyLiveData: MutableLiveData<PaymentNumberVerifyResponse>
        get() = _paymentNumberVerifyResponse

    fun paymentNumberVerifyFun(body: PaymentNumberVerifyRequest) {
        val result = request.paymentNumberVerifyService(body)
        result.enqueue(object : Callback<PaymentNumberVerifyResponse?> {
            override fun onResponse(
                call: Call<PaymentNumberVerifyResponse?>,
                response: Response<PaymentNumberVerifyResponse?>
            ) {
                if (response.code() == 200) {
                    _paymentNumberVerifyResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<PaymentNumberVerifyResponse?>, t: Throwable) {

            }
        })
    }

    //Payment Number Verify OTP
    private val _verifyPaymentNumberOTPResponse = MutableLiveData<VerifyPaymentNumberOTPResponse>()
    val verifyPaymentNumberOTPLiveData: MutableLiveData<VerifyPaymentNumberOTPResponse>
        get() = _verifyPaymentNumberOTPResponse


    //addPaymentDetails
    private val _addPaymentDetailsResponse = MutableLiveData<PaymentDetailsResponse>()
    val addPaymentDetailsLiveData: MutableLiveData<PaymentDetailsResponse>
        get() = _addPaymentDetailsResponse

    fun addPaymentDetailsFun(body: PaymentDetailsRequest) {
        val result = request.addPaymentDetails(body)
        result.enqueue(object : Callback<PaymentDetailsResponse?> {
            override fun onResponse(
                call: Call<PaymentDetailsResponse?>,
                response: Response<PaymentDetailsResponse?>
            ) {
                if (response.code() == 200) {
                    _addPaymentDetailsResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<PaymentDetailsResponse?>, t: Throwable) {

            }
        })
    }


    private val _paymentStatusResponse = MutableLiveData<PaymentStatusResponse>()
    val paymentStatusLiveData: MutableLiveData<PaymentStatusResponse>
        get() = _paymentStatusResponse

    fun paymentStatusFun(body: PaymentStatusRequest) {
        val result = request.paymentStatusService(body)
        result.enqueue(object : Callback<PaymentStatusResponse?> {
            override fun onResponse(
                call: Call<PaymentStatusResponse?>,
                response: Response<PaymentStatusResponse?>
            ) {
                if (response.code() == 200){
                    _paymentStatusResponse.postValue(response.body())
                }


            }

            override fun onFailure(call: Call<PaymentStatusResponse?>, t: Throwable) {
              Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _myPaymentResponse = MutableLiveData<MyPaymentResponse>()
    val myPaymentLiveData: MutableLiveData<MyPaymentResponse>
        get() = _myPaymentResponse

    fun myPaymentFun(body: MyPaymentRequest) {
        val result = request.myPaymentService(body)
        result.enqueue(object : Callback<MyPaymentResponse?> {
            override fun onResponse(
                call: Call<MyPaymentResponse?>,
                response: Response<MyPaymentResponse?>
            ) {
               _myPaymentResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<MyPaymentResponse?>, t: Throwable) {
               Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }
        })

    }


}