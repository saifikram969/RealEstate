package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopLocationsRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.adapters.LocationsAdapter
import com.btjnonbrokerage.databinding.FragmentTopLocationsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopLocationsFragment : BaseFragment<FragmentTopLocationsBinding>() {

    @Inject
    lateinit var viewModel: MainViewModel

    private val topLocationAdapter by lazy {
        LocationsAdapter(onClick = {
            val action = TopLocationsFragmentDirections.actionTopLocationsFragmentToSearchFragment(it.state_data.name)
            findNavController().navigate(action)
        })
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTopLocationsBinding = FragmentTopLocationsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //toolbar
        binding.toolbarTopLocations.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbarTopLocations.toolbarTitle.text = ""
        setLocationAdapter()
        viewModel.getTopLocations(TopLocationsRequest("1"))

        viewModel.topLocationsLiveData.observe(viewLifecycleOwner) {
            topLocationAdapter.submitList(it.top_states)
        }



    }

    private fun setLocationAdapter() {
        binding.rvLocations.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLocations.adapter = topLocationAdapter
    }


}