package com.btjnonbrokerage.Fragment.addlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.databinding.FragmentListingLocationBinding

class ListingLocationFragment : BaseFragment<FragmentListingLocationBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListingLocationBinding = FragmentListingLocationBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        binding.selectState.setOnClickListener {
//            StateSelectDialogFragment().show(parentFragmentManager,"state-select")
//        }

    }
}