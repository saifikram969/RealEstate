package com.btjnonbrokerage.MVVM.RequestBuilder

import com.btjnonbrokerage.Fragment.Payment.MyPayment.MyPaymentRequest
import com.btjnonbrokerage.Fragment.Payment.MyPayment.MyPaymentResponse
import com.btjnonbrokerage.MVVM.APIModel.AddProperty.AddPropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.AddProperty.AddPropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.Auth.Login.LoginRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.Login.LoginResponse

import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityResponse
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateLocationsResponse
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopLocationsRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopLocationsResponse
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.NearEstateRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.OTP.VerifyOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.OTP.VerifyOTPResponse
import com.btjnonbrokerage.MVVM.APIModel.Auth.Register.RegisterWithPhoneReq
import com.btjnonbrokerage.MVVM.APIModel.Auth.Register.RegisterWithPhoneRes
import com.btjnonbrokerage.MVVM.APIModel.Auth.ResendOTP.ResendOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Auth.ResendOTP.ResendOTPResponse
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.FetchMessagesRequest
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.FetchMessagesResponse
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send.SendMessagesRequest
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send.SendMessagesResponse
import com.btjnonbrokerage.MVVM.APIModel.DeleteProperty.DeletePropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.DeleteProperty.DeletePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.FCM.FCMRequest
import com.btjnonbrokerage.MVVM.APIModel.FCM.FCMResponse
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.ALLFeaturedRequest
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.ALLFeaturedResponse
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageResponse
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.NearEstateResponse
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.NotificationResponse
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.NotificationsRequest
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationScreen.NotificationCheckRequest
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationScreen.NotificationCheckResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.AddPaymentDetails.PaymentDetailsResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.NumberVerify.PaymentNumberVerifyResponse
import com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP.VerifyPaymentNumberOTPRequest
import com.btjnonbrokerage.MVVM.APIModel.Payment.VerifyOTP.VerifyPaymentNumberOTPResponse
import com.btjnonbrokerage.MVVM.APIModel.PinCode.PinCodeRequest
import com.btjnonbrokerage.MVVM.APIModel.PinCode.PinCodeResponse
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileResponse
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.UpdateProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.UpdateProfileResponse
import com.btjnonbrokerage.MVVM.APIModel.Search.SearchRequest
import com.btjnonbrokerage.MVVM.APIModel.Search.SearchResponse
import com.btjnonbrokerage.MVVM.APIModel.TopUsers.TopUserRequest
import com.btjnonbrokerage.MVVM.APIModel.TopUsers.TopUserResponse
import com.btjnonbrokerage.MVVM.APIModel.contactToOwner.ContactToOwnerRequest
import com.btjnonbrokerage.MVVM.APIModel.contactToOwner.ContactToOwnerResponse
import com.btjnonbrokerage.MVVM.APIModel.editproperty.EditPropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.editproperty.EditPropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.favorite.addfavorite.AddFavoriteRequest
import com.btjnonbrokerage.MVVM.APIModel.favorite.addfavorite.AddFavoriteResponse
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.GetFavoritesRequest
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.GetFavoritesResponse
import com.btjnonbrokerage.MVVM.APIModel.favorite.removefavorite.RemoveFavoriteRequest
import com.btjnonbrokerage.MVVM.APIModel.favorite.removefavorite.RemoveFavoriteResponse
import com.btjnonbrokerage.MVVM.APIModel.featurerequest.MakeFeatureRequest
import com.btjnonbrokerage.MVVM.APIModel.featurerequest.MakeFeatureResponse
import com.btjnonbrokerage.MVVM.APIModel.review.AddReview.AddReviewRequest
import com.btjnonbrokerage.MVVM.APIModel.review.AddReview.AddReviewResponse
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.AllReviewListRequest
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.AllReviewListResponse

import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyRequest
import com.btjnonbrokerage.MVVM.PaymentAPIModel.PaymentStatusRequest
import com.btjnonbrokerage.MVVM.PaymentAPIModel.PaymentStatusResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


object RequestBuilder {
    private val intercepter = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }
    private val mockInterceptor = MockInterceptor()
    private val client = OkHttpClient.Builder().apply { 
        this.addInterceptor(intercepter)
        this.addInterceptor(mockInterceptor)
    }.build()

    private const val BASE_URL = "https://btjnonbrokerage.com/nonbro_api/"

    val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

interface AuthService {

    @POST("signup_mobile.php")
    fun registerUserAPIService(@Body loginRequest: RegisterWithPhoneReq): Call<RegisterWithPhoneRes>

    @POST("verify.php")
    fun verifyOTP(@Body body: VerifyOTPRequest): Call<VerifyOTPResponse>


    @POST("login_mobile.php")
    fun loginUserAPIService(@Body loginRequest: LoginRequest): Call<LoginResponse>


    @POST("add_property.php")
    fun addPropertyAPIService(@Body addPropertyRequest: AddPropertyRequest): Call<AddPropertyResponse>

    @POST("get_property.php")
    fun getSinglePropertyAPIService(@Body getSingleProperty: GetSinglePropertyRequest): Call<GetSinglePropertyResponse>

    @POST("view_property.php")
    fun viewPropertyOfUser(@Body getUserProperty: ViewPropertyRequest): Call<ViewPropertyResponse>

    @POST("user_estates.php")
    fun getNearEstate(@Body nearEstateRequest: NearEstateRequest): Call<NearEstateResponse>

    @POST("get_user_profile.php")
    fun getProfile(@Body body: GetProfileRequest): Call<GetProfileResponse>

    @POST("get_wishist.php")
    fun getFavorite(@Body body: GetFavoritesRequest): Call<GetFavoritesResponse>

    @POST("update_user.php")
    fun updateProfileService(@Body body: UpdateProfileRequest): Call<UpdateProfileResponse>

    @GET("all_states.php")
    fun getState(): Call<StateLocationsResponse>

    @POST("fetch_city.php")
    fun getCity(@Body body: CityRequest): Call<CityResponse>

    @POST("property_review.php")
    fun addReviewApi(@Body addReviewRequest: AddReviewRequest) : Call<AddReviewResponse>

    @POST("resend_otp.php")
    fun resendOTP(@Body resendOTPRequest: ResendOTPRequest) : Call<ResendOTPResponse>

    @POST("device_token_save.php")
    fun firebaseFMCService(@Body body : FCMRequest) : Call<FCMResponse>

    @POST("user_notification_list.php")
    fun notificationsServices(@Body body : NotificationsRequest) : Call<NotificationResponse>

    @POST("notification_seen_unseen.php")
    fun notificationCheckService(@Body body : NotificationCheckRequest) : Call<NotificationCheckResponse>

}

interface OtherService {
    @POST("homepage.php")
    fun getHomePageService(@Body homePageRequest: HomePageRequest): Call<HomePageResponse>

    @POST("fetch_location_page.php")
    fun getTopLocationsService(@Body topLocationsRequest: TopLocationsRequest): Call<TopLocationsResponse>

    @POST("top_user_page.php")
    fun getTopUsersService(@Body topUserRequest: TopUserRequest): Call<TopUserResponse>

    @POST("add_wish.php")
    fun addPropertyFavorite(@Body addFavoriteRequest: AddFavoriteRequest): Call<AddFavoriteResponse>

    @POST("remove_wish.php")
    fun removePropertyFavorite(@Body removeFavoriteRequest: RemoveFavoriteRequest): Call<RemoveFavoriteResponse>

    @POST("location_property.php")
    fun searchService(@Body searchRequest : SearchRequest): Call<SearchResponse>

    @POST("user_featured_property.php")
    fun  allFeaturedProperty(@Body body: ALLFeaturedRequest) : Call<ALLFeaturedResponse>

    @POST("edit_property.php")
    fun editPropertyDetails(@Body body: EditPropertyRequest) : Call<EditPropertyResponse>

    @POST("delete_property.php")
    fun deletePropertyService(@Body body : DeletePropertyRequest) : Call<DeletePropertyResponse>

    @POST("fetch_pincode.php")
    fun fetchCityPincode(@Body body: PinCodeRequest) : Call<PinCodeResponse>

    @POST("make_featured.php")
    fun makeFeatureApi(@Body body : MakeFeatureRequest) :Call<MakeFeatureResponse>

    @POST("contact_property.php")
    fun contactToOwner(@Body body : ContactToOwnerRequest) : Call<ContactToOwnerResponse>


    @POST("view_all_rating.php")
    fun allReviewsListService(@Body body : AllReviewListRequest) : Call<AllReviewListResponse>

    @POST("user_send_msg.php")
    fun sendMessagesServices(@Body body : SendMessagesRequest) : Call<SendMessagesResponse>

    @POST("user_fetch_msg.php")
    fun fetchMessagesServices(@Body body : FetchMessagesRequest) : Call<FetchMessagesResponse>
}

interface  PaymentService{

    @POST("payment_number_verify.php")
    fun paymentNumberVerifyService(@Body body: PaymentNumberVerifyRequest) : Call<PaymentNumberVerifyResponse>

    @POST("verify.php")
    fun verifyPaymentNumberOTPService(@Body body: VerifyPaymentNumberOTPRequest) : Call<VerifyPaymentNumberOTPResponse>

    @POST("payment_details.php")
    fun addPaymentDetails(@Body body: PaymentDetailsRequest) : Call<PaymentDetailsResponse>

    @POST("payment_responce.php")
    fun paymentStatusService(@Body body: PaymentStatusRequest) : Call<PaymentStatusResponse>

    @POST("user_payment_history.php")
    fun myPaymentService(@Body body: MyPaymentRequest) : Call<MyPaymentResponse>

    @POST("create_order_id.php")
    fun createOrder(@Body body: CityRequest)


}