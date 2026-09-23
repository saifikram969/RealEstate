package com.btjnonbrokerage.MVVM.APIModel.review.AddReview

data class AddReviewRequest(
    val comment: String,
    val property_id: String,
    val rating: String,
    val uid: String
)