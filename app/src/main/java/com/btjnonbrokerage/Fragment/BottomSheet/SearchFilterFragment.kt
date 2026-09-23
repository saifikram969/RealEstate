package com.btjnonbrokerage.Fragment.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentSearchFilterBinding

class SearchFilterFragment(val clickOnFilter : (String) -> Unit) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSearchFilterBinding

    private var selected = false
    lateinit var arrayCategoryCards: Array<TextView>
    private var selectedCategory = ""
    var categorySelecteded: String = "House"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchFilterBinding.inflate(layoutInflater, container, false)

        arrayCategoryCards = arrayOf(
            binding.tvCategoryHouse, binding.tvCategoryApartment, binding.tvCategoryHotel,
            binding.tvCategoryVilla, binding.tvCategoryCottage)

        binding.tvCategoryHouse.setOnClickListener {
            selectOneCategory(it as TextView)
            selected = true
            selectedCategory = "House"
        }

        binding.tvCategoryApartment.setOnClickListener {
            selectOneCategory(it as TextView)
            selected = true
            selectedCategory = "Apartment"
        }

        binding.tvCategoryHotel.setOnClickListener {
            selectOneCategory(it as TextView)
            selected = true
            selectedCategory = "Hotel"
        }

        binding.tvCategoryVilla.setOnClickListener {
            selectOneCategory(it as TextView)
            selected = true
            selectedCategory = "Villa"
        }

        binding.tvCategoryCottage.setOnClickListener {
            selectOneCategory(it as TextView)
            selected = true
            selectedCategory = "Cottage"
        }

        binding.tvFilter.setOnClickListener {
            clickOnFilter(categorySelecteded)
            dismiss()
        }

        return binding.root
    }

    private fun selectOneCategory(safe: TextView) {
        for (i in 0 until arrayCategoryCards.size) {
            if (safe == arrayCategoryCards[i]) {
                arrayCategoryCards[i].setBackgroundResource(R.drawable.monthlybuttoncolor)
                arrayCategoryCards[i].setTextColor(resources.getColor(R.color.white))
                categorySelecteded = safe.text.toString()
            } else {
                arrayCategoryCards[i].setBackgroundResource(R.drawable.yearlybuttoncolor)
                arrayCategoryCards[i].setTextColor(resources.getColor(R.color.boldTextColor))
            }
        }
    }

}