package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Model.ImageModel
import com.btjnonbrokerage.databinding.LayoutAddEstatePhotosBinding


class AddListingAdapter(val onCrossClick: (ImageModel) -> Unit) : ListAdapter<ImageModel, AddListingAdapter.MyViewHolder>(
    diff
) {

    inner class MyViewHolder(private val binding: LayoutAddEstatePhotosBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImageModel) {
            binding.imgAddListing.setImageURI(item.imgUri)

            binding.ivRemoveImage.setOnClickListener {
                onCrossClick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddListingAdapter.MyViewHolder {

        return MyViewHolder(LayoutAddEstatePhotosBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AddListingAdapter.MyViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    object diff : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageModel, newItem: ImageModel): Boolean {
            return oldItem.imgUri == newItem.imgUri
        }
    }



}
