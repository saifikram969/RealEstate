package com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify

data class PaymentNumberVerifyResponse(
    val msg: String,
    val pay_token: String,
    val status: String,
    val uid: String
)