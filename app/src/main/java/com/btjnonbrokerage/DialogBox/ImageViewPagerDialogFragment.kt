package com.btjnonbrokerage.DialogBox

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.adapters.ImagePagerAdapter
import com.btjnonbrokerage.databinding.FragmentImageViewPagerDialogBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImageViewPagerDialogFragment(private val images: List<String>) : DialogFragment() {

    @Inject
    lateinit var viewModel: AuthViewModel

    private val imageAdapter by lazy {
        ImagePagerAdapter { imageUrl ->
            Log.d("ImageClick", imageUrl)
        }
    }

    private lateinit var binding: FragmentImageViewPagerDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageViewPagerDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = imageAdapter


        Log.d("ImageViewPager", images.toString())
        if (images.isNotEmpty()) {
            imageAdapter.submitList(images)
        } else {
            Log.e("ImageViewPager", "Image list is empty or null")
        }

        println(images)
        imageAdapter.submitList(images)
        imageAdapter.notifyDataSetChanged()

//        val uid = MySharedPreferences(requireContext()).getToken()
//        viewModel.getSinglePropertyViewModel(GetSinglePropertyRequest(propertyId, uid.toString()))
//        viewModel.getSinglePropertyRequestLiveData.observe(viewLifecycleOwner) { data ->
//            val images = data.property_details.property_images
//            Log.d("ImageViewPagerDialogFragment", "Images received: $images")
//            imageAdapter.submitList(images)
//            Log.d("ImageViewPager", images.toString())
//        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(600, 800)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
