package com.btjnonbrokerage.DialogBox

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.btjnonbrokerage.databinding.FragmentBhkTypeDialogBoxBinding


class BhkTypeDialogBoxFragment(private val onBhkSelected: (String) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentBhkTypeDialogBoxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBhkTypeDialogBoxBinding.inflate(inflater, container, false)

        // Handle BHK type selection
        binding.tvBhk.setOnClickListener {
            onBhkSelected("1 RK")
            dismiss() // Close the dialog
        }
        binding.tvBhk1.setOnClickListener {
            onBhkSelected("BHK 1")
            dismiss() // Close the dialog
        }
        binding.tvBhk2.setOnClickListener {
            onBhkSelected("BHK 2")
            dismiss() // Close the dialog
        }
        binding.tvBhk3.setOnClickListener {
            onBhkSelected("BHK 3")
            dismiss() // Close the dialog
        }
        binding.tvBhk4.setOnClickListener {
            onBhkSelected("BHK 4")
            dismiss() // Close the dialog
        }
        binding.tvBhk5.setOnClickListener {
            onBhkSelected("BHK 5")
            dismiss() // Close the dialog
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(800, 900)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
