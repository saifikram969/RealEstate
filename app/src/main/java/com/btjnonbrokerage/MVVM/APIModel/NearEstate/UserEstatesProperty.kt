package com.btjnonbrokerage.MVVM.APIModel.NearEstate

data class UserEstatesProperty(
    val properties: List<Property>,
    val remaining_properties: List<RemainingProperty>
)