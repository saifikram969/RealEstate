package com.btjnonbrokerage.MVVM.APIModel.Profile

import com.google.gson.annotations.SerializedName

data class GetProfileResponse(
    val msg: String,
    val status: String,
    val user: User,
    @SerializedName("property_count" ) var propertyCount : Int?    = null,
    @SerializedName("review_count"   ) var reviewCount   : Int?    = null

)
data class User(
    val auth_token: String,
    val city: Any,
    val country: Any,
    val datentime: String,
    val deviceid: Any,
    val email: String,
    val emailverified: String,
    val flat: Any,
    val fullname: String,
    val id: String,
    val locality: Any,
    val mobile: Any,
    val mobileverified: String,
    val password: String,
    val phone_code: String,
    val profileimg: String,
    val state: Any,
    val status: String,
    val street: Any,
    val usertype: String,
    val zipcode: Any
)