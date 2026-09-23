package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.NearEstate.RemainingProperty
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.ItemPropertyLayoutBinding

class HomeExploreOtherStateAdapter(val onClick: (RemainingProperty) -> Unit, val clickOnLike : (RemainingProperty)->  Unit, val clickOnUnLike : (RemainingProperty)-> Unit) :
    ListAdapter<RemainingProperty, HomeExploreOtherStateAdapter.MyViewHolder>(Diff) {
    inner class MyViewHolder(private val binding: ItemPropertyLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        var wishlist = 0

        fun bind(item: RemainingProperty) {
            val isFavLocal = com.btjnonbrokerage.Base.MySharedPreferences(binding.root.context).getFavoritesList().any { it.property_id == item.property_id }
            if(item.wishlist == 1 || isFavLocal){
                wishlist = 1
                binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
            }else{
                wishlist = 0
                binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)
            }

            if(item.images.isNotEmpty()) {
                Glide.with(binding.root.context).load(item.images[0])
                    .placeholder(R.drawable.home_ph)
                    .into(binding.ivPropertyImage)
            }
            binding.tvHomeSaleTitle.text = uppercaseLowerCase(item.pr_name)
            binding.tvLocationCity.text = uppercaseLowerCase(item.city_name)
            binding.tvLocationState.text = uppercaseLowerCase(item.state_name)
            binding.ratingTv.text = item.avg_rating.toString()
            binding.tvPricing.text = item.price+"/"
            binding.tvMonthOrYear.text  = item.plan_type
            binding.propertyCategory.text = item.category
            binding.root.setOnClickListener {
                onClick(item)
            }

            binding.ivHeartIcon.setOnClickListener {
                if(wishlist==1){
                    wishlist = 0
                    clickOnUnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)
                }else{
                    wishlist = 1
                    clickOnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
                }

            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ItemPropertyLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: HomeExploreOtherStateAdapter.MyViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<RemainingProperty>() {
        override fun areItemsTheSame(
            oldItem: RemainingProperty,
            newItem: RemainingProperty
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RemainingProperty,
            newItem: RemainingProperty
        ): Boolean {
            return oldItem == newItem
        }
    }
    fun uppercaseLowerCase( text : String) : String {
     return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }

}