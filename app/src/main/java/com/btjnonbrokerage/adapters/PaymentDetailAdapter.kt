package com.btjnonbrokerage.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Fragment.Payment.MyPayment.PaymentHistory
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.MyPaymentDetailsBinding

class PaymentDetailAdapter(val onItemClicked: (PaymentHistory) -> Unit) :
    ListAdapter<PaymentHistory, PaymentDetailAdapter.MyViewHolder>(Diff) {

    inner class MyViewHolder(val binding: MyPaymentDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PaymentHistory) {

            binding.tvPayType.text = uppercaseLowerCase(item.pay_type.toString())
            binding.tvLandlordName.text = uppercaseLowerCase(item.landlord_name.toString())
            binding.tvLandloadPhone.text = uppercaseLowerCase(item.landlord_phone.toString())
            binding.tvPaymentMode.text = uppercaseLowerCase(item.pay_mode.toString())
            binding.tvDate.text = item.date
            binding.tvPaymentStatus.text = uppercaseLowerCase(item.pay_status.toString())
            if(item.pay_status=="success"){
                binding.tvPaymentStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green))
            }else{
                binding.tvPaymentStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.colorSnackBarError))
            }

            itemView.setOnClickListener {
                onItemClicked(item)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentDetailAdapter.MyViewHolder {
        return MyViewHolder(
            MyPaymentDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PaymentDetailAdapter.MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object Diff : DiffUtil.ItemCallback<PaymentHistory>() {
        override fun areItemsTheSame(oldItem: PaymentHistory, newItem: PaymentHistory): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PaymentHistory, newItem: PaymentHistory): Boolean {
            return oldItem == newItem
        }
    }

    fun uppercaseLowerCase( text : String) : String {
        return   text.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() }
    }
}