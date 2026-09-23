package com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList

data class AllReviewListResponse(
    val msg: String,
    val ratings: List<Rating>,
    val status: String,
    val total_reviews: String
)

data class Rating(
    val rating_id : String,
    val datetime: String,
    val fullname: String,
    val msg: String,
    val profileimg: String,
    val rating: String,
    val uid: String
)