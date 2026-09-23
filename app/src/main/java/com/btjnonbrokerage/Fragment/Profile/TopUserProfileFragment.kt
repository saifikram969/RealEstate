package com.btjnonbrokerage.Fragment.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.MVVM.APIModel.Profile.GetProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.ViewPropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.UserPropertyAdapter
import com.btjnonbrokerage.databinding.FragmentTopUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TopUserProfileFragment : BaseFragment<FragmentTopUserProfileBinding>() {

    val args: TopUserProfileFragmentArgs by navArgs()

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel


    private val userPropertyAdapter by lazy {
        UserPropertyAdapter(
            onItemClicked = {
                val action =
                    TopUserProfileFragmentDirections.actionTopUserProfileFragmentToDetailFullFragment(it.property_id)
                findNavController().navigate(action)
            }, clickOnLike = {
                addToWishListBaseAPI(it)
            }, clickOnUnLike = {
                removeToWishListBaseAPI(it.property_id)
            }, notVerified = {
                showErrorSnackBar("Property is not verified, please contact admin", true)
            },onEditIconClicked = { property ->  // Handle edit icon click
               // findNavController().navigate(R.id.action_profileFragment_to_editPropertyFragment)
                Toast.makeText(requireContext(), "bcbacabcja", Toast.LENGTH_SHORT).show()
            }
        )
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTopUserProfileBinding = FragmentTopUserProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userUid = args.Uid
        authViewModel.getProfile(GetProfileRequest(userUid))
        mainViewModel.getUserPropertyViewModel(ViewPropertyRequest(args.Uid))


        binding.toolbarTopProfile.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        authViewModel.getProfileLiveData.observe(viewLifecycleOwner) {
            Glide.with(requireContext()).load(it.user.profileimg).placeholder(R.drawable.dealer_img).into(binding.ivDealerImg)
            binding.tvDealerName.text = uppercaseLowerCase(it.user.fullname)
            binding.tvDealerPhone.text = it.user.phone_code + " " + it.user.mobile
        }

        mainViewModel.viewPropertyLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                userPropertyAdapter.submitList(it.properties)
                binding.tvDealerTotal.text = it.properties.size.toString()
                binding.tvproperties.text = it.properties.size.toString()
                binding.topUserpageSimmer.visibility = View.GONE
                binding.rvTopUserProperties.visibility = View.VISIBLE
            }
        }

        setPropertyAdapter()
    }

    private fun setPropertyAdapter() {
        binding.rvTopUserProperties.apply {
            adapter = userPropertyAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }
}