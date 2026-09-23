package com.btjnonbrokerage.adapters.dialogadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.btjnonbrokerage.MVVM.APIModel.Locations.City.CityList
import com.btjnonbrokerage.databinding.ItemCityBinding

class CityAdapter(val onItemClicked: (CityList) -> Unit) : ListAdapter<CityList, CityAdapter.MyViewHolder>(
    Diff
) {
  inner  class MyViewHolder(val binding: ItemCityBinding)  : RecyclerView.ViewHolder(binding.root){

        fun bind(item : CityList){
            binding.cityName.text = item.city
            itemView.setOnClickListener {
                onItemClicked(item)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return  MyViewHolder(ItemCityBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

     object  Diff : DiffUtil.ItemCallback<CityList>() {
         override fun areItemsTheSame(oldItem: CityList, newItem: CityList): Boolean {
             return  oldItem == newItem
         }

         override fun areContentsTheSame(oldItem: CityList, newItem: CityList): Boolean {
            return  oldItem == newItem
         }
     }


}