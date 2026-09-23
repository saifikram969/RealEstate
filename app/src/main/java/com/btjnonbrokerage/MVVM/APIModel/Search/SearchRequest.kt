package com.btjnonbrokerage.MVVM.APIModel.Search

data class SearchRequest(
    val category: String,
    val search_input: String,
    val page: String,
    val uid: String,
    val min_price:String,
    val max_price:String
)