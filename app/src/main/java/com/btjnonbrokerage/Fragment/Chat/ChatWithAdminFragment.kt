package com.btjnonbrokerage.Fragment.Chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.fatch.FetchMessagesRequest
import com.btjnonbrokerage.MVVM.APIModel.ChatWithAdmin.send.SendMessagesRequest
import com.btjnonbrokerage.MVVM.ViewModel.MainViewModel
import com.btjnonbrokerage.adapters.chatAdapter.ChatAdapter
import com.btjnonbrokerage.databinding.FragmentChatWithAdminBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatWithAdminFragment : BaseFragment<FragmentChatWithAdminBinding>() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private val chatAdapter by lazy {
       ChatAdapter()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatWithAdminBinding  = FragmentChatWithAdminBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.sendMessageButton.setOnClickListener {
            val messages = binding.chatMessageInput.text.toString().trim()
            if(messages.isNotEmpty()){
                sendMessages(messages)
                binding.chatMessageInput.text.clear()
//                binding.chatRecyclerView.scrollToPosition(chatAdapter.itemCount - 1)
            }

        }

        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(3000)
                fetchMessagesApiCall()
            }
        }



        setAdapter()
        fetchMessagesApiCall()
        fetchMessagesResponse()
    }

    private fun fetchMessagesApiCall(){
        val uid = MySharedPreferences(requireContext()).getToken()
        uid?.let { FetchMessagesRequest(it) }?.let { mainViewModel.fetchMessagesFun(it) }
    }
    private fun fetchMessagesResponse(){
        mainViewModel.fetchMessagesLiveData.observe(viewLifecycleOwner){
            chatAdapter.submitList(it.messages)

        }
    }
    private  fun sendMessages(messages : String){
        val uid = MySharedPreferences(requireContext()).getToken()
        uid?.let { SendMessagesRequest(messages, it) }?.let { mainViewModel.sendMessagesFun(it) }
    }
    private fun setAdapter(){
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
        }
    }





}