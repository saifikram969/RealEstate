package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.TopUser
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutTopOwnerItemBinding

class TopEstateOwnerAdapter(val onClick : (TopUser) -> Unit ) : ListAdapter<TopUser,TopEstateOwnerAdapter.MyViewHolder>(Diff) {

    inner class MyViewHolder(val binding: LayoutTopOwnerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TopUser) {
            Glide.with(binding.root.context).load(item.profileimg).placeholder(R.drawable.dealer_img) .into(binding.circleImageView)
            binding.textView5.text = item.name
            binding.tvNumberSold.text = item.property_count

            itemView.setOnClickListener {
                onClick(item)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutTopOwnerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.topUserRank.text = "#"+(position+1).toString()
    }

    object Diff:DiffUtil.ItemCallback<TopUser>() {
        override fun areItemsTheSame(oldItem: TopUser, newItem: TopUser): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopUser, newItem: TopUser): Boolean {
            return oldItem==newItem
        }

    }

}