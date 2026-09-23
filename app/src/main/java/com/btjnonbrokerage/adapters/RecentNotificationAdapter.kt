package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.R


class RecentNotificationAdapter : RecyclerView.Adapter<RecentNotificationAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentNotificationAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.display_layout_notification, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentNotificationAdapter.ViewHolder, position: Int) {
    //
    }

    override fun getItemCount(): Int {
    return 5
    }
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//        val imageView: ImageView = itemView.findViewById(R.id.imageview)
//        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}