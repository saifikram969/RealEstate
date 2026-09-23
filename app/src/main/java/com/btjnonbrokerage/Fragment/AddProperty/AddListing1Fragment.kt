package com.btjnonbrokerage.Fragment.AddProperty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentAddListing1Binding

class AddListing1Fragment : BaseFragment<FragmentAddListing1Binding>() {
    private lateinit var arrayCategoryCards: Array<TextView>
    private var selected = false
    private var selectedCategory = ""
    private var propertyType = ""
    private var categorySelected = "House"

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddListing1Binding = FragmentAddListing1Binding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setUserName()
        setupCategorySelection()
        setupPropertyTypeSelection()
        setupNextButton()
        setupKeyboardListener()

        binding.inNextBtn.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupToolbar() {
        binding.toolbarAddListing1.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
       // binding.toolbarAddListing1.toolbarTitle.text = getString(R.string.add_property)
    }

    private fun setUserName() {
        val userName = MySharedPreferences(requireContext()).getUserName()
        val capitalizedUserName = userName?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } ?: ""
        binding.tvUserName.text = "$capitalizedUserName,"
    }

    private fun setupCategorySelection() {
        arrayCategoryCards = arrayOf(
            binding.tvCategoryHouse, binding.tvCategoryApartment, binding.tvCategoryFlat
        )

        binding.tvCategoryHouse.setOnClickListener {
            handleCategorySelection(it as TextView, "House")
        }

        binding.tvCategoryApartment.setOnClickListener {
            handleCategorySelection(it as TextView, "Apartment")
        }
        binding.tvCategoryFlat.setOnClickListener {
            handleCategorySelection(it as TextView, "Flat")
        }
    }

    private fun handleCategorySelection(view: TextView, category: String) {
        hideKeyboard()
        selectOneCategory(view)
        selected = true
        selectedCategory = category
    }

    private fun setupPropertyTypeSelection() {
        binding.tvTypeFurnished.setOnClickListener {
            setPropertyType("Furnished", binding.tvTypeFurnished, binding.tvTypeUnfurnished, binding.tvTypeSemifurnished)
        }
        binding.tvTypeSemifurnished.setOnClickListener {
            setPropertyType("Semi-Furnished", binding.tvTypeSemifurnished, binding.tvTypeFurnished, binding.tvTypeUnfurnished)
        }
        binding.tvTypeUnfurnished.setOnClickListener {
            setPropertyType("Unfurnished", binding.tvTypeUnfurnished, binding.tvTypeFurnished, binding.tvTypeSemifurnished)
        }
    }

    private fun setPropertyType(type: String, selectedView: TextView, vararg otherViews: TextView) {
        propertyType = type
        selectedView.setBackgroundResource(R.drawable.monthlybuttoncolor)
        selectedView.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

        otherViews.forEach { view ->
            view.setBackgroundResource(R.drawable.yearlybuttoncolor)
            view.setTextColor(ContextCompat.getColor(requireContext(), R.color.boldTextColor))
        }
    }

    private fun setupNextButton() {
        binding.inNextBtn.nextBtn.setOnClickListener {
            val propertyName = binding.etPropertyName.text.toString()

            when {
                propertyName.isEmpty() -> showErrorSnackBar("Enter Property Name", false)
                !selected -> showErrorSnackBar("Select Property Category", false)
                propertyType.isEmpty() -> showErrorSnackBar("Select Property Type", false)
                else -> {
                    val action = AddListing1FragmentDirections
                        .actionAddListing1FragmentToAddListingAddressFragment(propertyName, selectedCategory, "", propertyType)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun selectOneCategory(selectedView: TextView) {
        arrayCategoryCards.forEach { view ->
            if (view == selectedView) {
                view.setBackgroundResource(R.drawable.monthlybuttoncolor)
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                categorySelected = selectedView.text.toString()
            } else {
                view.setBackgroundResource(R.drawable.yearlybuttoncolor)
                view.setTextColor(ContextCompat.getColor(requireContext(), R.color.boldTextColor))
            }
        }
    }

    private fun setupKeyboardListener() {
        handleKeyBoard.startKeyboardListener(binding.root) {
            binding.llNextBtn.visibility = if (it) View.GONE else View.VISIBLE
        }
    }
}
