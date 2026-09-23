package com.btjnonbrokerage.Fragment.MainFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.btjnonbrokerage.Base.BaseFragment
import com.btjnonbrokerage.Base.MySharedPreferences
import com.btjnonbrokerage.MVVM.APIModel.Notification.NotificationList.NotificationsRequest
import com.btjnonbrokerage.MVVM.ViewModel.AuthViewModel
import com.btjnonbrokerage.adapters.NotificationListAdapter
import com.btjnonbrokerage.databinding.FragmentNotificationListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListFragment : BaseFragment<FragmentNotificationListBinding>() {

    @Inject
    lateinit var authViewModel: AuthViewModel

    private var page = 1
    private var totalCount = 0

    // Adapter initialization using lazy is good
    private val notificationListAdapter by lazy {
        NotificationListAdapter()
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNotificationListBinding =
        FragmentNotificationListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.commonToolbarNotifications.apply {
            toolbarTitle.text = "Notifications"
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }


        // Set up RecyclerView adapter
        setupNotificationsAdapter()

        // Call API and observe the response
        fetchNotifications(1)
        observeNotificationResponse()
        setPagination()
    }

    // Set up RecyclerView adapter
    private fun setupNotificationsAdapter() {
        binding.rvNotification.apply {
            adapter = notificationListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Fetch notifications from the API
    private fun fetchNotifications(page: Int) {
        val uid = MySharedPreferences(requireContext()).getToken()
        uid?.let {
            authViewModel.notificationViewModel(NotificationsRequest(it, page.toString()))
        } ?: run {
            // Handle null token, e.g., show error message or redirect user
            showErrorSnackBar("User is not authenticated", false)
        }
    }

    // Observe the LiveData for notification responses
    private fun observeNotificationResponse() {

        authViewModel.notificationLiveData.observe(viewLifecycleOwner) {

            if (it.status == "success") {
                totalCount = it.total_count.toInt()
                if (page > 1) {
                    val mList = notificationListAdapter.currentList.toMutableList()
                    mList.addAll(it.notifications)
                    notificationListAdapter.submitList(mList)
                }
                else {
                    notificationListAdapter.submitList(it.notifications)

                }
            }
            else{
                binding.llNoNotification.visibility = View.VISIBLE
                binding.rvNotification.visibility = View.GONE
            }
        }


    }


    private fun setPagination() {
        binding.rvNotification.setOnScrollChangeListener { view, _, _, _, _ ->
            if (view?.canScrollVertically(RecyclerView.VERTICAL) == false) {
                if (notificationListAdapter.currentList.size < totalCount) {
                    page++
                    fetchNotifications(page)
                }
            }
        }
    }


}
