package com.btjnonbrokerage.MVVM.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
import com.btjnonbrokerage.MVVM.Repository.PaymentRepo
import javax.inject.Inject

class PaymentViewModel  @Inject constructor(private val repo: PaymentRepo) : ViewModel(){

    //  Payment Number Verify
    val paymentNumberVerifyLiveData   : LiveData<PaymentNumberVerifyResponse>
        get() = repo.paymentNumberVerifyLiveData

    fun paymentNumberVerify(body : PaymentNumberVerifyRequest){
        repo.paymentNumberVerifyFun(body)
    }


    // add Payment Details
    val addPaymentDetailsLiveData   : LiveData<PaymentDetailsResponse>
        get() = repo.addPaymentDetailsLiveData
    fun addPaymentDetails(body : PaymentDetailsRequest){
        repo.addPaymentDetailsFun(body)
    }

    val paymentStatusLiveData   : LiveData<PaymentStatusResponse>
    get() = repo.paymentStatusLiveData
    fun paymentStatus(body : PaymentStatusRequest){
        repo.paymentStatusFun(body)
    }

    val myPaymentLiveData   : LiveData<MyPaymentResponse>
    get() = repo.myPaymentLiveData
    fun myPayment(body : MyPaymentRequest){
        repo.myPaymentFun(body)
    }




}