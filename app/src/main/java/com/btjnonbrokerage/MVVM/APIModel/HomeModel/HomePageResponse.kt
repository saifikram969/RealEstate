package com.btjnonbrokerage.MVVM.APIModel.HomeModel

data class HomePageResponse(
    val featured_properties: List<FeaturedProperty>,
    val msg: String,
    val status: String,
    val top_location: List<TopLocation>,
    val top_users: List<TopUser>
)

data class FeaturedProperty(
    val address: String,
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
    val id: String,
    val images: List<String>,
    val lats: String,
    val list_type: String,
    val longs: String,
    val name: String,
    val phone_code: String,
    val phone_number: String,
    val pincode: String,
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
    val wishlist: Int,
    val plan_type:String
)
data class StateData(
    val country_id: String,
    val id: String,
    val img_url: String,
    val name: String
)
data class TopLocation(
    val property_count: String,
    val state_data: StateData,
    val state_id: String
)
data class TopUser(
    val name: String,
    val profileimg: String,
    val property_count: String,
    val uid: String
)