package com.btjnonbrokerage.Fragment.MainFragment

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.colorResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.Location.StateSelectDialogFragment
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageRequest
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.NearEstateRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.FeaturedProperty

import com.btjnonbrokerage.Model.HomeFilterModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.messaging
import com.btjnonbrokerage.MVVM.APIModel.FCM.FCMRequest

import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationScreen.NotificationCheckRequest
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.HomeExploreNearbyAdapter
import com.btjnonbrokerage.adapters.HomeExploreOtherStateAdapter
import com.btjnonbrokerage.adapters.HomeFeaturedAdapter
import com.btjnonbrokerage.adapters.HomeFilterOptionsAdapter
import com.btjnonbrokerage.adapters.HomeTopEstateOwnerAdapter
import com.btjnonbrokerage.adapters.HomeTopLocationAdapter
import com.btjnonbrokerage.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val homeFilterList = ArrayList<HomeFilterModel>().apply {
        add(HomeFilterModel("Pay Rent"))
        add(HomeFilterModel("Add your property"))
    }


    private val homeFilterOptionsAdapter by lazy {
        HomeFilterOptionsAdapter(onClick = {

            if (it == "Pay Rent") {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToPaymentVerificationFragment()
                findNavController().navigate(action)
            } else if (it == "Add your property") {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAddListing1Fragment()
                findNavController().navigate(action)
            } else {
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(it)
                findNavController().navigate(action)
            }


        })
    }
    private val topLocationAdapter by lazy {
        HomeTopLocationAdapter(onClick = {
            val action =
                HomeFragmentDirections.actionHomeFragmentToSearchFragment(it.state_data.name)
            findNavController().navigate(action)
        })
    }
    private val topEstateOwnerAdapter = HomeTopEstateOwnerAdapter(onClick = {
        val action = HomeFragmentDirections.actionHomeFragmentToTopUserProfileFragment(it.uid)
        findNavController().navigate(action)
    })
    private val exploreNearbyAdapter = HomeExploreNearbyAdapter(onClick = {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFullFragment(it.property_id)
        findNavController().navigate(action)
    }, clickOnLike = {
        addToWishListBaseAPI(it)
    }, clickOnUnLike = {
        removeToWishListBaseAPI(it.property_id)
    })


    private val exploreOtherStateAdapter by lazy {
        HomeExploreOtherStateAdapter(onClick = {
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailFullFragment(it.property_id)
            findNavController().navigate(action)
        }, clickOnLike = {
            addToWishListBaseAPI(it)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        })
    }


    private val featuredAdapter by lazy {
        HomeFeaturedAdapter(onClick = {
            val action =
                HomeFragmentDirections.actionHomeFragmentToDetailFullFragment(it.property_id)
            findNavController().navigate(action)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        }, clickOnLike = {
            addToWishListBaseAPI(it)
        })
    }


    // Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


        userEstateAIP("Delhi")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(requireContext(), Locale.getDefault())


        // User Info
        val sharedPreferences = MySharedPreferences(requireContext())
        binding.homeName.text = uppercaseLowerCase(sharedPreferences.getUserName().toString())
        Glide.with(requireContext()).load(MySharedPreferences(requireContext()).getUserImage())
            .placeholder(R.drawable.user_avatar).into(binding.homeProfileImage)

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notificationListFragment)
        }
        binding.homeProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.featuredStateViewAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_featuredListFeaturedEstateFragment2)
        }

        binding.tvTopLocationExplore.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_topLocationsFragment)
        }

        binding.cardViewSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment(" ")
            findNavController().navigate(action)
        }

        binding.homeProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.tvTopOwnersExplore.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_topEstateOwnerFragment2)
        }

        binding.cardLocations.setOnClickListener {
            StateSelectDialogFragment(onItemSelected = {
                binding.tvUserLocations.text = it.name
                userEstateAIP(it.name)
            }).show(parentFragmentManager, "StateDialogBox")
        }





        categoryFun()
        checkLocationPermission()
        homeAPIsFun()
        setAllAdapter()
        estateAPIsResponse()
        homeApiResponse()
        setHomeFilterAdapter()
        createNotificationChannel()
        askNotificationPermission()
        firebaseMessagingService()
        checkNotificationApi()
        observeNotificationCheckResponse()

    }


    private fun setHomeFilterAdapter() {
        binding.rvHomeFilter.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = homeFilterOptionsAdapter
        }
        homeFilterOptionsAdapter.submitList(homeFilterList)
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    val latitude = location.latitude
                    val longitude = location.longitude
                    
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val userState = getUserState(LatLng(latitude, longitude))
                            withContext(Dispatchers.Main) {
                                userEstateAIP(userState)
                                binding.tvUserLocations.text = userState
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            withContext(Dispatchers.Main) {
                                userEstateAIP("Delhi")
                            }
                        }
                    }

                } else {
                    userEstateAIP("Delhi")
                }
            }
    }


    private fun homeAPIsFun() {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            Log.d("uid123456", uid)
            viewModel.homePageVM(HomePageRequest(uid))
        }
    }

    private fun userEstateAIP(userState: String) {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            viewModel.homePageVM(HomePageRequest(uid))
            authViewModel.nearEstateViewModel(NearEstateRequest(uid, userState))
        }
    }


    private fun setAllAdapter() {
        //Featured
        binding.rvFeaturedScroll.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedScroll.adapter = featuredAdapter
        //locations Adapter
        binding.rvTopLocation.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = topLocationAdapter
        }
        // Owner Adapter
        binding.rvTopEstateOwners.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopEstateOwners.adapter = topEstateOwnerAdapter

        //Other estate
        binding.rvExploreNearbyEstates.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvExploreNearbyEstates.adapter = exploreNearbyAdapter

        binding.rvExploreOtherEstates.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvExploreOtherEstates.adapter = exploreOtherStateAdapter


    }


    private fun estateAPIsResponse() {
        // estate
        Log.d("Near123", "Near123")
        authViewModel.nearEstateRequestLiveData.observe(viewLifecycleOwner) { response ->

            Log.d("Near123", "status: ${response.status}")
            if (response.status == "success") {
                try {
                    val nearbyEstates = response.user_estates_property.getOrNull(0)?.properties
                    val otherEstates =
                        response.user_estates_property.getOrNull(0)?.remaining_properties

                    // Check if the nearby estates data is empty
                    if (nearbyEstates?.size == 0) {
                        binding.tvExploreNearbyEstates.visibility = View.GONE
                        exploreNearbyAdapter.submitList(nearbyEstates)
                    } else {
                        binding.tvExploreNearbyEstates.visibility = View.VISIBLE
                        exploreNearbyAdapter.submitList(nearbyEstates)
                        Log.d("Near123", "Near123: $nearbyEstates")
                    }

                    // Populate other states data if available
                    otherEstates?.let {
                        exploreOtherStateAdapter.submitList(it)
                    }

                    val otherStateProperties =
                        response.user_estates_property.getOrNull(1)?.remaining_properties
                    otherStateProperties?.let {
                        exploreOtherStateAdapter.submitList(it)

                    }

                } catch (e: Exception) {
                    Log.e("EstateAPI", "Error processing estate data", e)
                }
            }
        }
    }

    private fun homeApiResponse() {
        viewModel.homeLiveData.observe(viewLifecycleOwner) { response ->
            val localProps = MySharedPreferences(requireContext()).getUploadedPropertiesList().map {
                FeaturedProperty(
                    address = it.property_details.address ?: "",
                    avg_rating = 0.0f,
                    balcony = it.property_details.balcony ?: "",
                    bathroom = it.property_details.bathroom ?: "",
                    bedroom = it.property_details.bedroom ?: "",
                    category = it.property_details.category ?: "",
                    city = it.property_details.city ?: "",
                    city_name = it.property_details.city_name ?: "",
                    date = it.property_details.date ?: "",
                    facilities = it.property_details.env_facilities ?: emptyList(),
                    facing = it.property_details.facing ?: "",
                    featured = it.property_details.featured ?: "",
                    id = it.property_details.id ?: "",
                    images = it.property_details.property_images ?: emptyList(),
                    lats = it.property_details.lats ?: "",
                    list_type = "",
                    longs = it.property_details.longs ?: "",
                    name = it.property_details.pr_name ?: "",
                    phone_code = it.property_details.phone_code ?: "",
                    phone_number = it.property_details.phone_number ?: "",
                    pincode = it.property_details.pincode ?: "",
                    pr_name = it.property_details.pr_name ?: "",
                    price = it.property_details.price ?: "",
                    prop_desc = it.property_details.prop_desc ?: "",
                    property_id = it.property_details.property_id ?: "",
                    rating_count = "0",
                    state = it.property_details.state ?: "",
                    state_name = it.property_details.state_name ?: "",
                    status = it.property_details.status ?: "",
                    time = it.property_details.time ?: "",
                    total_rooms = it.property_details.total_rooms ?: "",
                    trash = "",
                    uid = it.property_details.uid ?: "",
                    wishlist = if (isFavoritedLocal(it.property_details.property_id ?: "")) 1 else 0,
                    plan_type = it.property_details.plan_type ?: ""
                )
            }
            
            val combined = mutableListOf<FeaturedProperty>()
            combined.addAll(localProps)
            if (response.status == "success") {
                combined.addAll(response.featured_properties)
                topLocationAdapter.submitList(response.top_location)
                topEstateOwnerAdapter.submitList(response.top_users)
            }
            
            featuredAdapter.submitList(combined)
            //Simmer Effect
            binding.homePageSimmer.visibility = View.GONE
            binding.homePage.visibility = View.VISIBLE
        }
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getLastKnownLocation()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastKnownLocation()
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun getUserState(location: LatLng): String {
        return try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            MySharedPreferences(requireContext()).setLocation(location.latitude.toString(),location.longitude.toString())
            if (!addresses.isNullOrEmpty() && addresses[0].locality != null) {
                addresses[0].locality
            } else {
                "Delhi"
            }
        } catch (e: Exception) {
            "Delhi"
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
        } else {
        }
    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            val notificationManager =
                activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun fireBaseFCMApi(fcm: String) {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            authViewModel.firebaseFCMViewModel(FCMRequest(fcm, uid))
        }

    }

    private fun firebaseMessagingService() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            fireBaseFCMApi(token)

        })
    }

    private fun checkNotificationApi() {
        val uid = MySharedPreferences(requireContext()).getToken()

        uid?.let {
            authViewModel.notificationCheckViewModel(NotificationCheckRequest(uid))
        } ?: run {
            // Handle null token, e.g., show error message or redirect user
            showErrorSnackBar("User is not authenticated", false)
        }
    }
    private fun observeNotificationCheckResponse(){
        authViewModel.notificationCheckLiveData.observe(viewLifecycleOwner){

        }
    }


    private fun categoryFun(){
        binding.tvCategoryFlat.setOnClickListener {
            binding.tvCategoryFlat.setTextColor(ContextCompat.getColor(requireContext(), R.color.textAcc))
            binding.tvCategoryHouse.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
            binding.tvCategoryApartment.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
            binding.viewFlat.visibility = View.VISIBLE
            binding.viewHouse.visibility = View.GONE
            binding.viewApartment.visibility = View.GONE

        }
        binding.tvCategoryHouse.setOnClickListener {
              binding.tvCategoryHouse.setTextColor(ContextCompat.getColor(requireContext(),R.color.textAcc))
              binding.tvCategoryApartment.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
              binding.tvCategoryFlat.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
              binding.viewApartment.visibility = View.GONE
              binding.viewFlat.visibility = View.GONE
              binding.viewHouse.visibility = View.VISIBLE
        }
        binding.tvCategoryApartment.setOnClickListener {
            binding.tvCategoryApartment.setTextColor(ContextCompat.getColor(requireContext(),R.color.textAcc))
            binding.tvCategoryHouse.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
            binding.tvCategoryFlat.setTextColor(ContextCompat.getColor(requireContext(),R.color.grey))
            binding.viewApartment.visibility = View.VISIBLE
            binding.viewFlat.visibility = View.GONE
            binding.viewHouse.visibility = View.GONE
        }

    }




}