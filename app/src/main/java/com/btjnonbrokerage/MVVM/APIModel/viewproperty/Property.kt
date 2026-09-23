package com.btjnonbrokerage.MVVM.APIModel.viewproperty

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    val avg_rating : Float,
    val address: String,
    val bathroom: String,
    val bedroom: String,
    val category: String,
    val city: String,
    val date: String,
    val env_facilities: List<String>,
    val featured: String,
    val id: String,
    val lats: String,
    val list_type: String,
    val longs: String,
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
    val wishlist: Int,
    val city_name : String,
    val state_name : String,
    val plan_type : String

): Parcelable