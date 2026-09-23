package com.btjnonbrokerage.Fragment.Profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.DialogBox.LogoutDialogBoxFragment
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileResponse
import com.btjnonbrokerage.MVVM.APIModel.Profile.User
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.UserPropertyAdapter
import com.btjnonbrokerage.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupListeners()
        observeViewModel()
        fetchUserProfile()
        fetchUserProperties()
    }

    private fun setupToolbar() {
        binding.toolbarProfile.toolbarTitle.text = "Profile"
        binding.toolbarProfile.toolbarBack.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.ivLogout.setOnClickListener { showLogoutDialog() }
        binding.tvMyPayment.setOnClickListener { navigateToMyPayment() }
        binding.tvTermsCondition.setOnClickListener { navigateToWebView("https://tsofttech.co/non_brokerage/term-conditions.php") }
        binding.tvEditProfile.setOnClickListener { navigateToEditProfile() }
        binding.tvMyProperty.setOnClickListener { navigateToUserProperties() }
        binding.addPropertiesBt.setOnClickListener { navigateToAddListing() }
        binding.tvChatWithAdmin.setOnClickListener { navigateToChatWithAdmin() }
        binding.tvPrivacyPolicy.setOnClickListener { navigateToWebView("https://btjnonbrokerage.com/privacy-policy.php") }

    }


    private fun observeViewModel() {
        authViewModel.getProfileLiveData.observe(viewLifecycleOwner) { response ->
            if (response.status == "success") {
                val existingImage = MySharedPreferences(requireContext()).getUserImage()
                val isLocalImage = existingImage?.startsWith("/") == true || existingImage?.startsWith("file://") == true
                val finalImage = if (isLocalImage) existingImage else response.user.profileimg
                
                setUserProfile(response.user, response.reviewCount.toString(), response.propertyCount.toString(), finalImage)
                saveProfile(response.user, finalImage)
            } else {
                showErrorSnackBar("Failed to load profile", false)
            }
        }


    }

    private fun fetchUserProfile() {
        val token = MySharedPreferences(requireContext()).getToken()
        token?.let {
            authViewModel.getProfile(GetProfileRequest(it))
        }
    }

    private fun fetchUserProperties() {
        val token = MySharedPreferences(requireContext()).getToken()
        token?.let {
            mainViewModel.getUserPropertyViewModel(ViewPropertyRequest(it))
        }
    }

    private fun showLogoutDialog() {
        LogoutDialogBoxFragment { handleLogout() }.show(
            childFragmentManager,
            "LogoutDialogBoxFragment"
        )
    }

    private fun handleLogout() {
        MySharedPreferences(requireContext()).clearSharedPreferences()
        findNavController().navigate(R.id.action_profileFragment_to_logInFormFragment)
    }

    private fun navigateToDetail(propertyId: String) {
        val action = ProfileFragmentDirections.actionProfileFragmentToDetailFullFragment(propertyId)
        findNavController().navigate(action)
    }

    private fun navigateToMyPayment() {
        val action = ProfileFragmentDirections.actionProfileFragmentToMyPaymentFragment()
        findNavController().navigate(action)
    }

    private fun navigateToWebView(webUrl:String) {
        findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWebViewFragment(webUrl))
    }

    private fun navigateToEditProfile() {
        findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
    }

    private fun navigateToUserProperties() {
        findNavController().navigate(R.id.action_profileFragment_to_userMyPropertyFragment)
    }

    private fun navigateToAddListing() {
        val action = ProfileFragmentDirections.actionProfileFragmentToAddListing1Fragment()
        findNavController().navigate(action)
    }

    private fun navigateToChatWithAdmin(){
        val action = ProfileFragmentDirections.actionProfileFragmentToChatWithAdminFragment()
        findNavController().navigate(action)
    }

    private fun setUserProfile(user: User ,reviewCount : String, propertyCount: String, finalImage: String?) {
        binding.tvUserName.text = user.fullname.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        binding.tvUserPhone.text = "+91 "+user.mobile.toString()
        Glide.with(requireContext()).load(finalImage).placeholder(R.drawable.user_avatar)
            .into(binding.profileImage)
        binding.reviewPoint.text = reviewCount
        binding.propertyPoint.text = propertyCount
    }
    private fun saveProfile(user : User, finalImage: String?){
        MySharedPreferences(requireContext()).setUser(user.fullname,user.mobile.toString(),finalImage)
    }
}
