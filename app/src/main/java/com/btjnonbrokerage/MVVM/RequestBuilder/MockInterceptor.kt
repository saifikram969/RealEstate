package com.btjnonbrokerage.MVVM.RequestBuilder

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import okio.Buffer

class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val jsonString = when {
            url.contains("homepage.php") -> MockDataHelper.HOME_PAGE_RESPONSE
            url.contains("get_user_profile.php") -> MockDataHelper.PROFILE_RESPONSE
            url.contains("get_property.php") -> MockDataHelper.SINGLE_PROPERTY_RESPONSE
            url.contains("view_property.php") -> MockDataHelper.VIEW_PROPERTY_RESPONSE
            url.contains("all_states.php") -> MockDataHelper.STATES_RESPONSE
            url.contains("fetch_city.php") -> MockDataHelper.CITIES_RESPONSE
            url.contains("login_mobile.php") -> MockDataHelper.LOGIN_RESPONSE
            url.contains("signup_mobile.php") -> MockDataHelper.REGISTER_RESPONSE
            url.contains("verify.php") -> MockDataHelper.VERIFY_OTP_RESPONSE
            url.contains("user_estates.php") -> MockDataHelper.NEAR_ESTATE_RESPONSE
            url.contains("add_property.php") -> {
                val body = request.body
                if (body != null) {
                    try {
                        val buffer = Buffer()
                        body.writeTo(buffer)
                        val jsonStr = buffer.readUtf8()
                        val obj = JSONObject(jsonStr)
                        val prName = obj.optString("pr_name", "My Custom Property")
                        val price = obj.optString("price", "10000")
                        val category = obj.optString("category", "House")
                        val address = obj.optString("address", "Custom Address")
                        val pincode = obj.optString("pincode", "123456")
                        
                        val newPropJson = """
                        {
                            "avg_rating": 5.0,
                            "address": "$address",
                            "bathroom": "1",
                            "bedroom": "1",
                            "category": "$category",
                            "city": "1",
                            "date": "2024-01-01",
                            "env_facilities": ["Custom"],
                            "featured": "0",
                            "id": "${System.currentTimeMillis()}",
                            "lats": "19.0760",
                            "list_type": "Sale",
                            "longs": "72.8777",
                            "pr_name": "$prName",
                            "price": "$price",
                            "prop_desc": "Custom user added property",
                            "property_id": "PROP${System.currentTimeMillis()}",
                            "property_images": [
                                "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=500&q=80"
                            ],
                            "state": "1",
                            "status": "Active",
                            "time": "12:00:00",
                            "total_rooms": "3",
                            "trash": "0",
                            "uid": "mock_user_123",
                            "wishlist": 0,
                            "city_name": "Custom City",
                            "state_name": "Custom State",
                            "plan_type": "Standard",
                            "pincode": "$pincode"
                        }
                        """.trimIndent()
                        MockDataHelper.addedProperties.add(newPropJson)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                MockDataHelper.SUCCESS_RESPONSE
            }
            url.contains("get_wishist.php") -> MockDataHelper.VIEW_PROPERTY_RESPONSE
            url.contains("location_property.php") -> MockDataHelper.SEARCH_RESPONSE
            url.contains("payment_number_verify.php") -> MockDataHelper.PAYMENT_NUMBER_VERIFY_RESPONSE
            url.contains("payment_details.php") -> MockDataHelper.PAYMENT_DETAILS_RESPONSE
            url.contains("payment_responce.php") -> MockDataHelper.PAYMENT_STATUS_RESPONSE
            url.contains("user_payment_history.php") -> MockDataHelper.MY_PAYMENT_RESPONSE
            url.contains("fetch_pincode.php") -> MockDataHelper.PINCODE_RESPONSE
            url.contains("user_featured_property.php") -> MockDataHelper.VIEW_PROPERTY_RESPONSE
            else -> MockDataHelper.SUCCESS_RESPONSE
        }

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(jsonString.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }
}
