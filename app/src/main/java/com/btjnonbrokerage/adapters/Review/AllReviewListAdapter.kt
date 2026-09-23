package com.btjnonbrokerage.adapters.Review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.Base.DateFormat
import com.btjnonbrokerage.MVVM.APIModel.GetProperty.Single.GetSinglePropertyResponse
import com.btjnonbrokerage.MVVM.APIModel.review.AllReviewList.Rating
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutReviewsUserDetailBinding

class AllReviewListAdapter : ListAdapter<Rating, AllReviewListAdapter.MyViewHolder>(Diff) {
    class MyViewHolder(val binding  : LayoutReviewsUserDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Rating) {


            try {
                binding.tvReviewDetail.text = item.msg
                Glide.with(binding.root.context).load(item.profileimg)
                    .placeholder(R.drawable.dealer_img).error(R.drawable.dealer_img)
                    .into(binding.imageUser)
                binding.ratingVar.rating = item.rating.toFloat()
                binding.tvTime.text = DateFormat().formatDateTime(item.datetime) ?: "Date not available"
                binding.tvUserName.text = item.fullname.toString()
            } catch (_: Exception) {


            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return  MyViewHolder(LayoutReviewsUserDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    object  Diff : DiffUtil.ItemCallback<Rating>() {
        override fun areItemsTheSame(oldItem: Rating, newItem: Rating): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Rating, newItem: Rating): Boolean {
          return  oldItem == newItem
        }
    }

}