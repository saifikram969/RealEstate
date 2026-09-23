package com.btjnonbrokerage.adapters.PropertyImages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.databinding.LayoutAddEstatePhotosBinding


class EditPropertyImgAdapter(val onCrossClick: (String) -> Unit) : ListAdapter<String, EditPropertyImgAdapter.MyViewHolder>(
    Diff
) {
    inner class MyViewHolder(val binding: LayoutAddEstatePhotosBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind (img : String){
                Glide.with(binding.root.context).load(img).into(binding.imgAddListing)
                binding.ivRemoveImage.setOnClickListener {
                    onCrossClick(img)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutAddEstatePhotosBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}