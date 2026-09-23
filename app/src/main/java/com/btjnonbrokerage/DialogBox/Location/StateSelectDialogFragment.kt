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
import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateList
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.adapters.dialogadapter.StateAdapter
import com.btjnonbrokerage.databinding.FragmentStateSelectDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StateSelectDialogFragment(val onItemSelected: (StateList) -> Unit) : DialogFragment() {

    @Inject
    lateinit var mainViewModel: MainViewModel
    private var list: List<StateList> = ArrayList()


    private lateinit var binding: FragmentStateSelectDialogBinding
    private val stateAdapter by lazy {
        StateAdapter(onItemClicked = {
            onItemSelected(it)
            dismiss()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStateSelectDialogBinding.inflate(inflater, container, false)
        mainViewModel.getStateViewModel()


        binding.etSearchState.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterState(p0.toString().trim())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        stateApiResponse()
        setStateAdapter()
        return binding.root
    }

    private fun setStateAdapter() {
        binding.rvState.apply {
            adapter = stateAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun stateApiResponse() {
        mainViewModel.getStateLiveData.observe(viewLifecycleOwner) {
            stateAdapter.submitList(it.states)
            list = it.states
        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            500,
            700
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    private fun filterState(searchText: String) {
        val filteredList = list.filter {
            it.name.startsWith(searchText.uppercase(), ignoreCase = true)
        }
        stateAdapter.submitList(filteredList)
    }
}
