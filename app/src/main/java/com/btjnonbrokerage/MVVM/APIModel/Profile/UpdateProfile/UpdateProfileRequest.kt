package com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile

data class UpdateProfileRequest(
    val fullname: String,
    val profile_image: String?,
    val uid: String
)