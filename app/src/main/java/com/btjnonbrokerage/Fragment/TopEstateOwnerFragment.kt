package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.MVVM.APIModel.TopUsers.TopUserRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.HomePageRequest
import com.btjnonbrokerage.adapters.TopEstateOwnerAdapter
import com.btjnonbrokerage.databinding.FragmentTopEstateOwnerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopEstateOwnerFragment : BaseFragment<FragmentTopEstateOwnerBinding>() {

    @Inject
    lateinit var mainViewModel: MainViewModel


    private val topOwnerAdapter by lazy {
        TopEstateOwnerAdapter(onClick = {
            val acton = TopEstateOwnerFragmentDirections.actionTopEstateOwnerFragment2ToTopUserProfileFragment(it.uid)
            findNavController().navigate(acton)
        })
    }


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTopEstateOwnerBinding = FragmentTopEstateOwnerBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.topAgentToolbar.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setTopOwnerAdapter()
//        mainViewModel.getTopUser(TopUserRequest("1"))
//        mainViewModel.topUserLiveData.observe(viewLifecycleOwner) {
//                if (it.status == "success") {
//                    topOwnerAdapter.submitList(it.top_users)
//                }
//                else{
//                    showErrorSnackBar("Api Failed",false)
//                }
//
//
//        }
        val uid = MySharedPreferences(requireContext()).getToken()
        mainViewModel.homePageVM(HomePageRequest(uid.toString()))
        mainViewModel.homeLiveData.observe(viewLifecycleOwner){
            try {
                if (it.status == "success"){
                    topOwnerAdapter.submitList(it.top_users)
                }
            }catch (e:Exception){
                showErrorSnackBar(e.message.toString(),false)
            }
        }
    }

    private fun setTopOwnerAdapter() {
        binding.rvTopOwners.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTopOwners.adapter = topOwnerAdapter

    }

}