package com.btjnonbrokerage.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.btjnonbrokerage.Model.DropdownItem
import com.btjnonbrokerage.R

class SpinnerItemAdapter(
    context: Context,
    private val items: List<DropdownItem>
) : ArrayAdapter<DropdownItem>(context, R.layout.spinner_item_with_image, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item_with_image, parent, false)
        val item = getItem(position)
        val imageView: ImageView = view.findViewById(R.id.spinner_item_image)
        val textView: TextView = view.findViewById(R.id.spinner_item_text)

        imageView.setImageResource(item?.icon ?: R.drawable.homeicon) // Use a default image if needed
        textView.text = item?.name

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}