package com.btjnonbrokerage.adapters.chatAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.Message
import com.btjnonbrokerage.R


class ChatAdapter : ListAdapter<Message, ChatAdapter.MyViewHolder>(Diff) {

    // ViewTypes for determining if the message is from the user or others
    private val LEFT = 0
    private val RIGHT = 1

    // ViewHolder for holding message views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.show_message)
        val timeView: TextView = itemView.findViewById(R.id.timeView)
//

        // Method to bind the message data
        fun bind(message: Message) {
            messageText.text = message.message
            timeView.text = message.time
        }
    }

    // Inflate the layout based on the message sender
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = if (viewType == RIGHT) {
            inflater.inflate(R.layout.chatitemright, parent, false)
        } else {
            inflater.inflate(R.layout.chatitemleft, parent, false)
        }
        return MyViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message) // Binding message to the ViewHolder
    }

    // Determine the view type (LEFT or RIGHT) based on the message sender
    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.type == 1) RIGHT else LEFT
    }

    // DiffUtil for efficient list updates
    object Diff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem // Check if the messages are the same by ID
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem // Check if the content is the same
        }
    }
}


