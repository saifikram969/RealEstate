package com.btjnonbrokerage.MVVM.APIModel.PinCode

data class PinCodeResponse(
    val msg: String,
    val pincodes: List<String>,
    val status: String
)