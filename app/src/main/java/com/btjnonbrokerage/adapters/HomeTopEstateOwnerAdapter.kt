package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.TopUser
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutHomeTopEstateOwnersBinding

class HomeTopEstateOwnerAdapter(val onClick : (TopUser) -> Unit) : ListAdapter<TopUser, HomeTopEstateOwnerAdapter.MyViewHolder>(Diff){

    inner   class MyViewHolder (private val binding : LayoutHomeTopEstateOwnersBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : TopUser){
            binding.ownerName.text = item.name
            Glide.with(binding.root.context).load(item.profileimg).placeholder(R.drawable.dealer_img) .into(binding.ivOwnerProfile)


            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeTopEstateOwnerAdapter.MyViewHolder {
        return  MyViewHolder(LayoutHomeTopEstateOwnersBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: HomeTopEstateOwnerAdapter.MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<TopUser>() {
        override fun areItemsTheSame(oldItem: TopUser, newItem: TopUser): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopUser, newItem: TopUser): Boolean {
            return oldItem == newItem
        }
    }


}