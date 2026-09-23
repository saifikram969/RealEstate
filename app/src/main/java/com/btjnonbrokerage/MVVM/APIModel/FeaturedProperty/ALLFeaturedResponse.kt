package com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty

data class ALLFeaturedResponse(
    val featured_property: FeaturedProperty,
    val msg: String,
    val status: String
)
data class FeaturedProperty(
    val properties: List<Property>
)
data class Property(
    val address: String,
    val address2: String,
    val avg_rating: Float,
    val balcony: String,
    val bathroom: String,
    val bedroom: String,
    val category: String,
    val city: String,
    val city_name: String,
    val date: String,
    val facilities: List<String>,
    val facing: String,
    val featured: String,
    val hall: String,
    val id: String,
    val images: List<String>,
    val kitchen: String,
    val lats: String,
    val list_type: String,
    val longs: String,
    val phone_code: String,
    val phone_number: String,
    val pincode: String,
    val plan_type: String,
    val pr_name: String,
    val price: String,
    val prop_desc: String,
    val property_id: String,
    val rating_count: String,
    val state: String,
    val state_name: String,
    val status: String,
    val time: String,
    val total_rooms: String,
    val trash: String,
    val uid: String,
    val wishlist: Int
)
