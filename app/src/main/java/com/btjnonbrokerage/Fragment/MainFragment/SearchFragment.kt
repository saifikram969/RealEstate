package com.btjnonbrokerage.Fragment.MainFragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.Fragment.BottomSheet.SearchFilterFragment
import com.btjnonbrokerage.MVVM.APIModel.Search.SearchRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.searchItemAdapter
import com.btjnonbrokerage.databinding.FragmentSearchBinding
import com.google.android.material.slider.RangeSlider
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var mainViewModel: MainViewModel
    private lateinit var selectedBhkTextView: TextView
    private lateinit var selectedAvailability: TextView
    private val searchAdapter by lazy {
        searchItemAdapter(onclick = {
            val action =
                SearchFragmentDirections.actionSearchFragmentToDetailFullFragment(it.property_id)
            findNavController().navigate(action)

        }, clickOnLike = {
            addToWishListBaseAPI(it)
        }, clickOnUnLike = {
            removeToWishListBaseAPI(it.property_id)
        }
        )
    }
    private val speechRecognizerLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK && result.data != null) {
                val speechText =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
                binding.etSearchBar.setText(speechText)
            }
        }

    private val args: SearchFragmentArgs by navArgs()

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedBhk()
        availability()
        rangeSeekBar()

        binding.ivMicrophone.setOnClickListener {
            startVoiceInput()
        }

        //back
        binding.toolbarBack1.setOnClickListener {
            findNavController().navigateUp()
        }

        searchApiCall("", "")
        setAdapter()
        try {
            if (args.searchFilter.equals("")) {

            } else {
                binding.etSearchBar.setText(args.searchFilter + "")
                searchApiCall("", args.searchFilter.toString().trim())
            }

        } catch (e: Exception) {


        }

        binding.etSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchApiCall("", p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.cvSearchFilter.setOnClickListener {
            SearchFilterFragment(clickOnFilter = {
                searchApiCall(it, binding.etSearchBar.text.toString())
            }).show(childFragmentManager, "")
        }
        searchAPIResponse()

    }

    private fun setAdapter() {
        binding.rvSearchResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResult.adapter = searchAdapter
    }

    private fun searchApiCall(category: String, text: String) {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            mainViewModel.searchViewModel(SearchRequest(category, text, "1", uid, "1", "999999999"))
        }
    }

    private fun searchAPIResponse() {
        mainViewModel.searchLiveData.observe(viewLifecycleOwner) {
            if (it.status.equals("success")) {
//                searchAdapter.currentList.clear()
                searchAdapter.submitList(it.search_location_property.properties)
            } else {

            }


        }
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")

        try {
            speechRecognizerLauncher.launch(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Speech recognition not supported!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun selectedBhk() {
        val bhkOptions = listOf(
            binding.tv1rk,
            binding.tv1bhk,
            binding.tv2bhk,
            binding.tv3bhk,
            binding.tv4bhk,
            binding.tv4bhkPlus
        ) // List of all options

        bhkOptions.forEach { textView ->
            textView.setOnClickListener {
                updateBhkSelection(textView, bhkOptions)
            }
        }
    }

    private fun availability() {
        var availabilityOption = listOf(
            binding.tvImmediate,
            binding.tvWithin15Days,
            binding.tvWithin30Days,
            binding.tvAfter30Days
        )

        availabilityOption.forEach { textView ->
            textView.setOnClickListener {
                updateAvailabilitySelction(textView, availabilityOption)
            }

        }
    }

    private fun updateBhkSelection(selected: TextView, allOptions: List<TextView>) {

        allOptions.forEach { textView ->
            textView.apply {
                setBackgroundResource(R.drawable.background_myedit_text) // Default background
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.boldTextColor
                    )
                ) // Default color
            }
        }

        selected.apply {
            setBackgroundResource(R.drawable.background_text) // Selected background
            setTextColor(ContextCompat.getColor(context, R.color.white)) // Selected color
        }

        // Update the selected option
        selectedBhkTextView = selected
    }

    private fun updateAvailabilitySelction(selected: TextView, allOptions: List<TextView>) {
        allOptions.forEach { textView ->
            textView.apply {
                setBackgroundResource(R.drawable.background_myedit_text) // Default background
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.boldTextColor
                    )
                ) // Default color
            }
        }

        selected.apply {
            setBackgroundResource(R.drawable.background_text) // Selected background
            setTextColor(ContextCompat.getColor(context, R.color.white)) // Selected color
        }
        selectedAvailability = selected
    }

    private fun rangeSeekBar() {
        binding.sliderRangeSlider.apply {
            setTrackActiveTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.Darkblue)))
            setTrackInactiveTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.grayRealEstate)))
            setThumbTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.Darkblue)))
            setHaloTintList(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.Darkblue)))
            binding.sliderRangeSlider.setValues(0f,500000f)
        }
        // Indian currency formatter
        val formatter = NumberFormat.getInstance(Locale("en", "IN"))

        // Listener to update TextViews and thumb labels
        binding.sliderRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            val min = values[0].toInt()
            val max = values[1].toInt()

            // Format values with commas
            val minFormatted = formatter.format(min)
            val maxFormatted = formatter.format(max)

            // Update TextViews
            binding.tvMinimumAmount.text = minFormatted
            binding.tvMaximumAmount.text = maxFormatted

            // Show formatted values on thumbs
            slider.setCustomThumbLabels(minFormatted, maxFormatted)
        }

    }

    // Extension function to show formatted values on thumbs
    private fun RangeSlider.setCustomThumbLabels(minLabel: String, maxLabel: String) {
        this.setLabelFormatter { value ->
            NumberFormat.getInstance(Locale("en", "IN")).format(value.toInt())
        }
    }

}