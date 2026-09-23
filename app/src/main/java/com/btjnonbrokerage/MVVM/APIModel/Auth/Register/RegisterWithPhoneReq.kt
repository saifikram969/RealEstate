package com.btjnonbrokerage.MVVM.APIModel.Auth.Register

data class RegisterWithPhoneReq(
    val address: String,
    val email: String,
    val fullname: String,
    val mobileNumber: String,
    val phone_code: String,
    val profileimg: String,
    val shop_image: String,
    val shop_name: String,
    val usertype: String
)