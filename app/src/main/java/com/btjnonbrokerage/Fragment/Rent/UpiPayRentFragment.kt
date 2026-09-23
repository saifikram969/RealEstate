package com.btjnonbrokerage.Fragment.Rent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.databinding.FragmentUpiPayRentBinding


class UpiPayRentFragment : BaseFragment<FragmentUpiPayRentBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUpiPayRentBinding  = FragmentUpiPayRentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}