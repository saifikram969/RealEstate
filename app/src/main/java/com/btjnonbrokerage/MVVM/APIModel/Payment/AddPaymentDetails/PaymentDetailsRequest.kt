package com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails

data class PaymentDetailsRequest(
    val acc_num: String,
    val agr_img_url: String,
    val beneficiar_pan: String,
    val ifsc: String,
    val landlord_name: String,
    val landlord_phone: String,
    val pay_mode: String,
    val pay_token: String,
    val property_address: String,
    val rent_amt: String,
    val tenant_pan: String,
    val upi_id: String,
    val bhk_type: String
)