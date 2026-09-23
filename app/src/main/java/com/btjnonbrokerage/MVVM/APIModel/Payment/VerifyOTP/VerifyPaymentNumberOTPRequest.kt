package com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP

data class VerifyPaymentNumberOTPRequest(
    val otp: String,
    val uid: String
)