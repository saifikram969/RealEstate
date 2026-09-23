package com.btjnonbrokerage.Fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateList
import com.btjnonbrokerage.MVVM.APIModel.editproperty.EditPropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.Location.SelectCityDialogFragment
import com.btjnonbrokerage.DialogBox.Location.StateSelectDialogFragment
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.PropertyImages.EditPropertyImgAdapter
import com.btjnonbrokerage.databinding.FragmentEditPropertyBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class EditPropertyFragment : BaseFragment<FragmentEditPropertyBinding>() {

    val args: EditPropertyFragmentArgs by navArgs()
    private var securityCustom = false
    private var security = ""
    private var propertyType = ""

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val editPropertyImgAdapter by lazy {
        EditPropertyImgAdapter(onCrossClick = {
            updateAdapter(it)
        })
    }

    private val directions = arrayOf("North", "East", "South", "West")
    private var currentIndex = 0
    private var selectedCategory = ""
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

    var totalRoom = ""

    private var selectedState: StateList? = null
    var state: String? = null
    var city: String? = null


    private var plantype = ""
    private var secure_deposit = ""
    var propertyId = ""


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditPropertyBinding = FragmentEditPropertyBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tv15Days.setOnClickListener {
            updateSelection(binding.tv15Days, binding.tv1Month, binding.tv2Month, binding.tvCustom)
            binding.etSecurityDeposit.text.clear()
            security = "15 Days"  // Update security value
        }

        binding.tv1Month.setOnClickListener {
            updateSelection(binding.tv1Month, binding.tv15Days, binding.tv2Month, binding.tvCustom)
            binding.etSecurityDeposit.text.clear()
            security = "1 Month"  // Update security value
        }

        binding.tv2Month.setOnClickListener {
            updateSelection(binding.tv2Month, binding.tv15Days, binding.tv1Month, binding.tvCustom)
            binding.etSecurityDeposit.text.clear()
            security = "2 Month"  // Update security value
        }

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

        authViewModel.getSinglePropertyViewModel(
            GetSinglePropertyRequest(
                args.propertyId,
                args.uid
            )
        )


        // logic to show rupee sign before rent price
        binding.etRentPrice.addTextChangedListener(CurrencyTextWatcher(binding.etRentPrice))
        // after submit button click
        binding.tvSubmit.setOnClickListener {

            checkApiValidations()

        }
        // back button click
        binding.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // delete building logic
        binding.cvToolbarDelete.setOnClickListener {
            mainViewModelBase.deletePropertyViewModel(args.propertyId)
        }

        // change facing direction
        binding.ForwardCard.setOnClickListener {
            currentIndex = (currentIndex + 1) % directions.size
            updateDirection()
        }
        binding.backCard.setOnClickListener {
            currentIndex = (currentIndex - 1 + directions.size) % directions.size
            updateDirection()
        }
        binding.tvAvailableFromDate.setOnClickListener {
            showDatePickerDialog(binding.tvAvailableFromDate)
        }
        setupPropertyTypeSelection()
        getPropertyAPI()
        setAllAdapter()
        editPropertyResponse()
    }


    private fun updateFacilityUI() {
        updateFacilityUI(binding.tvParkingLot, parkingLot)
        updateFacilityUI(binding.tvPetAllowed, petAllowed)
        updateFacilityUI(binding.tvGarden, garden)
        updateFacilityUI(binding.tvGym, gym)
        updateFacilityUI(binding.tvPark, park)
        updateFacilityUI(binding.tvFamiliy, familiy)
    }

    private fun updateFacilityUI(view: TextView, isSelected: Boolean) {
        if (isSelected) {
            view.setBackgroundResource(R.drawable.monthlybuttoncolor)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            view.setBackgroundResource(R.drawable.yearlybuttoncolor)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }


    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val clipData = data?.clipData
                val imageUriList = mutableListOf<Uri>()

                if (clipData != null) {
                    // Multiple images selected
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        imageUriList.add(imageUri)
                    }
                } else {
                    // Single image selected
                    val imageUri = data?.data
                    if (imageUri != null) {
                        imageUriList.add(imageUri)
                    }
                }

                // Process selected images
                processSelectedImages(imageUriList)
            }
        }

    private fun processSelectedImages(imageUris: List<Uri>) {
        loader.show()
        val propertyImages: MutableList<String?> = ArrayList<String?>()
        propertyImages.addAll(editPropertyImgAdapter.currentList)
        for (imageUri in imageUris) {
            propertyImages.add(imageUri.toString())
            editPropertyImgAdapter.submitList(propertyImages)
        }
        loader.dismiss()
    }

    private fun setAllAdapter() {
        binding.rvPropertyImages.apply {
            adapter = editPropertyImgAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun updateAdapter(item: String) {
        val currentList = editPropertyImgAdapter.currentList.toMutableList()
        currentList.remove(item)
        editPropertyImgAdapter.submitList(currentList)
    }

    private fun updateDirection() {
        binding.tvFacing.text = directions[currentIndex]
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

    private fun checkApiValidations() {

        //Environment facility
        val envVariables: MutableList<String> = ArrayList()
        if (parkingLot) {
            envVariables.add("Parking Lot")
        }
        if (petAllowed) {
            envVariables.add("Pet Allowed")
        }
        if (garden) {
            envVariables.add("Garden")
        }
        if (gym) {
            envVariables.add("Gym")
        }
        if (park) {
            envVariables.add("Park")
        }
        if (familiy) {
            envVariables.add("Family Room")
        }
        //images
        val updatedPropertyImages: MutableList<String> = mutableListOf()
        for (i in editPropertyImgAdapter.currentList) {
            if (!i.contains(propertyId)) {
                updatedPropertyImages.add(getStringImage(getCapturedImage(Uri.parse(i))))
            } else {
                updatedPropertyImages.add(i)
            }
        }

        if (editPropertyImgAdapter.currentList.size < 4) {
            showErrorSnackBar("Add at-least 4 images", true)

        } else if (binding.tvNoBedroom.text.toString() == "0" || binding.tvNoBathroom.text.toString() == "0"
            || binding.tvNoBalcony.text.toString() == "0" || binding.tvNoKitchen.text.toString() == "0"
            || binding.tvNoHall.text.toString() == "0"
        ) {
            showErrorSnackBar("Set Quantity at least 1", true)
        }else if (binding.etPropertyArea.text.toString() == "") {
            showErrorSnackBar("Please enter Property Area",false)
        }
        else {
            editPropertyCall(envVariables, updatedPropertyImages)
        }

    }

    private fun editPropertyCall(
        envVariables: MutableList<String>,
        updatedPropertyImages: MutableList<String>
    ) {
        val uid = MySharedPreferences(requireContext()).getToken()
        val editPropertyRequest = EditPropertyRequest(
            uid = uid!!,
            city = city!!,
            pincode = binding.etPincode.text.toString(),
            state = state!!,
            phone_number = binding.etPhone.text.toString(),
            pr_name = binding.etHouseName.text.toString(),
            price = getRawNumberFromEditText(binding.etRentPrice).toString(),
            bedroom = binding.tvNoBedroom.text.toString(),
            bathroom = binding.tvNoBathroom.text.toString(),
            balcony = binding.tvNoBalcony.text.toString(),
            hall = binding.tvNoHall.text.toString(),
            kitchen = binding.tvNoKitchen.text.toString(),
            address = binding.etAddress.text.toString(),
            address2 = binding.etAddress2.text.toString(),
            property_id = args.propertyId,
            category = selectedCategory,
            env_facility = envVariables,
            facing = binding.tvFacing.text.toString(),
            total_room = totalRoom,
            list_type = "rent",
            phone_code = binding.tvCountryCode.text.toString(),
            plan_type = plantype,
            prop_desc = binding.etPropertyDescription.text.toString(),
            pr_img = updatedPropertyImages,
            secure_deposit = security,
            available_from = binding.tvAvailableFromDate.text.toString(),
            furnished = propertyType,
            area = binding.etPropertyArea.text.toString()

        )

        mainViewModel.editPropertyViewModel(editPropertyRequest)
        // getting response after submit button and navigating user back
        mainViewModel.editPropertyLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                Log.d("property","Log Show for Property Update")
                //showErrorSnackBar("Property Updated Successfully", false)
                showErrorSnackBar(it.msg,false)
                findNavController().navigateUp()
                
            }
        }

    }

    private fun editPropertyResponse(){
        mainViewModelBase.deletePropertyLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                showErrorSnackBar("Property Deleted Successfully", false)
                findNavController().navigate(R.id.action_editPropertyFragment_to_profileFragment)
            }else{
                showErrorSnackBar(it.msg, true)
            }
        }
    }
    private fun updateSelection(selected: TextView, vararg others: TextView, showCustom: Boolean = false) {
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

    private fun getPropertyAPI(){
        authViewModel.getSinglePropertyRequestLiveData.observe(viewLifecycleOwner) {
            try {
                secure_deposit = it.property_details.secure_deposit
                binding.tvAvailableFromDate.text = it.property_details.avail_from

               propertyType = it.property_details.furnished

                when(propertyType){
                    "Furnished"->{
                        binding.tvTypeFurnished.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvTypeFurnished.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }

                    "Semi-Furnished"->{
                        binding.tvTypeSemifurnished.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvTypeSemifurnished.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                    "Unfurnished"->{
                        binding.tvTypeUnfurnished.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvTypeUnfurnished.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                    }
                }

                // Check if secure_deposit is a numeric value or a period (like "2 Month")
                if (!(secure_deposit == "15 Days" || secure_deposit == "1 Month" || secure_deposit == "2 Month")) {
                    // Numeric value (₹), update the EditText and backgroundF
                    binding.llSecurityDeposit.visibility = View.VISIBLE
                    binding.etSecurityDeposit.setText(secure_deposit)
                    binding.tvCustom.setBackgroundResource(R.drawable.monthlybuttoncolor)  // Reference to your drawable
                    binding.tvCustom.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )  // Set text color
                    security = secure_deposit
                } else {
                    // Handle if it's in terms of months, e.g., "2 Month"
                    when (secure_deposit) {
                        "15 Days" -> {
                            binding.tv15Days.setBackgroundResource(R.drawable.monthlybuttoncolor)
                            binding.tv15Days.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            security = "15 Days"
                        }

                        "1 Month" -> {
                            binding.tv1Month.setBackgroundResource(R.drawable.monthlybuttoncolor)
                            binding.tv1Month.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            security = "1 Month"
                        }

                        "2 Month" -> {
                            binding.tv2Month.setBackgroundResource(R.drawable.monthlybuttoncolor)
                            binding.tv2Month.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            security = "2 Month"
                        }

                        else -> {
                            // Handle any other cases if necessary
                        }
                    }
                }


                val envFacilities = it.property_details.env_facilities
                // Set facility flags based on the response
                parkingLot = "Parking Lot" in envFacilities
                petAllowed = "Pet Allowed" in envFacilities
                garden = "Garden" in envFacilities
                gym = "Gym" in envFacilities
                park = "Park" in envFacilities
                familiy = "Family Room" in envFacilities

                // Update UI based on the facility flags
                updateFacilityUI()
                propertyId = it.property_details.property_id
                //set house name
                binding.etHouseName.setText(it.property_details.pr_name)
                binding.etPhone.setText(it.property_details.phone_number)
                binding.tvCountryCode.text = it.property_details.phone_code
                //set address and address2
                binding.etAddress.setText(it.property_details.address)
                binding.etAddress2.setText(it.property_details.address2)
                //Locations
                binding.tvState.text = it.property_details.state_name
                binding.tvCity.text = it.property_details.city_name
                binding.etPincode.setText(it.property_details.pincode)
                binding.etPropertyArea.setText(it.property_details.area)
                //Facilities
                binding.tvNoBedroom.text = it.property_details.bedroom
                binding.tvNoBathroom.text = it.property_details.bathroom
                binding.tvNoBalcony.text = it.property_details.balcony
                binding.tvNoHall.text = it.property_details.hall
                binding.tvNoKitchen.text = it.property_details.kitchen
                bedrooms = it.property_details.bedroom.toInt()
                bathrooms = it.property_details.bathroom.toInt()
                balcony = it.property_details.balcony.toInt()
                kitchen = it.property_details.balcony.toInt()
                hall = it.property_details.hall.toInt()
                //property images
                editPropertyImgAdapter.submitList(it.property_details.property_images)
                //property price
                val price = it.property_details.price
//                price = " ₹ $price"
                binding.etRentPrice.setText(price)
                //set property facing
                binding.tvFacing.setText(it.property_details.facing)
                // set property description
                binding.etPropertyDescription.setText(it.property_details.prop_desc)
                // get city state ids
                city = it.property_details.city
                state = it.property_details.state
                // set Category
                when (it.property_details.category) {
                    "House" -> {
                        binding.tvCategoryHouse.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvCategoryHouse.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        selectedCategory = "House"
                    }

                    "Apartment" -> {
                        binding.tvCategoryApartment.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvCategoryApartment.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        selectedCategory = "Apartment"
                    }

                    "Flat" -> {
                        binding.tvCategoryFlat.setBackgroundResource(R.drawable.monthlybuttoncolor)
                        binding.tvCategoryFlat.setTextColor(
                            ContextCompat.getColor(requireContext(),R.color.white))
                        selectedCategory = "Flat"
                    }
                }
                binding.tvCategoryHouse.setOnClickListener {
                    binding.tvCategoryHouse.setBackgroundResource(R.drawable.monthlybuttoncolor)
                    binding.tvCategoryHouse.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.tvCategoryApartment.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryApartment.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.tvCategoryFlat.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryFlat.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    selectedCategory = "House"
                }
                binding.tvCategoryApartment.setOnClickListener {
                    binding.tvCategoryApartment.setBackgroundResource(R.drawable.monthlybuttoncolor)
                    binding.tvCategoryApartment.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.tvCategoryHouse.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryHouse.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.tvCategoryFlat.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryFlat.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    selectedCategory = "Apartment"
                }
                binding.tvCategoryFlat.setOnClickListener {
                    binding.tvCategoryFlat.setBackgroundResource(R.drawable.monthlybuttoncolor)
                    binding.tvCategoryFlat.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    binding.tvCategoryHouse.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryHouse.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    binding.tvCategoryApartment.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvCategoryApartment.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )
                    selectedCategory = "Flat"
                }

                // total rooms bind
                totalRoom = it.property_details.total_rooms.toString()


            } catch (_: Exception) {
                // Handle exceptions
            }

            binding.tvParkingLot.setOnClickListener {
                parkingLot = !parkingLot
                updateFacilityUI()
            }
            binding.tvPetAllowed.setOnClickListener {
                petAllowed = !petAllowed
                updateFacilityUI()
            }
            binding.tvGarden.setOnClickListener {
                garden = !garden
                updateFacilityUI()
            }
            binding.tvGym.setOnClickListener {
                gym = !gym
                updateFacilityUI()
            }
            binding.tvPark.setOnClickListener {
                park = !park
                updateFacilityUI()
            }
            binding.tvFamiliy.setOnClickListener {
                familiy = !familiy
                updateFacilityUI()
            }

            binding.ivPlusBedroom.setOnClickListener {

                if(bedrooms==10){

                }else{
                    bedrooms++
                    binding.tvNoBedroom.text = bedrooms.toString()
                }

            }
            binding.ivMinusBedroom.setOnClickListener {
                if (bedrooms > 0) {
                    bedrooms--
                    binding.tvNoBedroom.text = bedrooms.toString()
                }
            }
            binding.ivPlusBathroom.setOnClickListener {
                if(bathrooms==10){

                }else{
                    bathrooms++
                    binding.tvNoBathroom.text = bathrooms.toString()
                }


            }
            binding.ivMinusBathroom.setOnClickListener {
                if (bathrooms > 0) {
                    bathrooms--
                    binding.tvNoBathroom.text = bathrooms.toString()
                }
            }
            binding.ivPlusBalcony.setOnClickListener {
                if(balcony==10){

                }else{
                    balcony++
                    binding.tvNoBalcony.text = balcony.toString()
                }


            }
            binding.ivMinusBalcony.setOnClickListener {
                if (balcony > 0) {
                    balcony--
                    binding.tvNoBalcony.text = balcony.toString()
                }
            }
            binding.ivPlusKitchen.setOnClickListener {

                if(kitchen==10){

                }else{
                    kitchen++
                    binding.tvNoKitchen.text = kitchen.toString()
                }

            }
            binding.ivMinusKitchen.setOnClickListener {
                if (kitchen > 0) {
                    kitchen--
                    binding.tvNoKitchen.text = kitchen.toString()
                }
            }
            binding.ivPlusHall.setOnClickListener {
                if(hall==10){

                }else{
                    hall++
                    binding.tvNoHall.text = hall.toString()
                }

            }
            binding.ivMinusHall.setOnClickListener {
                if (hall > 0) {
                    hall--
                    binding.tvNoHall.text = hall.toString()
                }
            }
            //end changing quantities
            when (it.property_details.plan_type) {
                "monthly" -> {
                    // Set 'monthly' as selected
                    binding.tvMonth.setBackgroundResource(R.drawable.monthlybuttoncolor)
                    binding.tvMonth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    // Set 'yearly' as unselected
                    binding.tvYear.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvYear.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )

                    plantype = "monthly"
                }

                "yearly" -> {
                    // Set 'yearly' as selected
                    binding.tvYear.setBackgroundResource(R.drawable.monthlybuttoncolor)
                    binding.tvYear.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )

                    // Set 'monthly' as unselected
                    binding.tvMonth.setBackgroundResource(R.drawable.yearlybuttoncolor)
                    binding.tvMonth.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.black
                        )
                    )

                    plantype = "yearly"
                }
            }//gurjeet//



            binding.tvMonth.setOnClickListener {
                // Set 'monthly' as selected
                binding.tvMonth.setBackgroundResource(R.drawable.monthlybuttoncolor)
                binding.tvMonth.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )

                // Set 'yearly' as unselected
                binding.tvYear.setBackgroundResource(R.drawable.yearlybuttoncolor)
                binding.tvYear.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                plantype = "monthly"
            }//gurjeet//
            binding.tvYear.setOnClickListener {
                // Set 'yearly' as selected
                binding.tvYear.setBackgroundResource(R.drawable.monthlybuttoncolor)
                binding.tvYear.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

                // Set 'monthly' as unselected
                binding.tvMonth.setBackgroundResource(R.drawable.yearlybuttoncolor)
                binding.tvMonth.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                plantype = "yearly"
            }


            //Sate `
            binding.llState.setOnClickListener {
                StateSelectDialogFragment(onItemSelected = {
                    if (it != null) {
                        selectedState = it
                        binding.tvState.text = it.name
                        state = it.id
                    }
                }).show(parentFragmentManager, "StateDialogBox")
            }
            //City
            binding.llCity.setOnClickListener {
                if (selectedState != null) {
                    SelectCityDialogFragment(selectedState!!, onCitySelected = {
                        binding.tvCity.text = it.city
                        city = it.id

                    }).show(parentFragmentManager, "CityDialogBox")
                } else {
                    showErrorSnackBar("Please Select State", false)
                }
            }
            binding.cvAddImg.setOnClickListener {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                galleryIntent.putExtra(
                    Intent.EXTRA_ALLOW_MULTIPLE,
                    true
                ) // Allow multiple selection
                galleryActivityResultLauncher.launch(galleryIntent)
            }

        }
    }
    private fun showDatePickerDialog(dateTextView: TextView) {
        activity?.let { context ->
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Create a Calendar instance with the selected date
                    val selectedDateCalendar = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, selectedDay)
                    }

                    // Format the date as "12 Nov 2002"
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDateCalendar.time)

                    // Display the selected date in the TextView
                    dateTextView.text = formattedDate
                },
                year, month, day
            )

            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }
    private fun setupPropertyTypeSelection() {
        binding.tvTypeFurnished.setOnClickListener {
            setPropertyType("Furnished", binding.tvTypeFurnished, binding.tvTypeUnfurnished, binding.tvTypeSemifurnished)
        }
        binding.tvTypeSemifurnished.setOnClickListener {
            setPropertyType("Semi-Furnished", binding.tvTypeSemifurnished, binding.tvTypeFurnished, binding.tvTypeUnfurnished)
        }
        binding.tvTypeUnfurnished.setOnClickListener {
            setPropertyType("Unfurnished", binding.tvTypeUnfurnished, binding.tvTypeFurnished, binding.tvTypeSemifurnished)
        }
    }

    private fun setPropertyType(type: String, selectedView: TextView, vararg otherViews: TextView) {
        propertyType = type
        selectedView.setBackgroundResource(R.drawable.monthlybuttoncolor)
        selectedView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        otherViews.forEach { view ->
            view.setBackgroundResource(R.drawable.yearlybuttoncolor)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.boldTextColor))
        }
    }

}