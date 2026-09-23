package com.btjnonbrokerage.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.FeaturedProperty.Property
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.ItemPropertyLayoutBinding


class FeaturedSearchAdapter(
    val onclick: (Property) -> Unit,
    val clickOnLike: (Property) -> Unit,
    val clickOnUnLike: (Property) -> Unit
) : ListAdapter<Property, FeaturedSearchAdapter.MyViewHolder>(Diff) {
    inner class MyViewHolder(val binding: ItemPropertyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var favoritList = 0
        fun bind(item: Property) {
            val isFavLocal = com.btjnonbrokerage.Base.MySharedPreferences(binding.root.context).getFavoritesList().any { it.property_id == item.property_id }
            Glide.with(binding.root.context).load(item.images[0]).placeholder(R.drawable.home_ph) .into(binding.ivPropertyImage)
            binding.tvHomeSaleTitle.text = item.pr_name
            binding.tvLocationCity.text = item.city_name
            binding.tvLocationState.text = item.state_name
            binding.propertyCategory.text = item.category
            binding.tvPricing.text = item.price+"/"
            binding.tvMonthOrYear.text = item.plan_type
            binding.ratingTv.text = item.avg_rating.toString()
            if (item.wishlist == 1 || isFavLocal) {
                favoritList = 1
                binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
            }
            else {
                favoritList = 0
                binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)
            }


            binding.ivHeartIcon.setOnClickListener {
                if (favoritList == 1) {
                    favoritList = 0
                    clickOnUnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)

                } else {
                    favoritList = 1
                    clickOnLike(item)
                    binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
                }
            }
            itemView.setOnClickListener {
                onclick(item)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemPropertyLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }


}