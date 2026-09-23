package com.btjnonbrokerage.Fragment.AddProperty

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.DialogBox.Location.SelectCityDialogFragment
import com.btjnonbrokerage.DialogBox.Location.StateSelectDialogFragment
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateList
import com.btjnonbrokerage.MVVM.APIModel.PinCode.PinCodeRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentAddListingAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddListingAddressFragment : BaseFragment<FragmentAddListingAddressBinding>() {

    private var selectedState: StateList? = null
    private var countryCodeWithPlus = "+91"

    val args: AddListingAddressFragmentArgs by navArgs()
    var state: String? = null
    var city: String? = null
    var pincodes: List<String> = listOf()

    @Inject
    lateinit var viewModel: MainViewModel

    companion object {
        const val PINCODE_LENGTH = 6
        const val PHONE_LENGTH = 10
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddListingAddressBinding = FragmentAddListingAddressBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        setupStateSelector()
        setupCitySelector()
        setupPinCodeWatcher()
        setupNextButton()
        setupBackButton()
        keyBoardListener()
    }

    private fun setupStateSelector() {
        binding.llState.setOnClickListener {
            StateSelectDialogFragment(onItemSelected = { state ->
                state?.let {
                    selectedState = it
                    binding.tvState.text = it.name
                    this.state = it.id
                }
            }).show(parentFragmentManager, "StateDialogBox")
        }
    }

    private fun setupCitySelector() {
        binding.llCity.setOnClickListener {
            selectedState?.let {
                SelectCityDialogFragment(it, onCitySelected = { city ->
                    binding.tvCity.text = city.city
                    this.city = city.id
                    viewModel.fetchPincode(PinCodeRequest(city.city))
                }).show(parentFragmentManager, "CityDialogBox")
            } ?: showErrorSnackBar("Please Select State", false)
        }

        viewModel.fetchPincodeLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                pincodes = it.pincodes
            } else {
                Log.e("PincodeError", "Failed to fetch pincodes")
                showErrorSnackBar("Invalid", true)
            }
        }
    }

    private fun setupPinCodeWatcher() {
        binding.etPincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etPincode.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }

            override fun afterTextChanged(s: Editable?) {
                // Allow any 6 digit pincode for demo
            }
        })
    }

    private fun setupNextButton() {
        binding.inNextBtn.nextBtn.setOnClickListener {
            validateAndNavigate()
        }
    }

    private fun validateAndNavigate() {
        val phone = binding.etPhone.text.toString()
        val address = binding.etAddress.text.toString()
        val pincode = binding.etPincode.text.toString()
        val address2 = binding.etAddress2.text.toString()

        when {
            phone.isEmpty() -> showErrorSnackBar("Enter Phone Number", false)
            phone.length != PHONE_LENGTH -> showErrorSnackBar("Enter Valid Number", false)
            address.isEmpty() -> showErrorSnackBar("Enter Address", false)
            state.isNullOrEmpty() -> showErrorSnackBar("Select State", false)
            city.isNullOrEmpty() -> showErrorSnackBar("Select City", false)
            pincode.isEmpty() -> showErrorSnackBar("Enter Pincode", false)
            pincode.length != PINCODE_LENGTH -> showErrorSnackBar("Enter valid pincode", false)
            else -> navigateToNextFragment(phone, address, pincode, address2)
        }
    }

    private fun navigateToNextFragment(phone: String, address: String, pincode: String, address2: String) {
        val action = AddListingAddressFragmentDirections.actionAddListingAddressFragmentToAddListing2Fragment(
            args.houseName,
            args.Category,
            countryCodeWithPlus,
            phone,
            address,
            state.toString(),
            city.toString(),
            pincode,
            address2,
            args.propertyDescription,
            args.propertyType
        )
        try {
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("NavigationError", "Navigation failed: ${e.message}")
        }
    }

    private fun setupBackButton() {
        binding.inNextBtn.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun keyBoardListener() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            binding.root.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.root.height
            val keypadHeight = screenHeight - rect.bottom

            // Toggle button visibility based on keyboard presence
            binding.llNextBtn.visibility = if (keypadHeight > screenHeight * 0.15) View.GONE else View.VISIBLE
        }
    }
}
