package com.btjnonbrokerage.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.MVVM.APIModel.review.AddReview.AddReviewRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.databinding.FragmentALLReviewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ALLReviewFragment : BaseFragment<FragmentALLReviewBinding>() {

    private val args: ALLReviewFragmentArgs by navArgs()
    private var currentRating: Float = 0.0f

    @Inject
    lateinit var viewModel: MainViewModel

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentALLReviewBinding = FragmentALLReviewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uid = MySharedPreferences(requireContext()).getToken()
        setupRatingBar()
        setupClickListeners(uid)
    }

    private fun setupRatingBar() {
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            currentRating = rating
        }
    }

    private fun setupClickListeners(uid: String?) {
        binding.imgBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tvLetSubmit.setOnClickListener {
            val comment = binding.comment.text.toString().trim()

            if (comment.isNotEmpty() && uid != null) {
                submitReview(comment, uid)
            } else {
                showErrorSnackBar("Please enter a comment.", true)
            }
        }
    }

    private fun submitReview(comment: String, uid: String) {
        try {
            viewModel.addReviewApi(
                AddReviewRequest(
                    comment,
                    args.propertyId,
                    currentRating.toString(),
                    uid
                )
            )

            viewModel.addReviewApiLiveData.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    showErrorSnackBar("Review added successfully", false)
                    findNavController().navigateUp()
                } else {
                    // Handle non-success status, e.g., API returned an error
                    showErrorSnackBar("Failed to add review: ${response.msg}", true)
                }
            }
        } catch (e: Exception) {
            // Handle network or parsing exceptions
            showErrorSnackBar("An error occurred: ${e.message}", true)
        }
    }

}
