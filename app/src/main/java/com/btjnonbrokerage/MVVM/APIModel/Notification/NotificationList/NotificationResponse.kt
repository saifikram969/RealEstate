package com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList

data class NotificationResponse(
    val notifications: List<Notification>,
    val status: String,
    val total_count: String
)
data class Notification(
    val date: String,
    val description: String,
    val image: String,
    val payment_id: String,
    val property_id: String,
    val subject: String,
    val time: String,
    val title: String,
    val type: String
)