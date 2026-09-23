package com.btjnonbrokerage.MVVM.APIModel.PaymentNumberVerify

data class PaymentNumberVerifyResponse(
    val uid: String,
    val pay_token: String,
    val status: String,
    val msg: String
)
