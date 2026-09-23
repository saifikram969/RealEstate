package com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send

data class SendMessagesRequest(
    val message: String,
    val uid: String
)