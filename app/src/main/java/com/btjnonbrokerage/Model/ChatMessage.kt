package com.btjnonbrokerage.Model

import android.graphics.Bitmap

data class ChatMessage(
    val id : String,
    val message: String,
    val senderId : String,
    val imageBitmap: Bitmap? = null
)
