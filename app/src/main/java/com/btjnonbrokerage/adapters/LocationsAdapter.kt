package com.btjnonbrokerage.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.Locations.TopLocations.TopState
import com.btjnonbrokerage.databinding.LayoutLocationsBinding


class LocationsAdapter(val onClick : (TopState) -> Unit) : ListAdapter<TopState, LocationsAdapter.MyViewHolder>(Diff)  {

    inner class MyViewHolder (val binding : LayoutLocationsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : TopState){

            Glide.with(binding.root.context).load(item.state_data.img_url).into(binding.buildingImage)
            binding.textView5.text = uppercaseLowerCase(item.state_data.name)
            binding.root.setOnClickListener {
                onClick(item)
            }

        }
    }


    object Diff : DiffUtil.ItemCallback<TopState>() {
        override fun areItemsTheSame(oldItem: TopState, newItem: TopState): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopState, newItem: TopState): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsAdapter.MyViewHolder {
        return  MyViewHolder(LayoutLocationsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){
        holder.bind(getItem(position))
        holder.binding.topLocationBuildingNumber.text = "#"+1+position
    }
    fun uppercaseLowerCase( text : String) : String {
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }


}