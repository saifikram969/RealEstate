package com.btjnonbrokerage.MVVM.APIModel.NearEstate

data class NearEstateResponse(
    val msg: String,
    val status: String,
    val user_estates_property: List<UserEstatesProperty>
)