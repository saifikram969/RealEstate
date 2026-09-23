package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.DemoModel
import com.btjnonbrokerage.databinding.LayoutItemFilterSearchBinding

class FeaturedScreenAdapter:ListAdapter<DemoModel, FeaturedScreenAdapter.MyViewHolder>(
    Diff
) {

    inner class MyViewHolder(private val binding: LayoutItemFilterSearchBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: DemoModel) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutItemFilterSearchBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }

    object Diff : DiffUtil.ItemCallback<DemoModel>() {
        override fun areItemsTheSame(oldItem: DemoModel, newItem: DemoModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DemoModel, newItem: DemoModel): Boolean {
            return oldItem == newItem
        }
    }


}