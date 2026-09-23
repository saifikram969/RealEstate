package com.btjnonbrokerage.MVVM.APIModel.CreateOrder

data class CreateOrderRequest(
    val email: String,
    val name: String,
    val order_amount: String,
    val pay_token: String,
    val phone: String,
    val uid: String
)