package com.btjnonbrokerage.adapters.dialogadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.btjnonbrokerage.MVVM.APIModel.Locations.State.StateList
import com.btjnonbrokerage.databinding.ItemStateBinding

class StateAdapter(val onItemClicked: (StateList) -> Unit) : ListAdapter<StateList, StateAdapter.MyViewHolder>(Diff) {
 inner   class MyViewHolder(val binding: ItemStateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StateList) {
            binding.stateName.text = item.name
            itemView.setOnClickListener {
                onItemClicked(item)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            ItemStateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<StateList>() {
        override fun areItemsTheSame(oldItem: StateList, newItem: StateList): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: StateList, newItem: StateList): Boolean {
            return oldItem == newItem
        }

    }


}
