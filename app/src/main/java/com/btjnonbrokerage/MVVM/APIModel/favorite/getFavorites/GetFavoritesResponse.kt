package com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites

data class GetFavoritesResponse(
    val msg: String,
    val properties: List<Property>,
    val status: String
)

data class Property(
    val address: String,
    val balcony: String,
    val bathroom: String,
    val bedroom: String,
    val category: String,
    val city: String,
    val date: String,
    val env_facilities: List<String>,
    val facing: String,
    val featured: Any,
    val id: String,
    val lats: String,
    val list_type: String,
    val longs: String,
    val phone_code: String,
    val phone_number: String,
    val pincode: String,
    val pr_name: String,
    val price: String,
    val prop_desc: String,
    val property_id: String,
    val property_images: List<String>,
    val state: String,
    val status: String,
    val time: String,
    val total_rooms: String,
    val trash: String,
    val uid: String,
    val city_name: String,
    val state_name: String,
    val avg_rating :Float,
    val plan_type :String
)