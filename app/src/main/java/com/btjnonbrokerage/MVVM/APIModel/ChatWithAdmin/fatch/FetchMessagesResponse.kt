package com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch

data class FetchMessagesResponse(
    val messages: List<Message>,
    val status: String
)

data class Message(
    val date: String,
    val message: String,
    val time: String,
    val type: Int
)