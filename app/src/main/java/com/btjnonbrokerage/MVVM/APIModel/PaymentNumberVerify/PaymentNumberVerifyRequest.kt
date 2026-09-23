package com.btjnonbrokerage.MVVM.APIModel.PaymentNumberVerify

data class PaymentNumberVerifyRequest(
    val uid: String,
    val pay_type: String,
    val name: String,
    val country_code: String,
    val phone: String
)
