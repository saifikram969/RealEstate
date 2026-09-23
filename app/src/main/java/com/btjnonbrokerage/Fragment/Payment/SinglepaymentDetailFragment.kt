package com.btjnonbrokerage.Fragment.Payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.R
import com.btjnonbrokerage.databinding.FragmentSinglepaymentDetailBinding


class SinglepaymentDetailFragment : BaseFragment<FragmentSinglepaymentDetailBinding>() {

    private val args by navArgs<SinglepaymentDetailFragmentArgs>()
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSinglepaymentDetailBinding  = FragmentSinglepaymentDetailBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
           setTextCommon()


        }catch (_:Exception){

        }




    }
    private fun setTextInUPI(){
        binding.tvUpiId.text = args.paymentHIstory.upi_id
    }
    private fun setTextAccount(){
        binding.tvAccountNumber.text = args.paymentHIstory.acc_num
        binding.tvIfscCode.text = args.paymentHIstory.ifsc
    }

    private fun setTextCommon(){
        binding.tvPayToken.text = args.paymentHIstory.pay_token
        binding.tvPaymentType.text = args.paymentHIstory.pay_type
        binding.tvPaymentMode.text = args.paymentHIstory.pay_mode
        binding.tvRentAmount.text = args.paymentHIstory.rent_amt
        binding.tvLandlordName.text = args.paymentHIstory.landlord_name
        binding.tvLandlordPhone.text = args.paymentHIstory.landlord_phone
        binding.tvName.text = args.paymentHIstory.name
        binding.tvPhone.text = args.paymentHIstory.phone
        binding.tvPaymentId.text = args.paymentHIstory.payment_id
        binding.BHKType.text = args.paymentHIstory.bhk_type
        binding.tvPropertyAddress.text = args.paymentHIstory.property_address
        binding.tvBeneficiarPan.text = args.paymentHIstory.beneficiar_pan
        binding.tvTenantPan.text = args.paymentHIstory.tenant_pan
        binding.tvPaymentStatus.text = args.paymentHIstory.pay_status
        binding.paymentDate.text = args.paymentHIstory.date
        binding.tvPaymentTime.text = args.paymentHIstory.time
    }


}