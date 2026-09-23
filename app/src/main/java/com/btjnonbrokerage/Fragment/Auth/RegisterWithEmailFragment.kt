package com.btjnonbrokerage.Fragment.Auth

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.media.ExifInterface.ORIENTATION_NORMAL
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270
import androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90
import androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.MVVM.APIModel.Auth.Register.RegisterWithPhoneReq
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.Model.DropdownItem
import com.btjnonbrokerage.R
import com.btjnonbrokerage.adapters.SpinnerItemAdapter
import com.btjnonbrokerage.databinding.FragmentRegisterWithEmailBinding
import com.btjnonbrokerage.extensions.getName
import dagger.hilt.android.AndroidEntryPoint
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class RegisterWithEmailFragment : BaseFragment<FragmentRegisterWithEmailBinding>() {

    private val PICK_IMAGE_FROM_CAMERA = 1
    private val PICK_IMAGE_FROM_GALLARY = 2
    private var userProfileImg :String? = null
    private var category_type = ""
    private var selectedFileUri: Uri? = null


    @Inject
    lateinit var authViewModel: AuthViewModel

    private var countryCodeWithPlus = "+91"

    private var dropdownItems = listOf(

        DropdownItem(R.drawable.homey, "Landlord"),
        DropdownItem(R.drawable.homey, "Tenant"),
        DropdownItem(R.drawable.homey, "Dealer"),
//        DropdownItem(R.drawable.tutionfees, "Security Deposit")
    )

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterWithEmailBinding = FragmentRegisterWithEmailBinding.inflate(layoutInflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpinner()
        officePhoto()

        // Login Button
        binding.tvLogin.setOnClickListener {
            val action =
                RegisterWithEmailFragmentDirections.actionRegisterWithEmailFragmentToLogInFormFragment()
            findNavController().navigate(action)
        }


        //toolbar
        binding.toolbarCreateAccount.apply {
            toolbarTitle.text = "Create Account"
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }


        // Image picker
        binding.rlRegisterImage.setOnClickListener {
            val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> checkAndRequestCameraPermission()
                    1 -> openGallery()
                }
            }
            builder.show()
        }


        // user Register
        binding.tvRegisterBtnRE.setOnClickListener {
            hideKeyboard()
            val name = binding.etUserNameRE.text.toString()
            val number = binding.etPHoneNumber.text.toString()
            val email = binding.etUserGmail.text.toString()
            val address = binding.etAddress.text.toString()
            val officeName = binding.etOfficeName.text.toString()


            // Perform validation checks
            if (name.isEmpty()) {
                showErrorSnackBar("Please enter your name", false)
                return@setOnClickListener
            }

            if (number.isEmpty()) {
                showErrorSnackBar("Please enter your number", false)
                return@setOnClickListener
            }

            if (number.length != 10) {
                showErrorSnackBar("Please enter a valid number", false)
                return@setOnClickListener
            }
            if (email.isEmpty()){
                showErrorSnackBar("Please enter a valid Gmail",false)
                return@setOnClickListener
            }
            // Category-wise validation
            when (category_type) {
                "Landlord" -> {
                    if (address.isEmpty()) {
                        showErrorSnackBar("Please enter your address", false)
                        return@setOnClickListener
                    }
                }
                "Dealer" -> {
                    if (address.isEmpty()) {
                        showErrorSnackBar("Please enter your address", false)
                        return@setOnClickListener
                    }
                    if (officeName.isEmpty()) {
                        showErrorSnackBar("Please enter your office name", false)
                        return@setOnClickListener
                    }
                    if (selectedFileUri == null) {
                        showErrorSnackBar("Please upload office photo", false)
                        return@setOnClickListener
                    }
                }
            }

            // If all validations pass, proceed with registration
            val img = userProfileImg.toString()

            authViewModel.registerWithPhoneViewModel(RegisterWithPhoneReq(address,email,name,number,countryCodeWithPlus,img,
                selectedFileUri.toString(),officeName,category_type))
            // Observe registration result
            authViewModel.registerWithPhoneRequestLiveData.observe(viewLifecycleOwner) { response ->
                if (response.status == "success") {
                    val action =
                        RegisterWithEmailFragmentDirections.actionRegisterWithEmailFragmentToOTPFragment(response.uid)
                    findNavController().navigate(action)
                } else {
                    showErrorSnackBar(response.msg, false)
                }
            }
        }


    }


    private val permissionCameraLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            openCamera()
        }
    }

    private fun checkAndRequestCameraPermission() {
        val isCamera = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isCamera) {
            permissionCameraLauncher.launch(android.Manifest.permission.CAMERA)

        } else {
            openCamera()
        }


    }

    private fun openCamera() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i, PICK_IMAGE_FROM_CAMERA)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_FROM_GALLARY)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_FROM_GALLARY) {
                val uri: Uri = data?.data!!
                userProfileImg = getStringImage(getCapturedImage(uri))
                Glide.with(requireContext()).load(uri).placeholder(R.drawable.user_avatar).into(binding.profileImage)

                val isLarge = isImageSizeGreaterThan2000KB(uri)
                if (isLarge) {
                    Log.d("lawerty","Image size is greater than 2000 KB.")
                } else {
                    Log.d("lawerty","Image size is within limits.")
                }


            }
            if (requestCode == PICK_IMAGE_FROM_CAMERA) {
                val photo: Bitmap = data?.extras?.get("data") as Bitmap
                Glide.with(requireContext()).load(photo).into(binding.profileImage)
                userProfileImg = getStringImage(photo)
                val isLarge = isBitmapSizeGreaterThan2000KB(photo)
                if (isLarge) {
                    Log.d("lawerty","Image size is greater than 2000 KB.")
                } else {
                    Log.d("lawerty","Image size is within limits.")
                }
            }


        }

    }

    private fun isImageSizeGreaterThan2000KB(uri: Uri): Boolean {
        var input: InputStream? = null
        var fileSizeInBytes: Long = 0

        try {
            input = requireContext().contentResolver.openInputStream(uri)

            // First, decode bounds to get dimensions
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(input, null, options)

            // Reset the InputStream
            input?.close()
            input = requireContext().contentResolver.openInputStream(uri)

            // Calculate the file size in bytes
            fileSizeInBytes = input?.available()?.toLong() ?: 0

            // Handle EXIF orientation (if needed)
            val exif = ExifInterface(input!!)
            val orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION)?.toInt() ?: ExifInterface.ORIENTATION_NORMAL

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                val outWidth = options.outWidth
                val outHeight = options.outHeight
                options.outWidth = outHeight
                options.outHeight = outWidth
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            input?.close()
        }

        // Convert bytes to kilobytes
        val fileSizeInKB = fileSizeInBytes / 1024 // Size in KB

        // Return true if size is greater than 2000 KB, else false
        return fileSizeInKB > 2000
    }
    private fun isBitmapSizeGreaterThan2000KB(bitmap: Bitmap): Boolean {
        // Calculate the size of the Bitmap in bytes
        val fileSizeInBytes = bitmap.byteCount.toLong()

        // Convert bytes to kilobytes
        val fileSizeInKB = fileSizeInBytes / 1024 // Size in KB

        // Return true if size is greater than 2000 KB, else false
        return fileSizeInKB > 2000
    }

    private fun setSpinner(){

        // Create a custom ArrayAdapter for the Spinner
        val spinnerAdapter = SpinnerItemAdapter(requireContext(), dropdownItems)
        binding.spinner.adapter = spinnerAdapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = dropdownItems[position]
                category_type = dropdownItems[position].name

                if (category_type == "Landlord"){
                    binding.llOfficeNamePhoto.visibility = View.GONE
                    binding.llAddress.visibility = View.VISIBLE

                }
                else if (category_type == "Tenant"){
                    binding.llAddress.visibility = View.GONE
                    binding.llOfficeNamePhoto.visibility = View.GONE
                }
                else if (category_type == "Dealer"){
                    binding.llAddress.visibility = View.VISIBLE
                    binding.llOfficeNamePhoto.visibility = View.VISIBLE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle if needed
            }
        }
    }
    private fun officePhoto(){
        // File chooser setup
        val galleryResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    selectedFileUri = result.data?.data
                    Log.d("Result", result.data?.data.toString())
                    binding.tvFileName.text =
                        result.data?.data?.getName(requireContext()) ?: "No File Chosen"
                }
            }

        binding.tvChooseFile.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "application/pdf"
            galleryResult.launch(intent)
        }
    }


}