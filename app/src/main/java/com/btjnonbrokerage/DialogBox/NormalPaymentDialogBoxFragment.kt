package com.btjnonbrokerage.DialogBox

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentNormalPaymentDialogBoxBinding


class NormalPaymentDialogBoxFragment(val selectOrNot: (Boolean) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentNormalPaymentDialogBoxBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNormalPaymentDialogBoxBinding.inflate(layoutInflater, container, false)






        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


}