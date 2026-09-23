package com.btjnonbrokerage.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.HomeFilterModel
import com.btjnonbrokerage.databinding.LayoutItemHomeFilterBinding

class HomeFilterOptionsAdapter(val onClick: (String) -> Unit) :
    ListAdapter<HomeFilterModel, HomeFilterOptionsAdapter.MyViewHolder>(diff) {
    inner class MyViewHolder(val binding: LayoutItemHomeFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeFilterModel) {
            binding.tvHomeFilterOption.text = item.filterOption


            binding.root.setOnClickListener {
                onClick(binding.tvHomeFilterOption.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            LayoutItemHomeFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    object diff : DiffUtil.ItemCallback<HomeFilterModel>() {
        override fun areItemsTheSame(oldItem: HomeFilterModel, newItem: HomeFilterModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: HomeFilterModel,
            newItem: HomeFilterModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}