package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.BuildingSelectModel
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutEstateBinding


class WelcomeBuildingTypeAdapter(val onClick : (BuildingSelectModel, position:Int) -> Unit): ListAdapter<BuildingSelectModel, WelcomeBuildingTypeAdapter.MyViewHolder>(
    Diff
) {

    inner class MyViewHolder(private val binding: LayoutEstateBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: BuildingSelectModel, position: Int) {
            binding.cardviewOuter.setOnClickListener {
                onClick(model,position)
                notifyDataSetChanged()
            }

            if(model.select) {
                binding.cardviewOuter.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,
                    R.color.cardViewSelectBg))
            }
            else {
                binding.cardviewOuter.setCardBackgroundColor(ContextCompat.getColor(binding.root.context,R.color.grayRealEstate))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutEstateBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }

    object Diff : DiffUtil.ItemCallback<BuildingSelectModel>() {
        override fun areItemsTheSame(oldItem: BuildingSelectModel, newItem: BuildingSelectModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: BuildingSelectModel, newItem: BuildingSelectModel): Boolean {
            return oldItem == newItem
        }
    }


}