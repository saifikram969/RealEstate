package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.Model.BuildingSelectModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.adapters.WelcomeBuildingTypeAdapter
import com.btjnonbrokerage.databinding.FragmentWelcomeScreenBinding

class WelcomeScreen : BaseFragment<FragmentWelcomeScreenBinding>() {
    private val adapter = WelcomeBuildingTypeAdapter(onClick = { model,position ->
        list[position].select = !list[position].select
    })
    private val list = listOf(
        BuildingSelectModel(false),
        BuildingSelectModel(false),
        BuildingSelectModel(false),
        BuildingSelectModel(false),
        BuildingSelectModel(false),
    )


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWelcomeScreenBinding = FragmentWelcomeScreenBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBuildingTypeAdapter()


        binding.welcomeNext.setOnClickListener {
           // findNavController().navigate(R.id.action_welcomeScreen_to_homeFragment)

        }

    }

    private fun setBuildingTypeAdapter() {
        adapter.submitList(list)
        binding.rvBuildingType.layoutManager =
            GridLayoutManager(requireActivity(), 2, GridLayoutManager.VERTICAL, false)
        binding.rvBuildingType.adapter = adapter
    }
}