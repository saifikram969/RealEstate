package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.databinding.EnvFaciltyLayoutBinding


class EnvFacilityAdapter  : ListAdapter<String, EnvFacilityAdapter.EnvFacilityViewHolder>(DiffCallback) {

    inner class EnvFacilityViewHolder(private val binding: EnvFaciltyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(facility: String) {
            binding.tvCategoryHouse.text = facility
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EnvFacilityViewHolder {
        val binding = EnvFaciltyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EnvFacilityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EnvFacilityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}