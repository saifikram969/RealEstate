package com.btjnonbrokerage.Base

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.Property as FavoriteProperty
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse

class  MySharedPreferences  (context: Context){
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()


    fun setToken(token: String) {
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun setUser(name : String? , number : String? , userImage : String? ){
        editor.putString("name", name)
        editor.putString("number", number)
        editor.putString("userImage",userImage)
        editor.apply()
    }


    fun getUserName(): String?{
        return sharedPreferences.getString("name", null)
    }
    fun getUserNumber() : String? {
        return sharedPreferences.getString("number", null)
    }

    fun getUserImage() : String? {
        return sharedPreferences.getString("userImage", null)
    }

    fun clearSharedPreferences() {
        editor.clear()
        editor.apply()
    }
    fun setLocation(latitude : String?,longitude:String?) {
        editor.putString("latitude", latitude)
        editor.putString("longitude",longitude)
        editor.apply()
    }

    fun getLatitude(): String? {
        return sharedPreferences.getString("latitude", null)
    }
    fun getLongitude():String?{
        return sharedPreferences.getString("longitude",null)
    }

    fun setProfileJson(json: String) {
        editor.putString("profile_json", json)
        editor.apply()
    }

    fun getProfileJson(): String? {
        return sharedPreferences.getString("profile_json", null)
    }

    fun getFavoritesList(): List<FavoriteProperty> {
        val json = sharedPreferences.getString("local_favorites", "[]")
        val type = object : TypeToken<List<FavoriteProperty>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveFavoritesList(list: List<FavoriteProperty>) {
        val json = Gson().toJson(list)
        editor.putString("local_favorites", json)
        editor.apply()
    }

    fun getUploadedPropertiesList(): List<GetSinglePropertyResponse> {
        val json = sharedPreferences.getString("uploaded_properties", "[]")
        val type = object : TypeToken<List<GetSinglePropertyResponse>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveUploadedPropertiesList(list: List<GetSinglePropertyResponse>) {
        val json = Gson().toJson(list)
        editor.putString("uploaded_properties", json)
        editor.apply()
    }
}