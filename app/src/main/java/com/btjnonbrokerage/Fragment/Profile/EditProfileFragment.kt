package com.btjnonbrokerage.Fragment.Profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.UpdateProfileRequest
import com.btjnonbrokerage.MVVM.APIModel.Profile.UpdateProfile.User
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>() {

    private val PICK_IMAGE_FROM_CAMERA = 1
    private val PICK_IMAGE_FROM_GALLERY = 2
    private var userProfileImg: String? = null

    @Inject
    lateinit var authViewModel: AuthViewModel

    private lateinit var mySharedPreferences: MySharedPreferences

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditProfileBinding = FragmentEditProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mySharedPreferences = MySharedPreferences(requireContext())

        setupUI()
        setupListeners()
        updateProfileApiResponse()
    }

    // UI Setup Function
    private fun setupUI() {
        binding.etName.setText(mySharedPreferences.getUserName().toString())
        binding.etNumber.text = mySharedPreferences.getUserNumber()

        userProfileImg = mySharedPreferences.getUserImage()
        Glide.with(requireContext())
            .load(userProfileImg)
            .placeholder(R.drawable.user_avatar)
            .into(binding.profileImageEdit)

        binding.toolbarEditProfile.toolbarTitle.text = getString(R.string.edit_profile)
    }

    // Setup event listeners
    private fun setupListeners() {
        binding.etNumber.setOnClickListener {
            showErrorSnackBar("You can't change this field", true)
        }

        binding.profileImageEdit.setOnClickListener {
            showImagePickerDialog()
        }

        binding.tvSaveEditProfile.setOnClickListener {
            validateAndSaveProfile()
        }

        binding.toolbarEditProfile.toolbarBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Show dialog to pick image
    private fun showImagePickerDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkAndRequestCameraPermission()
                    1 -> openGallery()
                }
            }
            .show()
    }

    // Handle permission request and open camera
    private fun checkAndRequestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> openCamera()
            else -> permissionCameraLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    // Open camera intent
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, PICK_IMAGE_FROM_CAMERA)
    }

    // Open gallery intent
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY)
    }

    private val permissionCameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) openCamera() else showErrorSnackBar("Camera permission is required", true)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_FROM_GALLERY -> {
                    data?.data?.let { uri ->
                        handleGalleryImage(uri)
                    }
                }
                PICK_IMAGE_FROM_CAMERA -> {
                    val photo: Bitmap? = data?.extras?.get("data") as? Bitmap
                    photo?.let {
                        handleCameraImage(it)
                    }
                }
            }
        }
    }

    // Handle selected image from gallery
    private fun handleGalleryImage(uri: Uri) {
        val bitmap = getCapturedImage(uri)
        val localPath = saveImageToInternalStorage(bitmap)
        userProfileImg = localPath
        Glide.with(requireContext())
            .load(localPath)
            .placeholder(R.drawable.user_avatar)
            .into(binding.profileImageEdit)
    }

    // Handle captured image from camera
    private fun handleCameraImage(photo: Bitmap) {
        val localPath = saveImageToInternalStorage(photo)
        userProfileImg = localPath
        binding.profileImageEdit.setImageBitmap(photo)
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        // Delete old local image if it exists to avoid storage leaks
        if (userProfileImg?.startsWith("/") == true || userProfileImg?.startsWith("file://") == true) {
            val oldFile = File(userProfileImg!!.replace("file://", ""))
            if (oldFile.exists()) {
                oldFile.delete()
            }
        }

        val file = File(requireContext().filesDir, "profile_img_${System.currentTimeMillis()}.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    // Profile validation and save
    private fun validateAndSaveProfile() {
        val name = binding.etName.text.toString().trim()
        if (name.isEmpty()) {
            showErrorSnackBar("Enter your name", false)
        } else {
            updateProfileAPI(name)
        }
    }

    // Call API to update profile
    private fun updateProfileAPI(name: String) {
        loader.show()
        val token = mySharedPreferences.getToken().toString()
        // We only send the name to the API. Image is stored locally.
        authViewModel.updateProfileViewModel(UpdateProfileRequest(name, "", token))
    }

    // Observe profile update response
    private fun updateProfileApiResponse() {
        authViewModel.updateProfileLiveData.observe(viewLifecycleOwner) {
            loader.dismiss()
            if (it.status == "success") {
                updateLocalProfileData(it.user)
                showErrorSnackBar("Profile updated successfully", false)
            } else {
                showErrorSnackBar("Profile update failed", true)
            }
        }
    }

    // Update local shared preferences with new profile data
    private fun updateLocalProfileData(user: User) {
        Log.d("yqwertyui",user.toString())
        
        // Use local image if available, else use API image
        val finalImage = if (userProfileImg?.startsWith("/") == true || userProfileImg?.startsWith("file://") == true) {
            userProfileImg
        } else {
            user.profile_image
        }

        Glide.with(requireContext())
            .load(finalImage)
            .placeholder(R.drawable.user_avatar)
            .error(R.drawable.user_avatar)
            .into(binding.profileImageEdit)

        mySharedPreferences.apply {
            setUser(user.fullname, user.mobile, finalImage)
        }
    }
}



