package com.btjnonbrokerage.MVVM.Repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.FetchMessagesRequest
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.FetchMessagesResponse
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send.SendMessagesRequest
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send.SendMessagesResponse
import com.btjnonbrokerage.MVVM.APIModel.DeleteProperty.DeletePropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.DeleteProperty.DeletePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.ALLFeaturedRequest
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.ALLFeaturedResponse
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageRequest
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageResponse
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityResponse
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateLocationsResponse
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopLocationsRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopLocationsResponse
import com.btjnonbrokerage.MVVM.APIModel.PinCode.PinCodeRequest
import com.btjnonbrokerage.MVVM.APIModel.PinCode.PinCodeResponse
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
import com.btjnonbrokerage.MVVM.RequestBuilder.AuthService
import com.btjnonbrokerage.MVVM.RequestBuilder.OtherService
import com.btjnonbrokerage.MVVM.RequestBuilder.RequestBuilder
import dagger.hilt.android.qualifiers.ActivityContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainRepo @Inject constructor(@ActivityContext val context: Context) {


    private val request: AuthService = RequestBuilder.retrofit.create(AuthService::class.java)
    private val request1: OtherService = RequestBuilder.retrofit.create(OtherService::class.java)


    private val _viewPropertyResponse = MutableLiveData<ViewPropertyResponse>()
    val singlePropertyLiveData: LiveData<ViewPropertyResponse>
        get() = _viewPropertyResponse

    fun getUserPropertyFun(body: ViewPropertyRequest) {
        val result = request.viewPropertyOfUser(body)
        result.enqueue(object : Callback<ViewPropertyResponse?> {
            override fun onResponse(
                call: Call<ViewPropertyResponse?>,
                response: Response<ViewPropertyResponse?>
            ) {
                if (response.code() == 200) {
                    _viewPropertyResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ViewPropertyResponse?>, t: Throwable) {

            }
        })
    }


    private val _contactToOwnerResponse = MutableLiveData<ContactToOwnerResponse>()
    val contactToOwnerLiveData: LiveData<ContactToOwnerResponse>
        get() = _contactToOwnerResponse

    fun contactToOwnerFun(body: ContactToOwnerRequest) {
        val result = request1.contactToOwner(body)
        result.enqueue(object : Callback<ContactToOwnerResponse?> {
            override fun onResponse(
                call: Call<ContactToOwnerResponse?>,
                response: Response<ContactToOwnerResponse?>
            ) {
                if (response.code() == 200) {
                    _contactToOwnerResponse.postValue(response.body())
                } else {
//                    Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ContactToOwnerResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //delete property
    private val _deletePropertyResponse = MutableLiveData<DeletePropertyResponse>()
    val deletePropertyLiveData: LiveData<DeletePropertyResponse>
        get() = _deletePropertyResponse

    fun deletePropertyFun(propertyId: DeletePropertyRequest) {
        val result = request1.deletePropertyService(propertyId)
        result.enqueue(object : Callback<DeletePropertyResponse?> {
            override fun onResponse(
                call: Call<DeletePropertyResponse?>,
                response: Response<DeletePropertyResponse?>
            ) {
                if (response.code() == 200) {
                    _deletePropertyResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DeletePropertyResponse?>, t: Throwable) {


            }
        })
    }


    //Home
    private val _homeResponse = MutableLiveData<HomePageResponse>()
    val homeLiveData: LiveData<HomePageResponse>
        get() = _homeResponse

    fun getHomePage(body: HomePageRequest) {
        val result = request1.getHomePageService(body)
        result.enqueue(object : Callback<HomePageResponse?> {
            override fun onResponse(
                call: Call<HomePageResponse?>,
                response: Response<HomePageResponse?>
            ) {
//                Toast.makeText(context, response.code().toString(), Toast.LENGTH_SHORT).show()
                _homeResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<HomePageResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.d("123456789", t.message.toString())
            }
        })
    }


    private val _addFavoriteResponse = MutableLiveData<AddFavoriteResponse>()
    val addFavoriteLiveData: LiveData<AddFavoriteResponse>
        get() = _addFavoriteResponse


    fun addFavorite(body: AddFavoriteRequest) {
        val result = request1.addPropertyFavorite(body)
        result.enqueue(object : Callback<AddFavoriteResponse?> {
            override fun onResponse(
                call: Call<AddFavoriteResponse?>,
                response: Response<AddFavoriteResponse?>
            ) {
                if (response.code() == 200) {
                    _addFavoriteResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<AddFavoriteResponse?>, t: Throwable) {

            }
        })

    }

    private val _removeFavoriteResponse = MutableLiveData<RemoveFavoriteResponse>()
    val removeFavoriteLiveData: LiveData<RemoveFavoriteResponse> get() = _removeFavoriteResponse

    fun removeFavorite(body: RemoveFavoriteRequest) {
        val result = request1.removePropertyFavorite(body)
        result.enqueue(object : Callback<RemoveFavoriteResponse?> {
            override fun onResponse(
                call: Call<RemoveFavoriteResponse?>,
                response: Response<RemoveFavoriteResponse?>
            ) {
                _removeFavoriteResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<RemoveFavoriteResponse?>, t: Throwable) {

            }
        })
    }


    private val _getFavoriteResponse = MutableLiveData<GetFavoritesResponse>()
    val getFavoritesResponse: LiveData<GetFavoritesResponse>
        get() = _getFavoriteResponse

    fun getFavoriteFun(body: GetFavoritesRequest) {
        val result = request.getFavorite(body)
        result.enqueue(object : Callback<GetFavoritesResponse?> {
            override fun onResponse(
                call: Call<GetFavoritesResponse?>,
                response: Response<GetFavoritesResponse?>
            ) {

                if (response.code() == 200) {
                    _getFavoriteResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<GetFavoritesResponse?>, t: Throwable) {

            }
        })
    }

    private val _getStatesResponse = MutableLiveData<StateLocationsResponse>()
    val getStatesLiveData: LiveData<StateLocationsResponse>
        get() = _getStatesResponse

    fun getStatesFun() {
        val result = request.getState()
        result.enqueue(object : Callback<StateLocationsResponse?> {
            override fun onResponse(
                call: Call<StateLocationsResponse?>,
                response: Response<StateLocationsResponse?>
            ) {
                if (response.code() == 200) {
                    _getStatesResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<StateLocationsResponse?>, t: Throwable) {

            }
        })
    }

    private val _getCityResponse = MutableLiveData<CityResponse>()
    val getCityLiveData: LiveData<CityResponse>
        get() = _getCityResponse

    fun getCityFun(body: CityRequest) {
        val result = request.getCity(body)
        result.enqueue(object : Callback<CityResponse?> {
            override fun onResponse(call: Call<CityResponse?>, response: Response<CityResponse?>) {
                if (response.code() == 200) {
                    _getCityResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<CityResponse?>, t: Throwable) {
            }
        })
    }

    private val _addReviewApi = MutableLiveData<AddReviewResponse>()
    val addReviewApiLiveData: LiveData<AddReviewResponse>
        get() = _addReviewApi

    fun addReviewApi(body: AddReviewRequest) {
        val result = request.addReviewApi(body)
        result.enqueue(object : Callback<AddReviewResponse?> {
            override fun onResponse(
                call: Call<AddReviewResponse?>,
                response: Response<AddReviewResponse?>
            ) {
                _addReviewApi.postValue(response.body())
            }

            override fun onFailure(call: Call<AddReviewResponse?>, t: Throwable) {
//
            }

        })
    }


    private val _topLocationResponse = MutableLiveData<TopLocationsResponse>()
    val topLocationLiveData: LiveData<TopLocationsResponse>
        get() = _topLocationResponse

    fun topLocationsFun(topLocationsRequest: TopLocationsRequest) {
        val result = request1.getTopLocationsService(topLocationsRequest)

        result.enqueue(object : Callback<TopLocationsResponse?> {
            override fun onResponse(
                call: Call<TopLocationsResponse?>,
                response: Response<TopLocationsResponse?>
            ) {
                _topLocationResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<TopLocationsResponse?>, t: Throwable) {

            }
        })
    }

    private val _topUserResponse = MutableLiveData<TopUserResponse>()
    val topUserLiveData: LiveData<TopUserResponse>
        get() = _topUserResponse

    fun topUsersFun(body: TopUserRequest) {
        val result = request1.getTopUsersService(body)
        result.enqueue(object : Callback<TopUserResponse?> {
            override fun onResponse(
                call: Call<TopUserResponse?>,
                response: Response<TopUserResponse?>
            ) {
                _topUserResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<TopUserResponse?>, t: Throwable) {

            }
        })
    }


    private val _searchResponse = MutableLiveData<SearchResponse>()
    val searchLiveData: LiveData<SearchResponse>
        get() = _searchResponse

    fun searchFun(searchRequest: SearchRequest) {
        val result = request1.searchService(searchRequest)
        result.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                if (response.code() == 200) {
                    _searchResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SearchResponse?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                Log.d("123456789", t.message.toString())
            }
        })
    }

  private  val _allReviewListResponse = MutableLiveData<AllReviewListResponse>()
    val allReviewListLiveData : LiveData<AllReviewListResponse>
        get() = _allReviewListResponse

    fun allReviewList(body : AllReviewListRequest){
        val result = request1.allReviewsListService(body)
        result.enqueue(object : Callback<AllReviewListResponse?> {
            override fun onResponse(
                call: Call<AllReviewListResponse?>,
                response: Response<AllReviewListResponse?>
            ) {
               if(response.code()==200){
                   _allReviewListResponse.postValue(response.body())
               }
            }

            override fun onFailure(call: Call<AllReviewListResponse?>, t: Throwable) {

            }
        })

    }


    private val _sendMessagesResponse = MutableLiveData<SendMessagesResponse>()
    val sendMessagesLiveData : LiveData<SendMessagesResponse>
        get() = _sendMessagesResponse

    fun sendMessagesFun(body : SendMessagesRequest) {
        val result = request1.sendMessagesServices(body)
        result.enqueue(object : Callback<SendMessagesResponse?> {
            override fun onResponse(
                call: Call<SendMessagesResponse?>,
                response: Response<SendMessagesResponse?>
            ) {
                if(response.code()==200){
                    _sendMessagesResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<SendMessagesResponse?>, t: Throwable) {

            }
        })
    }

    private val _fetchMessagesResponse = MutableLiveData<FetchMessagesResponse>()
    val fetchMessagesLiveData : LiveData<FetchMessagesResponse>
        get() = _fetchMessagesResponse

    fun fetchMessagesFun(body : FetchMessagesRequest){
        val result = request1.fetchMessagesServices(body)
        result.enqueue(object : Callback<FetchMessagesResponse?> {
            override fun onResponse(
                call: Call<FetchMessagesResponse?>,
                response: Response<FetchMessagesResponse?>
            ) {
                if(response.code() == 200 ){
                    _fetchMessagesResponse.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<FetchMessagesResponse?>, t: Throwable) {


            }
        })
    }

            private val _allFeaturedLiveData = MutableLiveData<ALLFeaturedResponse>()
    val allFeaturedLiveData: LiveData<ALLFeaturedResponse>
        get() = _allFeaturedLiveData

    fun allFeaturedFun(body: ALLFeaturedRequest) {
        val result = request1.allFeaturedProperty(body)
        result.enqueue(object : Callback<ALLFeaturedResponse?> {
            override fun onResponse(
                call: Call<ALLFeaturedResponse?>,
                response: Response<ALLFeaturedResponse?>
            ) {
                if (response.code() == 200) {
                    _allFeaturedLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ALLFeaturedResponse?>, t: Throwable) {

            }
        })
    }

    val editPropertyLiveData = MutableLiveData<EditPropertyResponse>()
    fun editPropertyFun(body: EditPropertyRequest) {
        val result = request1.editPropertyDetails(body)
        result.enqueue(object : Callback<EditPropertyResponse?> {
            override fun onResponse(
                call: Call<EditPropertyResponse?>,
                response: Response<EditPropertyResponse?>
            ) {
                if (response.code() == 200) {
                    editPropertyLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<EditPropertyResponse?>, t: Throwable) {
                Log.d("Retrofit Error", "EditPropertyAPI going in on Failure")
            }
        })
    }

    val fetchPincodeLiveData = MutableLiveData<PinCodeResponse>()
    fun fetchPincodeFun(body: PinCodeRequest) {
        val result = request1.fetchCityPincode(body)
        result.enqueue(object : Callback<PinCodeResponse?> {
            override fun onResponse(
                call: Call<PinCodeResponse?>,
                response: Response<PinCodeResponse?>
            ) {
                fetchPincodeLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<PinCodeResponse?>, t: Throwable) {

            }
        })
    }


    val makeFeatureApiLiveData = MutableLiveData<MakeFeatureResponse>()
    fun makeFeatureApi(body: MakeFeatureRequest) {
        val result = request1.makeFeatureApi(body)
        result.enqueue(object : Callback<MakeFeatureResponse?> {
            override fun onResponse(
                call: Call<MakeFeatureResponse?>,
                response: Response<MakeFeatureResponse?>
            ) {
                makeFeatureApiLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<MakeFeatureResponse?>, t: Throwable) {
//
            }
        })
    }






}