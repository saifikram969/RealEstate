package com.btjnonbrokerage.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.DateFormat
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.ImageViewPagerDialogFragment
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.contactToOwner.ContactToOwnerRequest
import com.btjnonbrokerage.MVVM.APIModel.featurerequest.MakeFeatureRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.EnvFacilityAdapter
import com.btjnonbrokerage.adapters.ReviewLayoutAdapter
import com.btjnonbrokerage.databinding.FragmentDetailFullBinding
import com.btjnonbrokerage.extensions.load
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@AndroidEntryPoint
class DetailFullFragment : BaseFragment<FragmentDetailFullBinding>() {

    private val args: DetailFullFragmentArgs by navArgs()
    private var phoneNumber: String? = null
    private var isDataLoaded = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val envFacilityAdapter by lazy { EnvFacilityAdapter() }
    private val reviewLayoutAdapter by lazy { ReviewLayoutAdapter() }

    private val listImages: MutableMap<ImageView, String> = mutableMapOf()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailFullBinding = FragmentDetailFullBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentLocationAndCalculateDistance()
        mapFun()
        setupToolbar()
        setupClickListeners()

        setFacilityAdapter()
        setReviewAdapter()

        fetchPropertyDetails()
        observePropertyResponse()
        observeContactOwnerResponse()
    }

    private fun setupToolbar() {
        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupClickListeners() {
        binding.cvEditProperty.setOnClickListener {
            val action = DetailFullFragmentDirections
                .actionDetailFullFragmentToEditPropertyFragment(
                    MySharedPreferences(requireContext()).getToken().toString(),
                    args.propertyId
                )
            findNavController().navigate(action)
        }

        binding.viewAllReview.setOnClickListener {
            val action = DetailFullFragmentDirections
                .actionDetailFullFragmentToReviewFragment(args.propertyId)
            findNavController().navigate(action)
        }

        binding.editReview.setOnClickListener {
            val action = DetailFullFragmentDirections
                .actionDetailFullFragmentToALLReviewFragment(args.propertyId)
            findNavController().navigate(action)
        }

        binding.tvConnectWithOwner.setOnClickListener {
            contactOwner()
        }
    }

    private fun setReviewAdapter() {
        binding.rvReview.apply {
            adapter = reviewLayoutAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setFacilityAdapter() {
        binding.rvEnvFacilities.apply {
            adapter = envFacilityAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun fetchPropertyDetails() {
        if (args.propertyId.startsWith("local_")) {
            val localProp = MySharedPreferences(requireContext()).getUploadedPropertiesList().find {
                it.property_details.property_id == args.propertyId
            }
            if (localProp != null) {
                // Mimic the observation flow
                if (localProp.request_contact_status == "1") {
                    changeContactOwnerUI()
                }

                displayPropertyDetails(localProp)
                displayPropertyImages(localProp.property_details.property_images ?: emptyList())
                setupImageClickListeners(localProp.property_details.property_images ?: emptyList())

                phoneNumber = localProp.property_details.phone_number

                if (MySharedPreferences(requireContext()).getToken().toString() == localProp.property_details.uid) {
                    binding.cvEditProperty.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireContext(), "Local property not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            val token = MySharedPreferences(requireContext()).getToken()
            token?.let {
                viewModel.getSinglePropertyViewModel(GetSinglePropertyRequest(args.propertyId, it))
            }
        }
    }

    private fun observePropertyResponse() {
        viewModel.getSinglePropertyRequestLiveData.observe(viewLifecycleOwner) { data ->
            if (data.request_contact_status == "1") {
                changeContactOwnerUI()
            }

            displayPropertyDetails(data)
            displayPropertyImages(data.property_details.property_images ?: emptyList())
            setupImageClickListeners(data.property_details.property_images ?: emptyList())

            phoneNumber = data.property_details.phone_number

            if (MySharedPreferences(requireContext()).getToken()
                    .toString() == data.property_details.uid
            ) {
                binding.cvEditProperty.visibility = View.VISIBLE
            }
        }
    }

    private fun displayPropertyDetails(data: GetSinglePropertyResponse) {
        with(data.property_details) {
            binding.propertyCategory.text = category
            binding.buildingName.text = pr_name
            binding.propertyAddress.text =
                "${city_name?.capitalize()}, ${state_name?.capitalize()} #$pincode"
            binding.propertyPrice.text = "₹ $price"
            binding.tvMonthOrYear.text = plan_type.capitalize()
            binding.tvAddress1.text = address?.capitalize()
            binding.tvAddressLine2.text = address2?.capitalize()
            binding.tvPropertyDescription.text = prop_desc?.capitalize()

            binding.tvSecurityDeposit.text = secure_deposit
            binding.tvPropertyType.text = furnished
            binding.propertyAvailability.text = avail_from
            binding.nortSouth.text = facing
            binding.tvArea.text = area+"/sq.ft"


            //about the Room
            aboutRoom(bedroom,bathroom,balcony,hall)
        }

        envFacilityAdapter.submitList(data.property_details.env_facilities ?: emptyList())
        val reviewList = data.property_details.reviews
        if (!reviewList.isNullOrEmpty()) {
            binding.viewAllReview.visibility = View.VISIBLE
            reviewLayoutAdapter.submitList(data.property_details.reviews ?: emptyList())
        }

    }

    private fun displayPropertyImages(images: List<String>) {
        try {
            if (images.size > 4) {
                binding.remainingImageTv.text = (images.size - 3).toString()
            }
            if (images.isNotEmpty()) {
                listImages[binding.bigImage] = images.getOrNull(0) ?: ""
                listImages[binding.smallImage1] = images.getOrNull(1) ?: images.getOrNull(0) ?: ""
                listImages[binding.smallImage2] = images.getOrNull(2) ?: images.getOrNull(0) ?: ""
                listImages[binding.moreImg] = images.getOrNull(3) ?: images.getOrNull(0) ?: ""
            }

            loadImagesIntoViews(images)
        } catch (e: Exception) {
            Log.e("DetailFullFragment", "Error loading images", e)
        }
    }

    private fun loadImagesIntoViews(images: List<String>) {
        with(binding) {
            images.getOrNull(0)?.let { bigImage.load(it) }
            images.getOrNull(1)?.let { smallImage1.load(it) } ?: images.getOrNull(0)?.let { smallImage1.load(it) }
            images.getOrNull(2)?.let { smallImage2.load(it) } ?: images.getOrNull(0)?.let { smallImage2.load(it) }
            images.getOrNull(3)?.let { moreImg.load(it) } ?: images.getOrNull(0)?.let { moreImg.load(it) }
        }
    }

    private fun setupImageClickListeners(images: List<String>) {
        binding.smallImage1.setOnClickListener {
            swapImages(binding.bigImage, binding.smallImage1)
        }

        binding.smallImage2.setOnClickListener {
            swapImages(binding.bigImage, binding.smallImage2)
        }

        binding.moreImg.setOnClickListener {
            swapImages(binding.bigImage, binding.moreImg)
            if (images.size > 4) {
                ImageViewPagerDialogFragment(images)
                    .show(parentFragmentManager, "ImageViewPagerDialogFragment")
            }
        }
    }

    private fun swapImages(imageView1: ImageView, imageView2: ImageView) {
        val image1 = listImages[imageView1]
        val image2 = listImages[imageView2]

        if (image1 != null && image2 != null) {
            imageView1.load(image2)
            imageView2.load(image1)
            listImages[imageView1] = image2
            listImages[imageView2] = image1
        } else {
            Log.w(
                "DetailFullFragment",
                "Cannot swap images, one or both image references are null."
            )
        }
    }

    private fun contactOwner() {
        val token = MySharedPreferences(requireContext()).getToken()
        token?.let {
            mainViewModel.contactToOwner(ContactToOwnerRequest(args.propertyId, it))
        }
    }

    private fun observeContactOwnerResponse() {
        mainViewModel.contactToOwnerLiveData.observe(viewLifecycleOwner) {
            showErrorSnackBar(it.msg, false)
            changeContactOwnerUI()
        }
    }

    private fun changeContactOwnerUI() {
        binding.tvConnectWithOwner.apply {
            setBackgroundResource(R.drawable.background_yellow_btn)
            text = "Contact Owner Sent"
        }
    }

    private fun aboutRoom(bedroom : String,bathroom : String,balcony : String,hall:String){
        Log.d("bathroom",bathroom)
        binding.aboutBedroom.apply {
            tvAbout.text =  "$bedroom Bedroom"
            ivAbout.setImageResource(R.drawable.bed_icon)
        }
        binding.aboutBathroom.apply {
            tvAbout.text =  "$bathroom Bathroom"
            ivAbout.setImageResource(R.drawable.bathtub)
        }
        binding.aboutHall.apply {
            tvAbout.text =  "$hall Hall"
            ivAbout.setImageResource(R.drawable.virtual)

        }
        binding.aboutBalcony.apply {
            tvAbout.text =  "$balcony Balcony"
            ivAbout.setImageResource(R.drawable.balcony)
        }
    }
    private fun mapFun(){
        var map  = binding.mapView
        Configuration.getInstance().load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val mapController = map.controller
        mapController.setZoom(15)
        val startPoint = GeoPoint(28.7041, 77.1025) // Example: Delhi
        mapController.setCenter(startPoint)


       val marker = Marker(map)
        marker.position = startPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Your Location"
       map.overlays.add(marker)
    }
    private fun getCurrentLocationAndCalculateDistance() {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
//            return
//        }
//
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            location?.let {
//                val currentLat = it.latitude
//                val currentLng = it.longitude
//
//                // Property Location (Example)
//                val propertyLat = 28.7041
//                val propertyLng = 77.1025
//
//                // Distance Calculation
//                val distance = calculateDistance(currentLat, currentLng, propertyLat, propertyLng)
//                Toast.makeText(requireContext(), "Distance: $distance km", Toast.LENGTH_LONG).show()
//
//                // Show marker on map
//                showMarkersOnMap(currentLat, currentLng, propertyLat, propertyLng)
//            }
//        }

        val sharedPreferences = MySharedPreferences(requireContext())

// Get stored latitude and longitude, or use default values if null
        //val currentLat = sharedPreferences.getLatitude()?.toDoubleOrNull() ?: 0.0
        //val currentLng = sharedPreferences.getLongitude()?.toDoubleOrNull() ?: 0.0
        val currentLat = 28.9845
        val currentLng = 77.7064
        val propertyLat = 28.7041
        val propertyLng = 77.1025

// Check if we have valid coordinates
        if (currentLat == 0.0 && currentLng == 0.0) {
            Toast.makeText(requireContext(), "Current location not available", Toast.LENGTH_LONG).show()
        } else {
            // Calculate Distance
            val distance = calculateDistance(currentLat, currentLng, propertyLat, propertyLng)
            binding.tvDistance.text = "$distance km"
            Toast.makeText(requireContext(), "Distance: $distance km", Toast.LENGTH_LONG).show()

            // Show markers on the map
            showMarkersOnMap(currentLat, currentLng, propertyLat, propertyLng)
        }

    }
    private fun showMarkersOnMap(currLat: Double, currLng: Double, propLat: Double, propLng: Double) {
        val map = binding.mapView

        // Set current location marker
        val currentLocation = GeoPoint(currLat, currLng)
        val currentMarker = Marker(map)
        currentMarker.position = currentLocation
        currentMarker.setAnchor(Marker.ANCHOR_CENTER.toFloat(), Marker.ANCHOR_BOTTOM.toFloat())
        currentMarker.title = "Current Location"
        map.overlays.add(currentMarker)

        // Set property location marker
        val propertyLocation = GeoPoint(propLat, propLng)
        val propertyMarker = Marker(map)
        propertyMarker.position = propertyLocation
        propertyMarker.setAnchor(Marker.ANCHOR_CENTER.toFloat(), Marker.ANCHOR_BOTTOM.toFloat())
        propertyMarker.title = "Property Location"
        map.overlays.add(propertyMarker)

        // Create a line (Polyline) between current location and property location
        val polyline = Polyline()
        polyline.setPoints(listOf(currentLocation, propertyLocation))
        polyline.color = android.graphics.Color.RED // Set color of the line
        polyline.width = 5f // Set width of the line
        map.overlays.add(polyline) // Add the line to the map

        // Center map between both locations
        map.controller.setCenter(currentLocation)
        map.controller.setZoom(12.0) // Adjust zoom level as needed

        // Refresh Map
        map.invalidate()
    }
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Radius of the Earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Distance in km
    }



}

