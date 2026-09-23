package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.RoomModel
import com.btjnonbrokerage.databinding.LayoutItemRoomBinding

class RoomAdapter : ListAdapter<RoomModel, RoomAdapter.MyViewModel>(diff) {
    class MyViewModel(val binding: LayoutItemRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(roomModel: RoomModel) {
            binding.tvRoom.text = roomModel.room
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.MyViewModel {
        return MyViewModel(
            LayoutItemRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        val roomModel = getItem(position)
        holder.bind(roomModel)
    }

    object diff : DiffUtil.ItemCallback<RoomModel>() {
        override fun areItemsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RoomModel, newItem: RoomModel): Boolean {
            return oldItem == newItem
        }
    }

}