package com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify

data class PaymentNumberVerifyRequest(
    val country_code: String,
    val name: String,
    val pay_type: String,
    val phone: String,
    val uid: String
)