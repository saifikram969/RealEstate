package com.btjnonbrokerage.MVVM.APIModel.PayRentDetails

data class PayRentDetailsRequest(
    val pay_token:String,
    val landlord_name:String,
    val landlord_phone:String,
    val pay_mode:String,
    val acc_num:String,
    val ifsc:String,
    val upi_id:String,
    val property_address:String,
    val rent_amt:String,
    val beneficiar_pan:String,
    val tenant_pan:String,
    val agr_img_url:String,


    )
