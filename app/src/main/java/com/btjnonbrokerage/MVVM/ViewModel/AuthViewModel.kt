package com.btjnonbrokerage.MVVM.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

import com.btjnonbrokerage.MVVM.Repository.AuthRepo
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {


    val registerWithPhoneRequestLiveData: LiveData<RegisterWithPhoneRes>
        get() = repo.registerWithPhoneLiveData

    fun registerWithPhoneViewModel(body: RegisterWithPhoneReq) {
        repo.registerWithPhoneFun(body)
    }

    val addPropertyLiveData: LiveData<AddPropertyResponse>
        get() = repo.addPropertyLiveData

    fun addPropertyViewModel(body: AddPropertyRequest) {
        repo.addPropertyFun(body)
    }
    val getSinglePropertyRequestLiveData: LiveData<GetSinglePropertyResponse>
        get() = repo.singlePropertyLiveData

    fun getSinglePropertyViewModel(body: GetSinglePropertyRequest) {
        repo.getSinglePropertyFun(body)
    }


    val nearEstateRequestLiveData: LiveData<NearEstateResponse>
        get() = repo.nearEstateLiveData

    fun nearEstateViewModel(body: NearEstateRequest) {
        repo.getNearEstateFun(body)
    }

    val getProfileLiveData: LiveData<GetProfileResponse>
        get() = repo.getProfileLiveData
    fun getProfile(body : GetProfileRequest){
        repo.getProfile(body)
    }



    val updateProfileLiveData  : LiveData<UpdateProfileResponse>
        get() = repo.updateProfileLiveData

    fun updateProfileViewModel(body : UpdateProfileRequest){
        repo.updateProfileFun(body)
    }

    val verifyOTPLiveData : LiveData<VerifyOTPResponse>
        get() = repo.verifyOTPLiveData
    fun verifyOTPViewModel(body : VerifyOTPRequest){
        repo.verifyOTP(body)
    }
    val loginLiveData : LiveData<LoginResponse>
        get() = repo.loginLiveData
    fun loginViewModel(body : LoginRequest){
        repo.loginFun(body)
    }


    val resendOTPResponse : LiveData<ResendOTPResponse>
    get() = repo.resendOTPLiveData
    fun resendOTPViewModel(body : ResendOTPRequest){
        repo.resendOTP(body)
    }

    val firebaseFCMLiveData : LiveData<FCMResponse>
        get() = repo.firebaseFCMLiveData
    fun firebaseFCMViewModel(body : FCMRequest){
        repo.firebaseFMCFun(body)
    }

    val notificationLiveData : LiveData<NotificationResponse>
        get() = repo.notificationLiveData

    fun notificationViewModel(body : NotificationsRequest){
        repo.notificationFun(body)
    }

    val notificationCheckLiveData : LiveData<NotificationCheckResponse>
        get() = repo.notificationCheck
    fun notificationCheckViewModel(body : NotificationCheckRequest){
        repo.notificationCheck(body)
    }


}