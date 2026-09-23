package com.btjnonbrokerage.Base

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.btjnonbrokerage.MVVM.APIModel.favorite.addfavorite.AddFavoriteRequest
import com.btjnonbrokerage.MVVM.APIModel.favorite.removefavorite.RemoveFavoriteRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.btjnonbrokerage.R
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse.RelatedProperty
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse.UserDetails
import com.btjnonbrokerage.MVVM.APIModel.favorite.getFavorites.Property as FavoriteProperty
abstract class BaseFragment<B : ViewBinding> : Fragment() {
    
    lateinit var binding: B
    lateinit var loader: Dialog
    val handleKeyBoard = HandleKeyBoard()


    @Inject
    lateinit var mainViewModelBase: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        dialogShow()


        return binding.root
    }

    fun dialogShow() {
        loader = Dialog(requireContext())
        loader.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loader.setContentView(R.layout.layout_loader)
        loader.setCancelable(false)
    }

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phonePattern = Patterns.PHONE // Android's built-in phone number pattern
        return phonePattern.matcher(phoneNumber).matches()
    }


    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(
                (requireActivity().findViewById(android.R.id.content)),
                message,
                Snackbar.LENGTH_LONG
            )
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun addToWishListBaseAPI(property: Any) {
        try {
            var json = Gson().toJson(property)
            // Map keys so different property models match the FavoriteProperty model
            json = json.replace("\"images\":", "\"property_images\":")
            json = json.replace("\"facilities\":", "\"env_facilities\":")
            val favProp = Gson().fromJson(json, FavoriteProperty::class.java)

            val prefs = MySharedPreferences(requireContext())
            val list = prefs.getFavoritesList().toMutableList()
            if (list.none { it.property_id == favProp.property_id }) {
                list.add(favProp)
                prefs.saveFavoritesList(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Keep the old string version just in case, but it shouldn't be used for ADD because we need full details
    fun addToWishListBaseAPI(propertyId: String) {
        // Ignored. Use the object version for local favorites to persist full details.
    }

    fun removeToWishListBaseAPI(propertyId: String) {
        val prefs = MySharedPreferences(requireContext())
        val list = prefs.getFavoritesList().toMutableList()
        list.removeAll { it.property_id == propertyId }
        prefs.saveFavoritesList(list)
    }

    fun isFavoritedLocal(propertyId: String): Boolean {
        val prefs = MySharedPreferences(requireContext())
        return prefs.getFavoritesList().any { it.property_id == propertyId }
    }

    fun saveBase64ImageToInternalStorage(base64Str: String, context: android.content.Context, index: Int): String {
        try {
            val decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            val file = File(context.filesDir, "uploaded_prop_${System.currentTimeMillis()}_$index.jpg")
            val fos = FileOutputStream(file)
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, fos)
            fos.close()
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    fun saveUploadedPropertyLocal(propertyDetails: GetSinglePropertyResponse.PropertyDetails) {
        val prefs = MySharedPreferences(requireContext())
        val list = prefs.getUploadedPropertiesList().toMutableList()
        val emptyUserDetails = UserDetails("", "", "", "", "")
        list.add(0, GetSinglePropertyResponse(
            msg = "Success",
            property_details = propertyDetails,
            related_properties = emptyList(),
            user_details = emptyUserDetails,
            request_contact_status = "0",
            status = "success"
        ))
        prefs.saveUploadedPropertiesList(list)
    }

    fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(requireActivity())
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun uppercaseLowerCase( text : String) : String {
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun getRawNumberFromEditText(editText: EditText): Long {
        val text = editText.text.toString()
        val unformattedString = text.replace(Regex("[^\\d]"), "")
        return unformattedString.toLongOrNull() ?: 0
    }

    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        val input: String = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return input
    }

    fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                requireActivity().contentResolver,
                selectedPhotoUri
            )

            else -> {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
        return bitmap
    }

    fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        activity?.let { context ->
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Set the calendar to the selected date
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    // Format the date to the desired format (e.g., 12 Oct 2024)
                    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val selectedDate = dateFormat.format(calendar.time)
                    // Pass the selected date to the callback function
                    onDateSelected(selectedDate)
                },
                year, month, day
            )
            // Set the minimum selectable date to today
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }







}