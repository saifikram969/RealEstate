package com.btjnonbrokerage.MVVM.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.GetFavoritesRequest
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.GetFavoritesResponse
import com.btjnonbrokerage.MVVM.APIModel.favorite.removefavorite.RemoveFavoriteRequest
import com.btjnonbrokerage.MVVM.APIModel.featurerequest.MakeFeatureRequest
import com.btjnonbrokerage.MVVM.APIModel.featurerequest.MakeFeatureResponse
import com.btjnonbrokerage.MVVM.APIModel.review.AddReview.AddReviewRequest
import com.btjnonbrokerage.MVVM.APIModel.review.AddReview.AddReviewResponse
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.AllReviewListRequest
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.AllReviewListResponse
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyResponse
import com.btjnonbrokerage.MVVM.Repository.MainRepo
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repo: MainRepo) : ViewModel() {

    val viewPropertyLiveData: LiveData<ViewPropertyResponse>
        get() = repo.singlePropertyLiveData
    fun getUserPropertyViewModel(body: ViewPropertyRequest) {
        repo.getUserPropertyFun(body)
    }

    val homeLiveData: LiveData<HomePageResponse>
        get() = repo.homeLiveData

    fun homePageVM(body: HomePageRequest){
        repo.getHomePage(body)
    }

    fun addFavoriteVM(body: AddFavoriteRequest) {
        repo.addFavorite(body)
    }

    fun removeFavoriteVM(body: RemoveFavoriteRequest) {
        repo.removeFavorite(body)
    }

    val getFavoriteLiveData : LiveData<GetFavoritesResponse>get() = repo.getFavoritesResponse
    fun getFavorite(body : GetFavoritesRequest){
        repo.getFavoriteFun(body)
    }

    val getStateLiveData : LiveData<StateLocationsResponse>
        get() = repo.getStatesLiveData
    fun getStateViewModel(){
        repo.getStatesFun()
    }

    val getCityLiveData : LiveData<CityResponse>
        get() = repo.getCityLiveData
    fun getCityViewModel(body : CityRequest){
        repo.getCityFun(body)
    }

    val topLocationsLiveData: LiveData<TopLocationsResponse>
    get() = repo.topLocationLiveData
    fun getTopLocations(body : TopLocationsRequest){
        repo.topLocationsFun(body)

    }


    val topUserLiveData: LiveData<TopUserResponse> get() = repo.topUserLiveData
    fun getTopUser(body : TopUserRequest){ repo.topUsersFun(body)
    }


    val addReviewApiLiveData : LiveData<AddReviewResponse> get() = repo.addReviewApiLiveData
    fun addReviewApi(body : AddReviewRequest){
        repo.addReviewApi(body)
    }

    val searchLiveData : LiveData<SearchResponse>
        get() = repo.searchLiveData
    fun searchViewModel(body : SearchRequest){
        repo.searchFun(body)
    }

    val allFeaturedLiveData : LiveData<ALLFeaturedResponse>
    get() = repo.allFeaturedLiveData
    fun getAllFeatured(body : ALLFeaturedRequest){
        repo.allFeaturedFun(body)
    }

    val editPropertyLiveData : LiveData<EditPropertyResponse>
        get() = repo.editPropertyLiveData
    fun editPropertyViewModel(body : EditPropertyRequest){
        repo.editPropertyFun(body)
    }

    val deletePropertyLiveData : LiveData<DeletePropertyResponse>
    get() = repo.deletePropertyLiveData
    fun deletePropertyViewModel(propertyId : String){
        repo.deletePropertyFun(DeletePropertyRequest(propertyId))
    }


    val fetchPincodeLiveData: LiveData<PinCodeResponse>
        get() = repo.fetchPincodeLiveData
    fun fetchPincode(body: PinCodeRequest) {
        repo.fetchPincodeFun(body)
    }


    val makeFeatureApiLiveData : LiveData<MakeFeatureResponse> get() = repo.makeFeatureApiLiveData
    fun makeFeatureApi(body : MakeFeatureRequest){
        repo.makeFeatureApi(MakeFeatureRequest(body.featured,body.property_id,body.uid))
    }

    val contactToOwnerLiveData : LiveData<ContactToOwnerResponse>
        get() = repo.contactToOwnerLiveData
    fun contactToOwner(body : ContactToOwnerRequest){
        repo.contactToOwnerFun(body)

    }

    val getAllReviewListLiveData : LiveData<AllReviewListResponse>
        get() = repo.allReviewListLiveData

    fun allReviewListFun(body : AllReviewListRequest){
        repo.allReviewList(body)
    }

    val sendMessagesLiveData : LiveData<SendMessagesResponse>
        get() = repo.sendMessagesLiveData
    fun sendMessagesFun(body : SendMessagesRequest){
        repo.sendMessagesFun(body)
    }

    val fetchMessagesLiveData : LiveData<FetchMessagesResponse>
        get()  = repo.fetchMessagesLiveData
    fun fetchMessagesFun(body :FetchMessagesRequest){
        repo.fetchMessagesFun(body)
    }

}