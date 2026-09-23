package com.btjnonbrokerage.MVVM.Repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.btjnonbrokerage.MVVM.APIModel.AddProperty.AddPropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.AddProperty.AddPropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.Auth.Login.LoginRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.Login.LoginResponse

import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.NearEstateRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.OTP.VerifyOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.OTP.VerifyOTPResponse
import com.btjnonbrokerage.MVVM.APIModel.Auth.Register.RegisterWithPhoneReq
import com.btjnonbrokerage.MVVM.APIModel.Auth.Register.RegisterWithPhoneRes
import com.btjnonbrokerage.MVVM.APIModel.Auth.ResendOTP.ResendOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.ResendOTP.ResendOTPResponse
import com.btjnonbrokerage.MVVM.APIModel.FCM.FCMRequest
import com.btjnonbrokerage.MVVM.APIModel.FCM.FCMResponse
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.NearEstateResponse
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.NotificationResponse
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.NotificationsRequest
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationScreen.NotificationCheckRequest
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationScreen.NotificationCheckResponse
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileResponse
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.UpdateProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.UpdateProfileResponse

import com.btjnonbrokerage.MVVM.RequestBuilder.AuthService
import com.btjnonbrokerage.MVVM.RequestBuilder.RequestBuilder
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class AuthRepo @Inject constructor(@ActivityContext val context: Context) {

    private val request: AuthService = RequestBuilder.retrofit.create(AuthService::class.java)


    private val registerWithPhoneRes = MutableLiveData<RegisterWithPhoneRes>()
    val registerWithPhoneLiveData: LiveData<RegisterWithPhoneRes>
        get() = registerWithPhoneRes

    fun registerWithPhoneFun(body: RegisterWithPhoneReq) {
        val result = request.registerUserAPIService(body)
        result.enqueue(object : Callback<RegisterWithPhoneRes?> {
            override fun onResponse(
                call: Call<RegisterWithPhoneRes?>,
                response: Response<RegisterWithPhoneRes?>
            ) {

                if (response.code() == 200) {
                    registerWithPhoneRes.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterWithPhoneRes?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }


    private val _addPropertyResponse = MutableLiveData<AddPropertyResponse>()
    val addPropertyLiveData: LiveData<AddPropertyResponse>
        get() = _addPropertyResponse


    fun addPropertyFun(body: AddPropertyRequest) {
        val result = request.addPropertyAPIService(body)
        result.enqueue(object : Callback<AddPropertyResponse?> {
            override fun onResponse(
                call: Call<AddPropertyResponse?>,
                response: Response<AddPropertyResponse?>
            ) {

                if (response.code() == 200) {
                    _addPropertyResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddPropertyResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

            }
        })
    }


    private val _singlePropertyResponse = MutableLiveData<GetSinglePropertyResponse>()
    val singlePropertyLiveData: LiveData<GetSinglePropertyResponse>
        get() = _singlePropertyResponse

    fun getSinglePropertyFun(body: GetSinglePropertyRequest) {
        val result = request.getSinglePropertyAPIService(body)
        result.enqueue(object : Callback<GetSinglePropertyResponse?> {
            override fun onResponse(
                call: Call<GetSinglePropertyResponse?>,
                response: Response<GetSinglePropertyResponse?>
            ) {
                if (response.code() == 200) {
                    _singlePropertyResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetSinglePropertyResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()

            }
        })
    }

    private val _nearEstateResponse = MutableLiveData<NearEstateResponse>()
    val nearEstateLiveData: LiveData<NearEstateResponse>
        get() = _nearEstateResponse

    fun getNearEstateFun(body: NearEstateRequest) {
        val result = request.getNearEstate(body)
        result.enqueue(object : Callback<NearEstateResponse?> {
            override fun onResponse(
                call: Call<NearEstateResponse?>,
                response: Response<NearEstateResponse?>
            ) {

                if (response.code() == 200) {
                    _nearEstateResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<NearEstateResponse?>, t: Throwable) {

                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _getProfileResponse = MutableLiveData<GetProfileResponse>()
    val getProfileLiveData: LiveData<GetProfileResponse>
        get() = _getProfileResponse

    fun getProfile(body: GetProfileRequest) {
        val sharedPrefs = com.btjnonbrokerage.Base.MySharedPreferences(context)
        val json = sharedPrefs.getProfileJson()
        val gson = com.google.gson.Gson()
        if (json != null) {
            val profile = gson.fromJson(json, GetProfileResponse::class.java)
            _getProfileResponse.postValue(profile)
        } else {
            val defaultProfileStr = com.btjnonbrokerage.MVVM.RequestBuilder.MockDataHelper.PROFILE_RESPONSE
            val defaultProfile = gson.fromJson(defaultProfileStr, GetProfileResponse::class.java)
            sharedPrefs.setProfileJson(defaultProfileStr)
            _getProfileResponse.postValue(defaultProfile)
        }
    }


    private val _updateUserResponse = MutableLiveData<UpdateProfileResponse>()
    val updateProfileLiveData: LiveData<UpdateProfileResponse>
        get() = _updateUserResponse

    fun updateProfileFun(body: UpdateProfileRequest) {
        val sharedPrefs = com.btjnonbrokerage.Base.MySharedPreferences(context)
        val json = sharedPrefs.getProfileJson()
        val gson = com.google.gson.Gson()
        if (json != null) {
            val profile = gson.fromJson(json, GetProfileResponse::class.java)
            
            val updatedUser = profile.user.copy(
                fullname = if (body.fullname.isNotEmpty()) body.fullname else profile.user.fullname,
                profileimg = body.profile_image ?: profile.user.profileimg
            )
            
            val updatedProfile = profile.copy(user = updatedUser)
            sharedPrefs.setProfileJson(gson.toJson(updatedProfile))
            
            _updateUserResponse.postValue(
                UpdateProfileResponse(
                    msg = "Profile Updated Successfully",
                    status = "success",
                    user = com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.User(
                        fullname = updatedUser.fullname,
                        mobile = updatedUser.mobile.toString(),
                        profile_image = updatedUser.profileimg
                    )
                )
            )
        } else {
            Toast.makeText(context, "No profile to update", Toast.LENGTH_SHORT).show()
        }
    }

    private val _verifyOTPResponse = MutableLiveData<VerifyOTPResponse>()
    val verifyOTPLiveData: LiveData<VerifyOTPResponse>
        get() = _verifyOTPResponse

    fun verifyOTP(body: VerifyOTPRequest) {
        val result = request.verifyOTP(body)
        result.enqueue(object : Callback<VerifyOTPResponse?> {
            override fun onResponse(
                call: Call<VerifyOTPResponse?>,
                response: Response<VerifyOTPResponse?>
            ) {
                if (response.code() == 200) {
                    _verifyOTPResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VerifyOTPResponse?>, t: Throwable) {

                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginLiveData: LiveData<LoginResponse>
        get() = _loginResponse

    fun loginFun(body: LoginRequest) {
        val result = request.loginUserAPIService(body)
        result.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.code() == 200) {
                    _loginResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _resendOTPResponse = MutableLiveData<ResendOTPResponse>()
    val resendOTPLiveData: LiveData<ResendOTPResponse>
        get() = _resendOTPResponse

    fun resendOTP(body: ResendOTPRequest) {
        val result = request.resendOTP(body)
        result.enqueue(object : Callback<ResendOTPResponse?> {
            override fun onResponse(
                call: Call<ResendOTPResponse?>,
                response: Response<ResendOTPResponse?>
            ) {
                if (response.code() == 200) {
                    _resendOTPResponse.postValue(response.body())
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResendOTPResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _firebaseFCM = MutableLiveData<FCMResponse>()
    val firebaseFCMLiveData: LiveData<FCMResponse>
        get() = _firebaseFCM

    fun firebaseFMCFun(body: FCMRequest) {
        val result = request.firebaseFMCService(body)
        result.enqueue(object : Callback<FCMResponse?> {
            override fun onResponse(call: Call<FCMResponse?>, response: Response<FCMResponse?>) {
                if (response.code() == 200) {
                    _firebaseFCM.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<FCMResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _notification = MutableLiveData<NotificationResponse>()
    val notificationLiveData : LiveData<NotificationResponse>
        get() = _notification
    fun notificationFun(body : NotificationsRequest){
        val request = request.notificationsServices(body)
        request.enqueue(object : Callback<NotificationResponse?> {
            override fun onResponse(
                call: Call<NotificationResponse?>,
                response: Response<NotificationResponse?>
            ) {
                if(response.code()==200){
                    _notification.postValue(response.body())
                }else{

                }

            }
            override fun onFailure(call: Call<NotificationResponse?>, t: Throwable) {

                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val _notificationCheck = MutableLiveData<NotificationCheckResponse>()
    val notificationCheck : LiveData<NotificationCheckResponse>
        get() = _notificationCheck

    fun notificationCheck(body : NotificationCheckRequest){
        val result = request.notificationCheckService(body)
        result.enqueue(object : Callback<NotificationCheckResponse?> {
            override fun onResponse(
                call: Call<NotificationCheckResponse?>,
                response: Response<NotificationCheckResponse?>
            ) {
                if(response.code() == 200){
                    _notificationCheck.postValue(response.body())
                }else{

                }
            }

            override fun onFailure(call: Call<NotificationCheckResponse?>, t: Throwable) {

            }
        })
    }

}