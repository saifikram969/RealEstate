package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.HomeModel.FeaturedProperty
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.LayoutFeaturedEstatesBinding

class HomeFeaturedAdapter(val onClick : (FeaturedProperty) -> Unit, val clickOnLike : (FeaturedProperty)->  Unit, val clickOnUnLike : (FeaturedProperty)-> Unit) : ListAdapter<FeaturedProperty, HomeFeaturedAdapter.MyViewHolder>(Diff) {
    inner class MyViewHolder(val binding: LayoutFeaturedEstatesBinding) : RecyclerView.ViewHolder(binding.root) {
        var wishlist = 0
        fun bind(item: FeaturedProperty) {
            val isFavLocal = com.btjnonbrokerage.Base.MySharedPreferences(binding.root.context).getFavoritesList().any { it.property_id == item.property_id }
            if(item.wishlist.toString() == "1" || isFavLocal){
                wishlist = 1
                binding.heartIcon.setImageResource(R.drawable.like_heart)
            }else{
                wishlist = 0
                binding.heartIcon.setImageResource(R.drawable.unlike_heart)
            }
            binding.heartIcon.setOnClickListener {
                if(wishlist==1){
                    wishlist = 0
                    clickOnUnLike(item)
                    binding.heartIcon.setImageResource(R.drawable.unlike_heart)
                }else{
                    wishlist = 1
                    clickOnLike(item)
                    binding.heartIcon.setImageResource(R.drawable.like_heart)
                }
            }
            itemView.setOnClickListener {
                onClick(item)
            }



            try {
                Glide.with(binding.root.context).load(item.images[0]).into(binding.buildingImage)
                binding.propertyName.text = item.pr_name.replaceFirstChar { it.uppercase() }
                binding.addressCity.text = item.city_name+","
                binding.addressState.text = item.state_name
                binding.priceTv.text = ("₹ " + item.price +"/")
                binding.propertyCategory.text = item.category
                binding.ratingTv.text = item.avg_rating.toString()
                binding.tvMonthOrYear.text = item.plan_type.toString()

            }catch (e:Exception){

            }




        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       return  MyViewHolder(LayoutFeaturedEstatesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }



    object Diff : DiffUtil.ItemCallback<FeaturedProperty>() {
        override fun areItemsTheSame(oldItem: FeaturedProperty, newItem: FeaturedProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FeaturedProperty, newItem: FeaturedProperty): Boolean {
            return oldItem == newItem
        }
    }


}