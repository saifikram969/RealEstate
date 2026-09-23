package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.ChatFirebase.ChatMessage
import com.btjnonbrokerage.databinding.ItemChatMessageBinding

class ChatAdapter(private val currentUserId: String) : ListAdapter<ChatMessage, ChatAdapter.ChatViewHolder>(ChatDiffCallback()) {

    class ChatViewHolder(val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage, currentUserId: String) {
            if (message.senderId == currentUserId) {
                binding.tvMessageSent.visibility = View.VISIBLE
                binding.tvMessageSent.text = message.message
                binding.tvMessageReceived.visibility = View.GONE
            } else {
                binding.tvMessageReceived.visibility = View.VISIBLE
                binding.tvMessageReceived.text = message.message
                binding.tvMessageSent.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message, currentUserId)
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.timestamp == newItem.timestamp // Assuming timestamp is unique
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem
        }
    }
}
