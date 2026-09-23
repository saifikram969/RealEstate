package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.DateFormat
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutReviewsUserDetailBinding

class ReviewLayoutAdapter :
    ListAdapter<GetSinglePropertyResponse.PropertyDetails.Review, ReviewLayoutAdapter.MyViewHolder>(
        Diff
    ) {

    inner class MyViewHolder(private val binding: LayoutReviewsUserDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GetSinglePropertyResponse.PropertyDetails.Review) {


            try {
                binding.tvReviewDetail.text = model.comment
                Glide.with(binding.root.context).load(model.profileimg)
                    .placeholder(R.drawable.dealer_img).error(R.drawable.dealer_img)
                    .into(binding.imageUser)
                binding.ratingVar.rating = model.rating.toFloat()
                binding.tvTime.text = DateFormat().formatDateTime(model.review_date) ?: "Date not available"
                binding.tvUserName.text = model.fullname.toString()
            } catch (_: Exception) {


            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutReviewsUserDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)
    }

    object Diff : DiffUtil.ItemCallback<GetSinglePropertyResponse.PropertyDetails.Review>() {
        override fun areItemsTheSame(
            oldItem: GetSinglePropertyResponse.PropertyDetails.Review,
            newItem: GetSinglePropertyResponse.PropertyDetails.Review
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GetSinglePropertyResponse.PropertyDetails.Review,
            newItem: GetSinglePropertyResponse.PropertyDetails.Review
        ): Boolean {
            return oldItem == newItem
        }
    }



}
