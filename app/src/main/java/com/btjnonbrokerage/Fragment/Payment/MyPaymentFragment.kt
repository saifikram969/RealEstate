package com.btjnonbrokerage.Fragment.Payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.Fragment.Payment.MyPayment.MyPaymentRequest
import com.btjnonbrokerage.MVVM.ViewModel.PaymentViewModel
import com.btjnonbrokerage.adapters.PaymentDetailAdapter
import com.btjnonbrokerage.databinding.FragmentMyPaymentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPaymentFragment : BaseFragment<FragmentMyPaymentBinding>() {

    @Inject
    lateinit var viewModel: PaymentViewModel
    private val paymentDetailAdapter by lazy {
        PaymentDetailAdapter(onItemClicked = {
            val action = MyPaymentFragmentDirections.actionMyPaymentFragmentToSinglepaymentDetailFragment(it)
            findNavController().navigate(action)
        })
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyPaymentBinding = FragmentMyPaymentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        commonToolbar()
        setPaymentDetailAdapter()
        myPaymentApiCall()
        observeMyPayment()

    }

    private fun setPaymentDetailAdapter() {
        binding.rvMyPayment.apply {
            adapter = paymentDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun myPaymentApiCall() {
        val uid = MySharedPreferences(requireContext()).getToken()
        if (uid != null) {
            viewModel.myPayment(MyPaymentRequest(uid))
        }
    }

    private fun observeMyPayment() {
        viewModel.myPaymentLiveData.observe(viewLifecycleOwner) {
            if (it.status == "success") {
                paymentDetailAdapter.submitList(it.payment_history)
                binding.llTransactionProperty.visibility = View.GONE
            } else {
                binding.llTransactionProperty.visibility = View.VISIBLE
            }
        }
    }
    private fun commonToolbar(){
        binding.apply {
            commonToolbarPayment.toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }
            commonToolbarPayment.toolbarTitle.text ="Payment History"
        }

    }


}