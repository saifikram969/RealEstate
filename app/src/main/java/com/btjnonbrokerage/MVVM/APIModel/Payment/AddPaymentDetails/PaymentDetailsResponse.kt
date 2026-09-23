package com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails

data class PaymentDetailsResponse(
    val msg: String,
    val pay_token: String,
    val status: String
)