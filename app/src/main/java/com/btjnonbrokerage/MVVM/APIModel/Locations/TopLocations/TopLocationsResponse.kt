package com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations

data class TopLocationsResponse(
    val msg: String,
    val status: String,
    val top_states: List<TopState>
)
data class StateData(
    val country_id: String,
    val id: String,
    val img_url: String,
    val name: String
)
data class TopState(
    val property_count: String,
    val state_data: StateData,
    val state_id: String
)