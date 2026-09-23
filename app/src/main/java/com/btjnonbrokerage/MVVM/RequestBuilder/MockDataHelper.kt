package com.btjnonbrokerage.MVVM.RequestBuilder

object MockDataHelper {
    
    val addedProperties = mutableListOf<String>()

    val SUCCESS_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "uid": "mock_user_123"
        }
    """.trimIndent()

    val LOGIN_RESPONSE = """
        {
            "status": "success",
            "msg": "Login Successful",
            "uid": "mock_user_123"
        }
    """.trimIndent()

    val REGISTER_RESPONSE = """
        {
            "status": "success",
            "msg": "Registration Successful",
            "uid": "mock_user_123"
        }
    """.trimIndent()

    val VERIFY_OTP_RESPONSE = """
        {
            "status": "success",
            "msg": "OTP Verified",
            "userdata": {
                "uid": "mock_user_123",
                "name": "Demo User",
                "mobile": "9876543210",
                "email": "demo@example.com",
                "icon": "https://i.pravatar.cc/150?img=12"
            }
        }
    """.trimIndent()

    val HOME_PAGE_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "featured_properties": [
                {
                    "id": "1",
                    "property_id": "PROP1",
                    "uid": "user1",
                    "name": "John Doe",
                    "profileimg": "https://i.pravatar.cc/150?img=11",
                    "pr_name": "Luxury Villa in Suburbs",
                    "prop_desc": "A beautiful luxury villa.",
                    "price": "5000000",
                    "bedroom": "4",
                    "bathroom": "3",
                    "balcony": "2",
                    "total_rooms": "9",
                    "category": "Villa",
                    "list_type": "Sale",
                    "facing": "North",
                    "address": "123 Suburb Lane",
                    "city": "1",
                    "city_name": "Mumbai",
                    "state": "1",
                    "state_name": "Maharashtra",
                    "pincode": "400001",
                    "phone_code": "+91",
                    "phone_number": "9876543210",
                    "lats": "19.0760",
                    "longs": "72.8777",
                    "date": "2024-01-01",
                    "time": "10:00:00",
                    "status": "Active",
                    "featured": "1",
                    "avg_rating": 4.5,
                    "rating_count": "10",
                    "wishlist": 0,
                    "trash": "0",
                    "plan_type": "Premium",
                    "facilities": ["Gym", "Pool", "Parking"],
                    "images": ["https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&q=80"]
                }
            ],
            "top_location": [
                {
                    "state_id": "1",
                    "property_count": "150",
                    "state_data": {
                        "id": "1",
                        "country_id": "1",
                        "name": "Maharashtra",
                        "img_url": "https://images.unsplash.com/photo-1570129477492-45c003edd2be?w=500&q=80"
                    }
                }
            ],
            "top_users": [
                {
                    "uid": "user2",
                    "name": "Jane Smith",
                    "profileimg": "https://i.pravatar.cc/150?img=5",
                    "property_count": "12"
                }
            ]
        }
    """.trimIndent()

    val PROFILE_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "property_count": 5,
            "review_count": 2,
            "user": {
                "id": "mock_user_123",
                "fullname": "Demo User",
                "email": "demo@example.com",
                "mobile": "9876543210",
                "phone_code": "+91",
                "profileimg": "https://i.pravatar.cc/150?img=12",
                "status": "Active",
                "usertype": "User",
                "auth_token": "mock_token_abc",
                "emailverified": "1",
                "mobileverified": "1",
                "password": "",
                "datentime": "2024-01-01 10:00:00",
                "city": "Mumbai",
                "state": "Maharashtra",
                "country": "India",
                "zipcode": "400001",
                "locality": "Bandra",
                "street": "Carter Road",
                "flat": "101",
                "deviceid": "device123"
            }
        }
    """.trimIndent()

    val SINGLE_PROPERTY_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "request_contact_status": "0",
            "property_details": {
                "id": "1",
                "property_id": "PROP1",
                "uid": "user1",
                "pr_name": "Luxury Villa in Suburbs",
                "prop_desc": "A beautiful luxury villa with amazing views and top notch amenities.",
                "price": "5000000",
                "bedroom": "4",
                "bathroom": "3",
                "balcony": "2",
                "hall": "1",
                "kitchen": "1",
                "total_rooms": "9",
                "category": "Villa",
                "list_type": "Sale",
                "facing": "North",
                "address": "123 Suburb Lane",
                "address2": "Near the park",
                "city": "1",
                "city_name": "Mumbai",
                "state": "1",
                "state_name": "Maharashtra",
                "pincode": "400001",
                "phone_code": "+91",
                "phone_number": "9876543210",
                "lats": "19.0760",
                "longs": "72.8777",
                "date": "2024-01-01",
                "time": "10:00:00",
                "status": "Active",
                "featured": "1",
                "avg_rating": 4.5,
                "wishlist": 0,
                "trash": "0",
                "plan_type": "Premium",
                "avail_from": "Immediately",
                "secure_deposit": "100000",
                "furnished": "Fully Furnished",
                "area": "2500 sqft",
                "env_facilities": ["Gym", "Pool", "Parking", "Security"],
                "property_images": [
                    "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&q=80",
                    "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=500&q=80",
                    "https://images.unsplash.com/photo-1583608205776-bfd35f0d9f83?w=500&q=80",
                    "https://images.unsplash.com/photo-1513694203232-719a280e022f?w=500&q=80"
                ],
                "reviews": [
                    {
                        "fullname": "Alice",
                        "profileimg": "https://i.pravatar.cc/150?img=1",
                        "rating": "5",
                        "comment": "Amazing place!",
                        "review_date": "2024-02-01"
                    }
                ]
            },
            "user_details": {
                "fullname": "John Doe",
                "profileimg": "https://i.pravatar.cc/150?img=11",
                "mobile": "9876543210",
                "phone_code": "+91",
                "auth_token": "token123"
            },
            "related_properties": []
        }
    """.trimIndent()

    val STATES_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "states": [
                {
                    "id": "1",
                    "country_id": "1",
                    "name": "Maharashtra"
                },
                {
                    "id": "2",
                    "country_id": "1",
                    "name": "Delhi"
                }
            ]
        }
    """.trimIndent()

    val CITIES_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "cities": [
                {
                    "id": "1",
                    "state_id": "1",
                    "city": "Mumbai"
                },
                {
                    "id": "2",
                    "state_id": "1",
                    "city": "Pune"
                }
            ]
        }
    """.trimIndent()

    val NEAR_ESTATE_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "user_estates_property": [
                {
                    "properties": [
                        {
                            "id": "1",
                            "property_id": "PROP1",
                            "uid": "user1",
                            "pr_name": "Nearby Villa",
                            "prop_desc": "A beautiful nearby villa.",
                            "price": "5000000",
                            "bedroom": "4",
                            "bathroom": "3",
                            "balcony": "2",
                            "total_rooms": "9",
                            "category": "Villa",
                            "list_type": "Sale",
                            "facing": "North",
                            "address": "123 Nearby Lane",
                            "city": "1",
                            "city_name": "Mumbai",
                            "state": "1",
                            "state_name": "Maharashtra",
                            "pincode": "400001",
                            "phone_code": "+91",
                            "phone_number": "9876543210",
                            "lats": "19.0760",
                            "longs": "72.8777",
                            "date": "2024-01-01",
                            "time": "10:00:00",
                            "status": "Active",
                            "featured": "1",
                            "avg_rating": 4.5,
                            "rating_count": "10",
                            "wishlist": 0,
                            "trash": "0",
                            "plan_type": "Premium",
                            "facilities": ["Gym", "Pool"],
                            "images": ["https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&q=80"]
                        }
                    ],
                    "remaining_properties": []
                },
                {
                    "properties": [],
                    "remaining_properties": [
                        {
                            "id": "2",
                            "property_id": "PROP2",
                            "uid": "user2",
                            "pr_name": "Other State Apartment",
                            "prop_desc": "A nice apartment in another state.",
                            "price": "3000000",
                            "bedroom": "2",
                            "bathroom": "2",
                            "balcony": "1",
                            "total_rooms": "5",
                            "category": "Apartment",
                            "list_type": "Rent",
                            "facing": "East",
                            "address": "456 Faraway Street",
                            "city": "2",
                            "city_name": "Delhi",
                            "state": "2",
                            "state_name": "Delhi",
                            "pincode": "110001",
                            "phone_code": "+91",
                            "phone_number": "9123456780",
                            "lats": "28.7041",
                            "longs": "77.1025",
                            "date": "2024-02-01",
                            "time": "11:00:00",
                            "status": "Active",
                            "featured": "0",
                            "avg_rating": 4.0,
                            "rating_count": "5",
                            "wishlist": 0,
                            "trash": "0",
                            "plan_type": "Standard",
                            "facilities": ["Gym", "Parking"],
                            "images": ["https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=500&q=80"]
                        }
                    ]
                }
            ]
        }
    """.trimIndent()

    val VIEW_PROPERTY_RESPONSE: String
        get() {
            val extraProps = if (addedProperties.isNotEmpty()) "," + addedProperties.joinToString(",") else ""
            return """
        {
            "status": "success",
            "msg": "Success",
            "properties": [
                {
                    "avg_rating": 4.5,
                    "address": "123 Suburb Lane",
                    "bathroom": "3",
                    "bedroom": "4",
                    "category": "Villa",
                    "city": "1",
                    "date": "2024-01-01",
                    "env_facilities": ["Gym", "Pool"],
                    "featured": "1",
                    "id": "1",
                    "lats": "19.0760",
                    "list_type": "Sale",
                    "longs": "72.8777",
                    "pr_name": "Owner Villa",
                    "price": "5000000",
                    "prop_desc": "A beautiful owner villa.",
                    "property_id": "PROP1",
                    "property_images": [
                        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&q=80",
                        "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=500&q=80"
                    ],
                    "state": "1",
                    "status": "Active",
                    "time": "10:00:00",
                    "total_rooms": "9",
                    "trash": "0",
                    "uid": "user1",
                    "wishlist": 0,
                    "city_name": "Mumbai",
                    "state_name": "Maharashtra",
                    "plan_type": "Premium"
                }$extraProps
            ]
        }
    """.trimIndent()
        }

    val SEARCH_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "search_location_property": {
                "properties": [
                    {
                        "avg_rating": 4.5,
                        "address": "123 Suburb Lane",
                        "address2": "Apt 4B",
                        "bathroom": "3",
                        "bedroom": "4",
                        "category": "Villa",
                        "city": "1",
                        "city_name": "Mumbai",
                        "date": "2024-01-01",
                        "facilities": ["Gym", "Pool"],
                        "facing": "East",
                        "featured": "1",
                        "id": "1",
                        "images": [
                            "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&q=80",
                            "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=500&q=80"
                        ],
                        "lats": "19.0760",
                        "list_type": "Sale",
                        "longs": "72.8777",
                        "phone_code": "+91",
                        "phone_number": "9876543210",
                        "hall": "1",
                        "kitchen": "1",
                        "plan_type": "Premium",
                        "pincode": "400001",
                        "pr_name": "Owner Villa",
                        "price": "5000000",
                        "prop_desc": "A beautiful owner villa.",
                        "property_id": "PROP1",
                        "rating_count": "100",
                        "state": "1",
                        "state_name": "Maharashtra",
                        "status": "Active",
                        "time": "10:00:00",
                        "total_rooms": "9",
                        "trash": "0",
                        "uid": "user1",
                        "wishlist": 0,
                        "balcony": "1"
                    }
                ]
            }
        }
    """.trimIndent()

    val PAYMENT_NUMBER_VERIFY_RESPONSE = """
        {
            "status": "success",
            "msg": "Number Verified Successfully",
            "pay_token": "mock_pay_token_12345",
            "uid": "mock_user_123"
        }
    """.trimIndent()

    val PAYMENT_DETAILS_RESPONSE = """
        {
            "status": "success",
            "msg": "Details Submitted",
            "pay_token": "mock_pay_token_12345"
        }
    """.trimIndent()

    val PAYMENT_STATUS_RESPONSE = """
        {
            "status": "success",
            "msg": "Payment Successful"
        }
    """.trimIndent()

    val MY_PAYMENT_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "payment_history": [
                {
                    "acc_num": "XXXXXX1234",
                    "agr_img_url": "",
                    "beneficiar_pan": "ABCDE1234F",
                    "bhk_type": "2 BHK",
                    "code": "SUCCESS",
                    "country_code": "+91",
                    "date": "2024-01-01",
                    "email": "user@example.com",
                    "id": "1",
                    "ifsc": "SBIN0001234",
                    "landlord_name": "Landlord Name",
                    "landlord_phone": "9876543210",
                    "name": "Tenant Name",
                    "pay_mode": "UPI",
                    "pay_status": "Success",
                    "pay_token": "mock_pay_token_12345",
                    "pay_type": "House Rent",
                    "payment_id": "PAY12345",
                    "phone": "9876543210",
                    "property_address": "123 Main St",
                    "rent_amt": "15000",
                    "response": "SUCCESS",
                    "status": "1",
                    "tenant_pan": "XYZAB1234C",
                    "time": "10:00 AM",
                    "trash": "0",
                    "uid": "mock_user_123",
                    "upi_id": "user@upi"
                }
            ]
        }
    """.trimIndent()

    val PINCODE_RESPONSE = """
        {
            "status": "success",
            "msg": "Success",
            "pincodes": ["400001", "400002", "400003", "110001", "110002"]
        }
    """.trimIndent()
}
