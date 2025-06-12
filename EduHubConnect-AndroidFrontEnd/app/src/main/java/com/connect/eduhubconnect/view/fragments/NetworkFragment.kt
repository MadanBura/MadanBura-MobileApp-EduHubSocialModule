package com.connect.eduhubconnect.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.adapters.PendingFriendRequestAdapter
import com.connect.eduhubconnect.adapters.SeeAvailableFriendsAdapter
import com.connect.eduhubconnect.databinding.FragmentNetworkBinding
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
import com.connect.eduhubconnect.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NetworkFragment : Fragment() {

    private var _binding: FragmentNetworkBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = tokenManager.getUserId()
        if (userId != null) {
            userViewModel.getDashboard(userId)
            userViewModel.seeAllUsersAvailable(userId)
        }

        observePendingFriendList()
        observeSeeAllFriends()
        observeSendReqRes(view)
    }

    private fun observeSendReqRes(view: View){
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.sendFriendReqRes.collect{
                when(it){
                    is ResultState.Loading->{}
                    is ResultState.Success->{

                    }
                    is ResultState.Error->{}
                }
            }
        }
    }



    private fun observePendingFriendList() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.dashboardState.collect { state ->
                when (state) {
                    is ResultState.Loading -> {
                        // Optional: Show loading indicator
                    }

                    is ResultState.Success -> {
                        val pendingRequests = state.data.pendingRequests
                        val adapter = PendingFriendRequestAdapter(pendingRequests,
                            onAcceptClick = { request ->

                            },
                            onRejectClick = { request ->
                            }
                        )
                        binding.friendReqRCV.layoutManager = LinearLayoutManager(requireContext())
                        binding.friendReqRCV.adapter = adapter
                    }

                    is ResultState.Error -> {
                        // Optional: Show error message
                    }
                }
            }
        }
    }

    private fun observeSeeAllFriends() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.friendList.collect { state ->
                when (state) {
                    is ResultState.Loading -> {
                        // Optional: Show shimmer or loading
                    }

                    is ResultState.Success -> {
                        val availableFriends = state.data

                        val adapter = SeeAvailableFriendsAdapter(availableFriends) { selectedUser ->

                            val currentUserId = tokenManager.getUserId() ?: 0L
                            val receiverId= selectedUser.id

                            userViewModel.sendFriendRequest(senderId = currentUserId, receiverId=receiverId)
                        }

                        binding.friendListRcv.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        binding.friendListRcv.adapter = adapter
                    }

                    is ResultState.Error -> {
                        // Optional: show error
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
