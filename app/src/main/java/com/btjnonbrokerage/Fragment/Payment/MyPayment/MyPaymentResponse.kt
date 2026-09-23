package com.btjnonbrokerage.Fragment.Payment.MyPayment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class MyPaymentResponse(
    val msg: String,
    val payment_history: List<PaymentHistory>,
    val status: String
)
@Parcelize
data class PaymentHistory(
    val acc_num: String?,
    val agr_img_url: String?,
    val beneficiar_pan: String?,
    val bhk_type: String?,
    val code: String?,
    val country_code: String?,
    val date: String?,
    val email: String?,
    val id: String?,
    val ifsc: String?,
    val landlord_name: String?,
    val landlord_phone: String?,
    val name: String?,
    val pay_mode: String?,
    val pay_status: String?,
    val pay_token: String?,
    val pay_type: String?,
    val payment_id: String?,
    val phone: String?,
    val property_address: String?,
    val rent_amt: String?,
    val response: String?,
    val status: String?,
    val tenant_pan: String?,
    val time: String?,
    val trash: String?,
    val uid: String?,
    val upi_id: String?
) : Parcelable