package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.ItemImageSlidersBinding

class ImagePagerAdapter(val onClick : (String) -> Unit) : ListAdapter<String , ImagePagerAdapter.MyViewHolder>(Diff) {

 inner   class MyViewHolder (val binding : ItemImageSlidersBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : String){
            Glide.with(binding.root.context)
                .load(item)
                .into(binding.imageView)

            itemView.setOnClickListener{
                onClick(item)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
      return  MyViewHolder(ItemImageSlidersBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(getItem(position))
    }
    object  Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return  oldItem == newItem
        }
    }


}