package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.TopLocation
import com.btjnonbrokerage.databinding.LayoutHomeTopLocationBinding

class HomeTopLocationAdapter(val onClick : (TopLocation) -> Unit) : ListAdapter<TopLocation, HomeTopLocationAdapter.MyViewHolder>(Diff){


    inner   class MyViewHolder (private val binding : LayoutHomeTopLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : TopLocation){
            binding.locationName.text = item.state_data.name
            Glide.with(binding.root.context).load(item.state_data.img_url).into(binding.imageLocation)
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutHomeTopLocationBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<TopLocation>() {
        override fun areItemsTheSame(oldItem: TopLocation, newItem: TopLocation): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopLocation, newItem: TopLocation): Boolean {
            return oldItem == newItem
        }
    }


}