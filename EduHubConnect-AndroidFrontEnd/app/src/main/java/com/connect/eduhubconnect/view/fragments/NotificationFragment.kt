    package com.connect.eduhubconnect.view.fragments

    import android.os.Bundle
    import android.util.Log
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.lifecycleScope
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.connect.eduhubconnect.R
    import com.connect.eduhubconnect.adapters.NotificationAdapter
    import com.connect.eduhubconnect.databinding.FragmentNotificationBinding
    import com.connect.eduhubconnect.model.NotificationResponseItem
    import com.connect.eduhubconnect.utils.ResultState
    import com.connect.eduhubconnect.utils.TokenManager
    import com.connect.eduhubconnect.viewmodel.NotificationViewModel
    import dagger.hilt.android.AndroidEntryPoint
    import kotlinx.coroutines.launch
    import javax.inject.Inject

    @AndroidEntryPoint
    class NotificationFragment : Fragment() {


        private lateinit var binding: FragmentNotificationBinding
        private val notificationViewModel: NotificationViewModel by viewModels()

        @Inject
        lateinit var tokenManager: TokenManager

        private lateinit var notificationAdapter: NotificationAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentNotificationBinding.inflate(inflater, container, false)
            return binding.root
        }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            tokenManager.getUserId()?.let {
                notificationViewModel.getNotifications(it)
            }

            observerNotificationList()
        }

        private fun observerNotificationList() {
            viewLifecycleOwner.lifecycleScope.launch {
                notificationViewModel.notifications.collect { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            // Show progress bar if needed
                        }
                        is ResultState.Success -> {
                            result.data?.let { notificationList ->
                                Log.d("NotificationFragment", "Loaded notifications: ${notificationList.size}")
                                if (notificationList.isNotEmpty()) {
                                    notificationAdapter = NotificationAdapter(notificationList)
                                    binding.notificationsRecyclerView.layoutManager =
                                        LinearLayoutManager(requireContext())
                                    binding.notificationsRecyclerView.adapter = notificationAdapter
                                }
                            }
                        }
                        is ResultState.Error -> {
                            Log.e("NotificationFragment", "Error: ${result.exception.message}")
                            // Optionally show error UI
                        }
                    }
                }
            }
        }

    }