package com.btjnonbrokerage.MVVM.APIModel.Locations.State

data class StateLocationsResponse(
    val msg: String,
    val states: List<StateList>,
    val status: String
)

data class  StateList(
    val country_id: String,
    val id: String,
    val name: String
)