package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.DateFormat
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.Notification
import com.btjnonbrokerage.databinding.ItemNotificationListBinding

class NotificationListAdapter :
    ListAdapter<Notification, NotificationListAdapter.MyViewHolder>(Diff) {
    class MyViewHolder(val binding: ItemNotificationListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification){
            try {
                binding.tvTitleNotificationsList.text = item.title
                binding.tvDescriptionNotifications.text = item.description
                binding.tvDateNotificationsList.text = DateFormat().formatDateNotifications(item.date)
                Glide.with(binding.root).load(item.image).into(binding.ivNotification)
            }catch (_: Exception){

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ItemNotificationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

}