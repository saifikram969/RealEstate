package com.btjnonbrokerage.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.Fragment.Profile.ProfileFragmentDirections
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyRequest
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.Property as ViewProperty
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.UserPropertyAdapter
import com.btjnonbrokerage.databinding.FragmentUserMyPropertyBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class UserMyPropertyFragment : BaseFragment<FragmentUserMyPropertyBinding>() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val userPropertyAdapter by lazy {
        UserPropertyAdapter(onItemClicked = {
            val action = UserMyPropertyFragmentDirections.actionUserMyPropertyFragmentToDetailFullFragment(it.property_id)
            findNavController().navigate(action)
        }, clickOnLike = {
            addToWishListBaseAPI(it)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        }, notVerified = {
            showErrorSnackBar("Property is not verified, please contact admin", true)
        }, onEditIconClicked = { property ->  // Handle edit icon click

        }

        )
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserMyPropertyBinding = FragmentUserMyPropertyBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.llEmptyProperty.setOnClickListener {
        findNavController().navigate(R.id.action_userMyPropertyFragment_to_addListing1Fragment)
        }


        binding.imgBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        getUserProfile()
        setPropertyAdapter()
        getUserProperty()
        propertyAPICall()

    }

    private fun getUserProfile() {
        val token = MySharedPreferences(requireContext()).getToken()
        if (token != null) {
            authViewModel.getProfile(GetProfileRequest(token))
        }
        authViewModel.getProfileLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                binding.tvTotal.text = it.propertyCount.toString()
            }
        }
    }

    private fun setPropertyAdapter() {
        binding.rvProfileProperties.apply {
            adapter = userPropertyAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun propertyAPICall() {
        val token = MySharedPreferences(requireContext()).getToken()
        if (token != null) {
            mainViewModel.getUserPropertyViewModel(ViewPropertyRequest(token.toString()))
        }
    }
    private fun getUserProperty() {
        mainViewModel.viewPropertyLiveData.observe(viewLifecycleOwner) { response ->
            val localProps = MySharedPreferences(requireContext()).getUploadedPropertiesList().map {
                ViewProperty(
                    address = it.property_details.address ?: "",
                    bathroom = it.property_details.bathroom ?: "",
                    bedroom = it.property_details.bedroom ?: "",
                    category = it.property_details.category ?: "",
                    city = it.property_details.city ?: "",
                    date = it.property_details.date ?: "",
                    featured = it.property_details.featured ?: "",
                    id = it.property_details.id ?: "",
                    lats = it.property_details.lats ?: "",
                    list_type = "",
                    longs = it.property_details.longs ?: "",
                    pr_name = it.property_details.pr_name ?: "",
                    price = it.property_details.price ?: "",
                    prop_desc = it.property_details.prop_desc ?: "",
                    property_id = it.property_details.property_id ?: "",
                    state = it.property_details.state ?: "",
                    status = it.property_details.status ?: "",
                    time = it.property_details.time ?: "",
                    total_rooms = it.property_details.total_rooms ?: "",
                    trash = "",
                    uid = it.property_details.uid ?: "",
                    city_name = it.property_details.city_name ?: "",
                    state_name = it.property_details.state_name ?: "",
                    avg_rating = 0.0f,
                    plan_type = it.property_details.plan_type ?: "",
                    wishlist = if (isFavoritedLocal(it.property_details.property_id ?: "")) 1 else 0,
                    env_facilities = it.property_details.env_facilities ?: emptyList(),
                    property_images = it.property_details.property_images ?: emptyList()
                )
            }

            val combined = mutableListOf<ViewProperty>()
            combined.addAll(localProps)
            if (response.status == "success") {
                combined.addAll(response.properties)
            }

            if (combined.isNotEmpty()) {
                userPropertyAdapter.submitList(combined)
                binding.userProfilePageSimmer.visibility = View.GONE
                binding.rvProfileProperties.visibility = View.VISIBLE
            } else {
                binding.userProfilePageSimmer.visibility = View.GONE
                binding.llEmptyProperty.visibility = View.VISIBLE
            }
        }
    }
}