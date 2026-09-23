package com.btjnonbrokerage.Base

import androidx.lifecycle.MutableLiveData

object NavigationViewModel {

    val onboardingNext = MutableLiveData<Boolean>()
    val onboardingBack = MutableLiveData<Boolean>()
    val onboardingEnd = MutableLiveData<Boolean>()
    val categoriesSeeAllViewModel = MutableLiveData<Boolean>()
    val paymentCheckModel = MutableLiveData<Boolean>()
    val paymentRentViewModel  = MutableLiveData<Boolean>()
}