package com.btjnonbrokerage.Model.ChatFirebase

data class FireBaseUserModel(
    val name:String,
    var image:String? = null,
    var email:String? = null,
    var token:String? = null,
    val id:String
)