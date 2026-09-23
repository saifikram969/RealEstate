package com.btjnonbrokerage.Fragment.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.btjnonbrokerage.databinding.FragmentPublishListDialogBinding

class PublishListDialogFragment(val onFinishClick: () -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPublishListDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPublishListDialogBinding.inflate(layoutInflater)

        binding.bottomFinishButton.setOnClickListener {
            onFinishClick()
            dismiss()
        }





        return binding.root
    }

    override fun dismiss() {
        super.dismiss()
        onFinishClick()
    }


}