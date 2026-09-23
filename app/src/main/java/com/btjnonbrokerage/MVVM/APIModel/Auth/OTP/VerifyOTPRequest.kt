package com.btjnonbrokerage.MVVM.APIModel.Auth.OTP

data class VerifyOTPRequest(
    val otp: String,
    val uid: String
)