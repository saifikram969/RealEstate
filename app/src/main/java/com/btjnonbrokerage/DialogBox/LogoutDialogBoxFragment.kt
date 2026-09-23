package com.btjnonbrokerage.DialogBox

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.btjnonbrokerage.databinding.FragmentLogoutDialogBoxBinding


class LogoutDialogBoxFragment(val onClickLogout: () -> Unit) : DialogFragment() {


    private lateinit var binding: FragmentLogoutDialogBoxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogoutDialogBoxBinding.inflate(layoutInflater)

        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvLogout.setOnClickListener {
            dismiss()
            onClickLogout()
        }





        return binding.root

    }


    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}