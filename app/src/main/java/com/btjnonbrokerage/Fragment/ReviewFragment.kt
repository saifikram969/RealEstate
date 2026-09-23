package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.AllReviewListRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.Review.AllReviewListAdapter
import com.btjnonbrokerage.adapters.ReviewLayoutAdapter
import com.btjnonbrokerage.databinding.FragmentReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReviewFragment : BaseFragment<FragmentReviewBinding>() {


    val args: ReviewFragmentArgs by navArgs()

    private var page = 1
    private var totalCount = 0

    @Inject
    lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val reviewLayoutAdapter by lazy {
        AllReviewListAdapter()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReviewBinding = FragmentReviewBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.commonToolbarReview.apply {
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }
            toolbarTitle.text = "Reviews"
        }








        setReviewAdapter()
        setPagination()
        getAllReviewResponse()
        reviewListApiCall(page)
        setProperty()
    }

    private fun setReviewAdapter() {
        binding.rvReview.apply {
            adapter = reviewLayoutAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun reviewListApiCall(page: Int) {
        mainViewModel.allReviewListFun(AllReviewListRequest(page.toString(), args.propertyId))
    }

    private fun setPagination() {
        binding.rvReview.setOnScrollChangeListener { view, _, _, _, _ ->
            if (view?.canScrollVertically(RecyclerView.VERTICAL) == false) {
                if (reviewLayoutAdapter.currentList.size < totalCount) {
                    page++
                    reviewListApiCall(page)
                }
            }
        }
    }


    private fun getAllReviewResponse() {
        mainViewModel.getAllReviewListLiveData.observe(viewLifecycleOwner) { it ->
            totalCount = it.total_reviews.toInt()
            if (page > 1) {
                val mList = reviewLayoutAdapter.currentList.toMutableList()
                mList.addAll(it.ratings)
                reviewLayoutAdapter.submitList(mList)
            } else {
                reviewLayoutAdapter.submitList(it.ratings)
            }
        }
    }

    private fun setProperty() {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            viewModel.getSinglePropertyViewModel(GetSinglePropertyRequest(args.propertyId, uid))
        }
        viewModel.getSinglePropertyRequestLiveData.observe(viewLifecycleOwner) { data ->
            try {
                Glide.with(requireContext()).load(data.property_details.property_images[0])
                    .into(binding.prpImage)
                binding.tvPropertyName.text = data.property_details.pr_name
                binding.avgRating.text = data.property_details.avg_rating.toString()
                binding.adress1.text =
                    data.property_details.city_name + " " + data.property_details.state_name.toString()
                binding.tvCategory.text = data.property_details.category
            } catch (_: Exception) {

            }


        }

    }


}