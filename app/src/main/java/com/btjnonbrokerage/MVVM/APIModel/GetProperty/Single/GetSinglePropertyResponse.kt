package com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single

data class GetSinglePropertyResponse(
    val msg: String,
    val property_details: PropertyDetails,
    val related_properties: List<RelatedProperty>,
    val status: String,
    val user_details: UserDetails,
    val request_contact_status :String?
) {
    data class PropertyDetails(
        val address: String,
        val address2: String,
        val avg_rating: Double,
        val balcony: String,
        val bathroom: String,
        val bedroom: String,
        val category: String,
        val city: String,
        val city_name: String,
        val date: String,
        val env_facilities: List<String>,
        val facing: String,
        val featured: String,
        val hall: String,
        val id: String,
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
        val property_images: List<String>,
        val reviews: List<Review> = emptyList(),
        val state: String,
        val state_name: String,
        val status: String,
        val time: String,
        val total_rooms: String?, // Or Any if you expect various types
        val trash: String,
        val uid: String,
        val wishlist: Int,
        val avail_from:String,
        val secure_deposit:String,
        val furnished:String,
        val area:String
    ) {
        data class Review(
            val comment: String,
            val fullname: String,
            val profileimg: String,
            val rating: String,
            val review_date: String
        )
    }

    data class RelatedProperty(
        val address: String,
        val address2: String,
        val avg_rating: Double,
        val balcony: String,
        val bathroom: String,
        val bedroom: String,
        val category: String,
        val city: String,
        val city_name: String,
        val date: String,
        val env_facilities: List<String>,
        val facing: String,
        val featured: String,
        val hall: String,
        val id: String,
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
        val property_images: List<String>,
        val state: String,
        val state_name: String,
        val status: String,
        val time: String,
        val total_rooms: String?,
        val trash: String,
        val uid: String,
        val wishlist: Int
    )

    data class UserDetails(
        val auth_token: String,
        val fullname: String,
        val mobile: String,
        val phone_code: String,
        val profileimg: String
    )
}