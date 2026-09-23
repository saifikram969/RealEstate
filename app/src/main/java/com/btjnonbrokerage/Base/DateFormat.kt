package com.btjnonbrokerage.Base

import java.text.SimpleDateFormat
import java.util.Locale

class DateFormat {

    fun formatDateTime(datetime: String): String {
        return try {
            val inputFormat =
                java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
            val outputFormat =
                java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            val date = inputFormat.parse(datetime)
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    fun formatDate(inputDate: String): String {
        return try {
            // Define the input date format
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            // Define the desired output date format
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

            // Parse the input date
            val date = inputFormat.parse(inputDate)
            // Format the date into the desired format
            outputFormat.format(date ?: java.util.Date())
        } catch (e: Exception) {
            "Invalid date" // Handle the exception
        }
    }

    fun formatDateNotifications(dateString: String): String? {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        return try {
            // Parse the input date string to a Date object
            val date = inputFormat.parse(dateString)
            // Format the Date object to the desired output format
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}