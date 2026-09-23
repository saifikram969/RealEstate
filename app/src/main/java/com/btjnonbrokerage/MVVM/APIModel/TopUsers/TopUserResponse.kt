package com.btjnonbrokerage.MVVM.APIModel.TopUsers

data class TopUserResponse(
    val msg: String,
    val status: String,
    val top_users: List<TopUser>
)
data class TopUser(
    val fullname: String,
    val profileimg: String,
    val property_count: String,
    val uid: String
)

