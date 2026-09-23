package com.btjnonbrokerage.Fragment.AddProperty

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.Fragment.BottomSheet.PublishListDialogFragment
import com.btjnonbrokerage.MVVM.APIModel.AddProperty.AddPropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.RoomAdapter
import com.btjnonbrokerage.databinding.FragmentAddListing3Binding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse.PropertyDetails


@AndroidEntryPoint
class AddListingFragment3 : BaseFragment<FragmentAddListing3Binding>() {


    val args: AddListingFragment3Args by navArgs()


    private val directions = arrayOf("North", "East", "South", "West")
    private var currentIndex = 0
    private var kitchen = 0
    private var hall = 0
    private var bedrooms = 0
    private var bathrooms = 0
    private var balcony = 0
    private var parkingLot = false
    private var petAllowed = false
    private var garden = false
    private var gym = false
    private var park = false
    private var familiy = false
    private var facing = ""
    private var plantype = "monthly"
    private var security = ""
    private var securityCustom = false

    @Inject
    lateinit var viewModel: AuthViewModel


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddListing3Binding = FragmentAddListing3Binding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDirection()

        binding.commonToolbar.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.ForwardCard.setOnClickListener {
            currentIndex = (currentIndex + 1) % directions.size
            updateDirection()
        }

        binding.etRentPrice.addTextChangedListener(CurrencyTextWatcher(binding.etRentPrice))


        binding.backCard.setOnClickListener {
            currentIndex = (currentIndex - 1 + directions.size) % directions.size
            updateDirection()
        }

        //month button
        binding.tvMonth.setOnClickListener {
            updatePlanSelection(binding.tvMonth, binding.tvYear, "monthly")
        }

        //year button
        binding.tvYear.setOnClickListener {
            updatePlanSelection(binding.tvYear, binding.tvMonth, "yearly")
        }

        binding.tvAvailableFrom.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                // Do something with the selected date (e.g., set it in a TextView)
                binding.tvAvailableFrom.text = selectedDate
            }
        }

        //BedRoom
        binding.ivPlusBedroom.setOnClickListener {
            if (bedrooms >= 10) {
            } else {
                bedrooms++
                binding.tvNoBedroom.text = bedrooms.toString()
            }
        }

        binding.ivMinusBedroom.setOnClickListener {
            if (bedrooms == 0) {

            } else {
                bedrooms--
                binding.tvNoBedroom.text = bedrooms.toString()
            }
        }


        //Bathroom
        binding.ivPlusBathroom.setOnClickListener {
            if (bathrooms >= 10) {

            } else {
                bathrooms++
                binding.tvNoBathroom.text = bathrooms.toString()
            }


        }

        binding.ivMinusBathroom.setOnClickListener {
            if (bathrooms == 0) {

            } else {
                bathrooms--
                binding.tvNoBathroom.text = bathrooms.toString()
            }
        }

        //Balcony
        binding.ivPlusBalcony.setOnClickListener {
            if (balcony >= 10) {
                showErrorSnackBar("Balcony is full", false)
            } else {
                balcony++
                binding.tvNoBalcony.text = balcony.toString()
            }

        }

        binding.ivMinusBalcony.setOnClickListener {
            if (balcony == 0) {

            } else {
                balcony--
                binding.tvNoBalcony.text = balcony.toString()
            }
        }

        //Kitchen
        binding.ivPlusKitchen.setOnClickListener {
            if (kitchen >= 10) {
                showErrorSnackBar("Kitchen is full", false)
            } else {
                kitchen++
                binding.tvNoKitchen.text = kitchen.toString()
            }
        }

        binding.ivMinusKitchen.setOnClickListener {
            if (kitchen == 0) {
            } else {
                kitchen--
                binding.tvNoKitchen.text = kitchen.toString()
            }
        }

        binding.ivPlusHall.setOnClickListener {
            if (hall >= 10) {
                showErrorSnackBar("Hall is full", false)
            } else {
                hall++
                binding.tvNoHall.text = hall.toString()
            }

        }
        binding.ivMinusHall.setOnClickListener {
            if (hall == 0) {
                showErrorSnackBar("Hall is empty", false)
            } else {
                hall--
                binding.tvNoHall.text = hall.toString()
            }
        }



        binding.tvParkingLot.setOnClickListener {
            if (parkingLot == true) {
                binding.tvParkingLot.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                parkingLot = false
            } else {
                binding.tvParkingLot.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                parkingLot = true
            }
        }

        binding.tvPetAllowed.setOnClickListener {
            if (petAllowed == true) {
                binding.tvPetAllowed.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                petAllowed = false
            } else {
                binding.tvPetAllowed.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                petAllowed = true
            }
        }

        binding.tvGarden.setOnClickListener {
            if (garden == true) {
                binding.tvGarden.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                garden = false
            } else {
                binding.tvGarden.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                garden = true
            }
        }

        binding.tvGym.setOnClickListener {
            if (gym == true) {
                binding.tvGym.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                gym = false
            } else{
                binding.tvGym.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                gym = true
            }
        }

        binding.tvPark.setOnClickListener {
            if (park == true) {
                binding.tvPark.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                park = false
            } else {
                binding.tvPark.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                park = true
            }
        }

        binding.tvFamiliy.setOnClickListener {
            if (familiy == true) {
                binding.tvFamiliy.apply {
                    setBackgroundResource(R.drawable.yearlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                }
                familiy = false
            } else {
                binding.tvFamiliy.apply {
                    setBackgroundResource(R.drawable.monthlybuttoncolor)
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                }
                familiy = true
            }
        }

        binding.finishbtn.setOnClickListener {
            val mySecurity = security
            val myAvailableFrom = binding.tvAvailableFrom.text.toString()
            val myBedroom = binding.tvNoBedroom.text.toString()
            val myBalcony = binding.tvNoBalcony.text.toString()
            val myRentPrice = getRawNumberFromEditText(binding.etRentPrice)
            val rentPrice = binding.etRentPrice.text.toString().trim()
            val cleanedPrice = rentPrice.replace(",", "").replace(" ", "").replace("₹", "")
            val myFacing = binding.tvFashing.text.toString()
            val myHall = binding.tvNoHall.text.toString()
            val myKitchen = binding.tvNoKitchen.text.toString()
            val myDescription = binding.etPropertyDescription.text.toString()
            val area = binding.etPropertyArea.text.toString()
            val builtUpArea = binding.tvBuiltArea.text.toString()

            Log.d("security", mySecurity)
            // Validate inputs
            if (rentPrice.isEmpty()) {
                showErrorSnackBar("Price is required", false)
            } else {
                val priceValue = cleanedPrice.toDoubleOrNull()

                if (priceValue == null) {
                    showErrorSnackBar("Invalid price format", false)
                } else if (priceValue < 500) {
                    showErrorSnackBar("Price should be greater than 500", false)
                }
//                else if (binding.tvAvailableFrom.text.toString() == "Ex : 12/11/2002") {
//                    showErrorSnackBar("Please select date", false)
//                }
                else if (myAvailableFrom.isEmpty()){
                    showErrorSnackBar("Please select date", false)
                }

                else if (area.isEmpty()) {
                    showErrorSnackBar("Area should not be empty", false)
                }
                else if (builtUpArea.isEmpty()) {
                    showErrorSnackBar("Built up area should not be empty", false)
                }

                else if (builtUpArea.toInt()>area.toInt()) {
                    showErrorSnackBar("Area should be greater than Built up area", false)

                }
                else if (mySecurity.isEmpty()){
                    showErrorSnackBar("Security deposit should not be empty", false)
                }
                else if (myBedroom == "0") {
                    showErrorSnackBar("Bedroom should not be empty", false)
                } else if (binding.tvNoBathroom.text.toString() == "0") {
                    showErrorSnackBar("Bathroom should not be empty", false)
                }
                else {
                    // Disable button to prevent multiple clicks
                    binding.finishbtn.isClickable = false

                    // Collect environment variables
                    val myEnvVariables = ArrayList<String>()
                    if (parkingLot) myEnvVariables.add("Parking Lot")
                    if (petAllowed) myEnvVariables.add("Pet Allowed")
                    if (garden) myEnvVariables.add("Garden")
                    if (gym) myEnvVariables.add("Gym")
                    if (park) myEnvVariables.add("Park")
                    if (familiy) myEnvVariables.add("Family Room")

                    // Call the API
                    addPropertyApi(
                        mySecurity = mySecurity,
                        myAvailableFrom = myAvailableFrom,
                        myBedroom = myBedroom,
                        myBalcony = myBalcony,
                        myRentPrice = myRentPrice.toString(),
                        myFacing = myFacing,
                        myHall = myHall,
                        myKitchen = myKitchen,
                        myPlan = plantype,
                        myDescription = myDescription,
                        myEnvVariables = myEnvVariables,
                        areaProperty = area
                    )
                }
            }
        }

        binding.tv15Days.setOnClickListener {
            updateSelection(binding.tv15Days, binding.tv1Month, binding.tv2Month, binding.tvCustom)

            security = "15 Days"  // Update security value
        }

        binding.tv1Month.setOnClickListener {
            updateSelection(binding.tv1Month, binding.tv15Days, binding.tv2Month, binding.tvCustom)
            security = "1 Month"  // Update security value
        }

        binding.tv2Month.setOnClickListener {
            updateSelection(binding.tv2Month, binding.tv15Days, binding.tv1Month, binding.tvCustom)
            security = "2 Month"  // Update security value
        }

//        binding.tvCustom.setOnClickListener {
//            val showCustom = !securityCustom
//            updateSelection(binding.tvCustom, binding.tv15Days, binding.tv1Month, showCustom)
//            security = if (showCustom) security else ""
//        }
//        binding.tvCustom.setOnClickListener {
//            val customSecurity = binding.etSecurityDeposit.text.toString()
//            securityCustom = !securityCustom  // Toggle the custom security flag
//            updateSelection(binding.tvCustom, binding.tv15Days, binding.tv1Month, binding.tv2Month, showCustom = securityCustom)
//
//            // If custom is selected, reset security value to avoid previous selections affecting it
//            security = if (securityCustom) customSecurity else security
//        }

        binding.tvCustom.setOnClickListener {
            securityCustom = !securityCustom  // Toggle the custom security flag

            updateSelection(
                binding.tvCustom,
                binding.tv15Days,
                binding.tv1Month,
                binding.tv2Month,
                showCustom = securityCustom
            )

            // If custom is selected, update security with the entered text, else retain previous selection
            security = if (securityCustom) {
                binding.etSecurityDeposit.text.toString()
            } else {
                ""
            }
        }

// Update security value when text changes in etSecurityDeposit
        binding.etSecurityDeposit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (securityCustom) {
                    security = s.toString().trim()+" "+"Month"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        addPropertyApiResponse()

    }




    private fun updateDirection() {
        binding.tvFashing.text = directions[currentIndex]
    }


    private class CurrencyTextWatcher(private val editText: EditText) : TextWatcher {
        private val decimalFormat: DecimalFormat =
            DecimalFormat.getCurrencyInstance(Locale.getDefault()) as DecimalFormat

        init {
            val symbols: DecimalFormatSymbols = decimalFormat.decimalFormatSymbols
            symbols.currencySymbol = " ₹ "  // Set currency symbol
            decimalFormat.decimalFormatSymbols = symbols
            decimalFormat.isGroupingUsed = true
            decimalFormat.minimumFractionDigits = 0
            decimalFormat.maximumFractionDigits = 0
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) return

            val unformattedString = s.toString().replace(Regex("[^\\d]"), "")
            val amount = unformattedString.toLongOrNull() ?: 0
            val formattedString = decimalFormat.format(amount)

            editText.removeTextChangedListener(this)
            editText.setText(formattedString)
            editText.setSelection(formattedString.length)
            editText.addTextChangedListener(this)
        }
    }


    private fun addPropertyApi(
        mySecurity: String,
        myAvailableFrom: String,
        myBedroom: String,
        myBalcony: String,
        myRentPrice: String,
        myFacing: String,
        myHall: String,
        myKitchen: String,
        myPlan: String,
        myDescription: String,
        myEnvVariables: ArrayList<String>,
        areaProperty: String

    ) {

        loader.show()

        val uid = MySharedPreferences(requireContext()).getToken() ?: ""
        
        Thread {
            // Save base64 images locally
            val localImagePaths = ArrayList<String>()
            args.imgList.img.forEachIndexed { index, base64 ->
                val path = saveBase64ImageToInternalStorage(base64, requireContext(), index)
                if (path.isNotEmpty()) {
                    localImagePaths.add(path)
                }
            }

            // Create local PropertyDetails object
            val localId = "local_${System.currentTimeMillis()}"
            val propertyDetails = PropertyDetails(
                address = args.address,
                address2 = args.address2,
                area = areaProperty,
                avail_from = myAvailableFrom,
                balcony = myBalcony,
                bathroom = myBedroom,
                bedroom = myBedroom,
                category = args.Category,
                city = args.city,
                city_name = args.city,
                phone_code = "+91",
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time),
                env_facilities = myEnvVariables,
                facing = myFacing,
                featured = "0",
                furnished = args.propertyType,
                hall = myHall,
                id = localId,
                kitchen = myKitchen,
                lats = "0.0",
                longs = "0.0",
                phone_number = args.phoneNumber,
                pincode = args.pincode,
                plan_type = myPlan,
                pr_name = args.propertyName,
                price = myRentPrice,
                prop_desc = myDescription,
                property_id = localId,
                property_images = localImagePaths,
                reviews = emptyList(),
                secure_deposit = mySecurity,
                state = args.state,
                state_name = args.state,
                status = "1",
                time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time),
                total_rooms = myBedroom,
                uid = uid,
                avg_rating = 0.0,
                list_type = "",
                trash = "",
                wishlist = 0
            )

            // Save locally
            saveUploadedPropertyLocal(propertyDetails)

            requireActivity().runOnUiThread {
                loader.dismiss()
                showErrorSnackBar("The public will be seeing your property after some time.", true)
                val action = AddListingFragment3Directions.actionAddListingFragment3ToProfileFragment()
                findNavController().navigate(action)
            }
        }.start()

    }

    private fun addPropertyApiResponse() {
        // Obsolete: Now handled manually inside addPropertyApi
    }

    private fun updateSelection(
        selected: TextView,
        vararg others: TextView,
        showCustom: Boolean = false
    ) {
        // Set the selected item appearance
        selected.setBackgroundResource(R.drawable.monthlybuttoncolor)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        // Reset appearance for non-selected items
        others.forEach {
            it.setBackgroundResource(R.drawable.yearlybuttoncolor)
            it.setTextColor(ContextCompat.getColor(requireContext(), R.color.boldTextColor))
        }

        // Handle visibility of custom input field
        binding.llSecurityDeposit.visibility = if (showCustom) View.VISIBLE else View.GONE
        securityCustom = showCustom
    }


    private fun updatePlanSelection(selected: TextView, other: TextView, plan: String) {
        // Set selected plan's appearance
        selected.apply {
            setBackgroundResource(R.drawable.monthlybuttoncolor)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        // Reset the other plan's appearance
        other.apply {
            setBackgroundResource(R.drawable.yearlybuttoncolor)
            setTextColor(ContextCompat.getColor(requireContext(), R.color.boldTextColor))
        }
        // Update plan type
        plantype = plan
    }




}


