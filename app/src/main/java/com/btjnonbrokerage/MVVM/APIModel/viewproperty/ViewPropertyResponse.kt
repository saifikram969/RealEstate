package com.btjnonbrokerage.MVVM.APIModel.viewproperty

data class ViewPropertyResponse(
    val msg: String,
    val properties: List<Property>,
    val status: String
)