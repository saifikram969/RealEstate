package com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile

data class UpdateProfileResponse(
    val msg: String,
    val status: String,
    val user: User
)

data class User(
    val fullname: String,
    val mobile: String,
    val profile_image: String,
)