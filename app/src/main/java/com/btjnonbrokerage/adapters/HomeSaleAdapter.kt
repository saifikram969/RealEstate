package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.DemoModel
import com.btjnonbrokerage.databinding.LayoutHomeSaleItemBinding

class HomeSaleAdapter(val onClick : (DemoModel) -> Unit) : ListAdapter<DemoModel, HomeSaleAdapter.MyViewHolder>(Diff) {

    inner   class MyViewHolder (private val binding : LayoutHomeSaleItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : DemoModel){
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutHomeSaleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
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