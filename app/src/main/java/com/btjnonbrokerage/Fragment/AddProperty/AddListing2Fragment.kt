package com.btjnonbrokerage.Fragment.AddProperty

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_NORMAL
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90
import androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION
import androidx.lifecycle.lifecycleScope
import com.btjnonbrokerage.Base.BaseFragment

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.btjnonbrokerage.Model.ImageModel
import com.btjnonbrokerage.Model.ImgList
import com.btjnonbrokerage.databinding.FragmentAddListing2Binding

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddListing2Fragment : BaseFragment<FragmentAddListing2Binding>() {
    val args: AddListing2FragmentArgs by navArgs()
    var list = ArrayList<ImageModel>()


    val adapter by lazy {
        com.btjnonbrokerage.adapters.AddListingAdapter(onCrossClick = {
            removeAddImage(it.imgUri)
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddListing2Binding = FragmentAddListing2Binding.inflate(layoutInflater)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.toolbarAddListing2.toolbarBack.visibility = View.GONE

        setAdapter()


        binding.cvAddImg.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Allow multiple selection
            galleryActivityResultLauncher.launch(galleryIntent)
        }


        binding.inNextBtn.nextBtn.setOnClickListener {
            if (adapter.currentList.size < 4) {
                showErrorSnackBar("Add at least 4 Images", true)
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        loader.show() // Show the loader

                        val list = withContext(Dispatchers.IO) {
                            adapter.currentList.map { item ->
                                getStringImage(getCapturedImage(item.imgUri))
                            }
                        }

                        val imgList = ImgList(ArrayList(list))

                        // Navigate to the next fragment
                        val action = AddListing2FragmentDirections
                            .actionAddListing2FragmentToAddListingFragment3(
                                args.propertyName,
                                args.Category,
                                args.countyCode,
                                args.phoneNumber,
                                args.address,
                                args.state,
                                args.city,
                                args.pincode,
                                imgList,
                                args.address2,
                                args.propertyDescription,
                                lat = "0",
                                long = "0",
                                args.propertyType
                            )
                        findNavController().navigate(action)
                    } finally {
                        loader.dismiss()
                    }
                }
            }
        }


        binding.inNextBtn.ivBackBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private var galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val clipData = data?.clipData
            val imageUriList = mutableListOf<Uri>()

            if (clipData != null) {
                // Multiple images selected
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    imageUriList.add(imageUri)
                }
            } else {
                // Single image selected
                val imageUri = data?.data
                if (imageUri != null) {
                    imageUriList.add(imageUri)
                }
            }

            // Process selected images
            processSelectedImages(imageUriList)
        }
    }

    private fun processSelectedImages(imageUris: List<Uri>) {
        loader.show()
        for (imageUri in imageUris) {
            list.add(ImageModel(id = imageUri.toString(), imgUri = imageUri))
            adapter.submitList(ArrayList(list))


        }
        loader.dismiss()
    }


    private fun setAdapter() {
        binding.rvAddListingPhotos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAddListingPhotos.adapter = adapter
    }

    private fun removeAddImage(imageUri: Uri) {
        Log.d("RemoveImage", "Attempting to remove image: $imageUri")

        val updatedList = list.filter { it.imgUri != imageUri }
        Log.d("RemoveImage", "Updated list size: ${updatedList.size}")

        list.clear()
        list.addAll(updatedList)
        adapter.submitList(ArrayList(updatedList))
    }

    private fun keyBoardFun() {
        handleKeyBoard.startKeyboardListener(binding.root) {
            if (it) {
                binding.llNextBtn.visibility = View.GONE
            } else {
                binding.llNextBtn.visibility = View.VISIBLE
            }
        }
    }

}