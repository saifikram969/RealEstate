package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.ALLFeaturedRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.adapters.FeaturedSearchAdapter
import com.btjnonbrokerage.databinding.FragmentFeaturedListFeaturedEstateBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeaturedListFeaturedEstateFragment :
    BaseFragment<FragmentFeaturedListFeaturedEstateBinding>() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val featuredAdapter by lazy {
        FeaturedSearchAdapter(onclick = {
            val action = FeaturedListFeaturedEstateFragmentDirections.actionFeaturedListFeaturedEstateFragment2ToDetailFullFragment(it.property_id)
            findNavController().navigate(action)
        }, clickOnLike = {
            addToWishListBaseAPI(it)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeaturedListFeaturedEstateBinding =
        FragmentFeaturedListFeaturedEstateBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.commonToolbar.toolbarBack.setOnClickListener{
            findNavController().navigateUp()
        }
        searchApiCall("")

        binding.etSearchBarFeatured.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchApiCall(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })





        setAdapter()
        featuredAPIResponse()

    }


    private fun setAdapter() {
        binding.rvFeaturedSearch.apply {
            adapter = featuredAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun featuredAPIResponse() {
        mainViewModel.allFeaturedLiveData.observe(viewLifecycleOwner) {

            if (it.status.equals("success")) {
                featuredAdapter.submitList(it.featured_property.properties)
            }


        }
    }

    private fun searchApiCall(text: String) {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            mainViewModel.getAllFeatured(ALLFeaturedRequest("1", text, uid))
        }
    }


}