package com.btjnonbrokerage.DialogBox.Location

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityList
import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityRequest
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateList
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.adapters.dialogadapter.CityAdapter
import com.btjnonbrokerage.databinding.FragmentSelectCityDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectCityDialogFragment(private val selectedSate: StateList, val onCitySelected: (CityList) -> Unit) : DialogFragment() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var binding: FragmentSelectCityDialogBinding
    private  var list : List<CityList> =  ArrayList()

    private val cityAdapter by lazy {
        CityAdapter(onItemClicked = {
            onCitySelected(it)
            dismiss()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSelectCityDialogBinding.inflate(inflater, container, false)
        mainViewModel.getCityViewModel(CityRequest(selectedSate.id))

        binding.etSearchCity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterState(p0.toString().trim())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        setAdapter()
        cityApiResponse()
        return binding.root
    }

    private fun setAdapter(){
        binding.rvCitySelect.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cityAdapter
        }
    }
    private fun cityApiResponse(){
        mainViewModel.getCityLiveData.observe(viewLifecycleOwner){
            cityAdapter.submitList(it.cities)
            list = it.cities
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(500, 700)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun filterState(searchText: String) {
        val filteredList = list.filter {
            it.city.startsWith(searchText.uppercase(), ignoreCase = true)
        }
      cityAdapter.submitList(filteredList)
    }




}