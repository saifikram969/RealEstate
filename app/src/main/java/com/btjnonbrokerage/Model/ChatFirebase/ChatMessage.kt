package com.btjnonbrokerage.Model.ChatFirebase

import java.util.Date

data class ChatMessage(
    val senderId: String = "",
    val receiverId: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val recipientId : String =""
)
