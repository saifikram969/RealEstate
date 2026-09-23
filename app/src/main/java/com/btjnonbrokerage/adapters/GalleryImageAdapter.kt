package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.ChatMessage
import com.btjnonbrokerage.databinding.AdminMsgRvItemBinding
import com.btjnonbrokerage.databinding.LayoutImageBinding
import com.btjnonbrokerage.databinding.LayoutUserMsgRvItemBinding

class GalleryImageAdapter (private val currentUserId: String) : ListAdapter<ChatMessage, RecyclerView.ViewHolder>(
    Diff
) {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    inner class SentMessageViewHolder(private val binding: LayoutImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ChatMessage) {

            binding.layoutImg.setImageBitmap(model.imageBitmap)


            // You can set other UI components here, like timestamp, status, etc.
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: LayoutImageBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ChatMessage) {
            binding.layoutImg.setImageBitmap(model.imageBitmap)
            // You can set other UI components here, like sender info, timestamp, etc.
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            SentMessageViewHolder(LayoutImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            ReceivedMessageViewHolder(LayoutImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.senderId == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    object Diff : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}