package com.btjnonbrokerage.MVVM.APIModel.Auth.OTP

data class VerifyOTPResponse(
    val msg: String,
    val status: String,
    val userdata: Userdata
)
data class Userdata(
    val email: String,
    val icon: String,
    val mobile: String,
    val name: String,
    val uid: String
)