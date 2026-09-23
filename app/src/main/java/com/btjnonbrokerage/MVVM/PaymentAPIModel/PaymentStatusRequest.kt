package com.btjnonbrokerage.MVVM.PaymentAPIModel

data class PaymentStatusRequest(
    val pay_token: String,
    val payment_id: String,
    val payment_status: String,
    val code : String,
    val response : String

)