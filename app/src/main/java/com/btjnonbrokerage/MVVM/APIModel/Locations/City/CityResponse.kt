package com.btjnonbrokerage.MVVM.APIModel.Locations.City

data class CityResponse(
    val cities: List<CityList>,
    val msg: String,
    val status: String
)
data class CityList(
    val city: String,
    val id: String,
    val state_id: String
)