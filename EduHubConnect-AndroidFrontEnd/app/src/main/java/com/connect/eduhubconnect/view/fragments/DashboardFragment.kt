package com.connect.eduhubconnect.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.connect.eduhubconnect.adapters.FeedAdapter
import com.connect.eduhubconnect.databinding.FragmentDashboardBinding
import com.connect.eduhubconnect.dto.CommentDTO
import com.connect.eduhubconnect.dto.PostDTO
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
import com.connect.eduhubconnect.view.RecentChatActivity
import com.connect.eduhubconnect.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    private var feedAdapter: FeedAdapter? = null
    private var postList: List<PostDTO> = emptyList()
    private var allComments: List<CommentDTO> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager.getUserId()?.let { userId ->
            userViewModel.getDashboard(userId)
            observeDashboardData(userId)
            observeCommentUpdates(userId)
        }


        //click on messgaIcon-> navigate to RecentChatActivity


    }

    private fun observeDashboardData(userId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.dashboardState.collect { state ->
                when (state) {
                    is ResultState.Loading -> binding.prgBar.visibility = View.VISIBLE

                    is ResultState.Success -> {
                        binding.prgBar.visibility = View.GONE
                        postList = state.data.feedPosts ?: emptyList()

//                        postList.forEach { userViewModel.getCommentsForPost(it.id) } // fetch all post comments (use actual backend logic)
                        setupAdapter(userId)
                    }

                    is ResultState.Error -> {
                        binding.prgBar.visibility = View.GONE
                        Toast.makeText(requireContext(), state.exception.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun observeCommentUpdates(userId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.comments.collect { state ->
                when (state) {
                    is ResultState.Success -> {
//                        allComments = state.data
//                        setupAdapter(userId) // update adapter with new comments

                        allComments = state.data
                        Log.e("USERCOMMENTDASH", allComments.toString())

                        binding.feedRecyclerView.post {
                            feedAdapter?.setComments(allComments)
                        }
                    }

                    is ResultState.Error -> {
                        Toast.makeText(requireContext(), "Error loading comments", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setupAdapter(userId: Long) {
        feedAdapter = FeedAdapter(
            postList = postList,
            likeClick = { post ->
                handleLikeClick(userId, post)
            },
            addCommentClick = { post, commentText ->
                userViewModel.addComment(userId, post.id, commentText)
                userViewModel.getCommentsForPost(post.id)
            },
//            comments = allComments.toMutableList()
        )

        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.feedRecyclerView.adapter = feedAdapter
    }

    private fun handleLikeClick(userId: Long, post: PostDTO) {
        userViewModel.toggleLike(userId, post.id)

        // Optimistic UI update
        post.likedByUser = !post.likedByUser
        post.likeCount += if (post.likedByUser) 1 else -1

        feedAdapter?.likedPosts?.let { likedSet ->
            if (post.likedByUser) likedSet.add(post.id) else likedSet.remove(post.id)
        }

        feedAdapter?.notifyDataSetChanged()
        observeLikeCount(post)
    }

    private fun observeLikeCount(post: PostDTO) {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.likeCountState.collect { state ->
                when (state) {
                    is ResultState.Success -> {
                        post.likeCount = state.data
                        feedAdapter?.notifyDataSetChanged()
                    }

                    is ResultState.Error -> {
                        Toast.makeText(requireContext(), "Failed to fetch updated likes", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
