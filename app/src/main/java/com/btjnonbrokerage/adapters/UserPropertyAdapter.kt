package com.btjnonbrokerage.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.btjnonbrokerage.MVVM.APIModel.viewproperty.Property
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.ItemPropertyLayoutBinding

class UserPropertyAdapter(val onItemClicked: (Property) -> Unit, val notVerified : () -> Unit, val clickOnLike : (Property)->  Unit, val clickOnUnLike : (Property)-> Unit, val onEditIconClicked: (Property) -> Unit
                          ) : ListAdapter<Property, UserPropertyAdapter.MyViewHolder>(diff) {
    inner class MyViewHolder(val binding: ItemPropertyLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var wishlist = 0



        fun bind(item : Property) {
            val isFavLocal = com.btjnonbrokerage.Base.MySharedPreferences(binding.root.context).getFavoritesList().any { it.property_id == item.property_id }
            binding.ivVerify.visibility = View.VISIBLE
            binding.propertyCategory.visibility = View.VISIBLE


            if(item.wishlist == 1 || isFavLocal){
                wishlist = 1
                binding.ivHeartIcon.setImageResource(R.drawable.like_heart)
            }
            else{
                wishlist = 0
                binding.ivHeartIcon.setImageResource(R.drawable.unlike_heart)
            }

                if(item.status == "1"){
                    binding.ivVerify.setImageResource(R.drawable.tickmark)
                }else{
                    binding.ivVerify.setImageResource(R.drawable.verified)
                }







            Glide.with(binding.root.context).load(item.property_images[0]).placeholder(R.drawable.home_ph).into(binding.ivPropertyImage)
            binding.tvHomeSaleTitle.text = uppercaseLowerCase(item.pr_name)
            binding.tvLocationCity.text = item.city_name
            binding.tvLocationState.text = item.state_name
            binding.propertyCategory.text = item.category
            binding.tvPricing.text = item.price+"/"
            binding.tvMonthOrYear.text = item.plan_type
            binding.ratingTv.text = item.avg_rating.toString()
            binding.root.setOnClickListener {

                if(item.status == "1"){
                    onItemClicked(item)
                }else{
                    notVerified()
                }

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

    object diff : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return  oldItem == newItem
        }
    }
    fun uppercaseLowerCase( text : String) : String {
        if (text.isEmpty()) return ""
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }

}