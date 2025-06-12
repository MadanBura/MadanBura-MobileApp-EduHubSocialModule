package com.connect.eduhubconnect.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.connect.eduhubconnect.viewmodel.UserViewModel
import com.connect.eduhubconnect.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.adapters.ExperienceAdapter
import com.connect.eduhubconnect.adapters.EducationAdapter
import com.connect.eduhubconnect.adapters.SelfPostAdapter
import com.connect.eduhubconnect.databinding.FragmentProfileBinding
import com.connect.eduhubconnect.dto.UserProfileDTO
import com.connect.eduhubconnect.utils.TokenManager
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val userViewModel: UserViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = tokenManager.getUserId()
        if (userId != null) {
            userViewModel.getUserProfile(userId)
        }

        lifecycleScope.launchWhenStarted {
            userViewModel.userProfile.collect { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBarPro.visibility = View.VISIBLE
                        hideContentViews() // Hide content while loading
                    }
                    is ResultState.Success -> {
                        binding.progressBarPro.visibility = View.GONE
                        showContentViews() // Show content after data is loaded
                        val userProfile = result.data
                        updateUI(userProfile)
                    }
                    is ResultState.Error -> {
                        binding.progressBarPro.visibility = View.GONE
                        showError() // Handle error state
                    }
                }
            }
        }
    }

    private fun updateUI(userProfile: UserProfileDTO) {

        val fullImageUrl = if (userProfile.profileImageUrl.contains("localhost"))
            userProfile.profileImageUrl.replace("localhost", "10.0.102.40")
        else (userProfile.profileImageUrl.startsWith("http"))
        "${userProfile.profileImageUrl}"

        // Load profile image using Glide
        view?.let {
            Glide.with(it.context)
                .load(fullImageUrl)
                .into(binding.profilePic)
        }


        binding.userName.text = userProfile.userName
        binding.userBio.text = userProfile.bio
        binding.aboutContent.text = userProfile.about

        // Handle address display
        val cityAddress = if (userProfile.addressDTO?.city != null && userProfile.addressDTO?.street != null) {
            "${userProfile.addressDTO.city} ${userProfile.addressDTO.street}"
        } else {
            "City and Address Unavailable"
        }
        binding.cityAddress.text = cityAddress

        // Experience Adapter
        val experienceAdapter = ExperienceAdapter(userProfile.experiences)
        binding.experienceRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.experienceRecyclerView.adapter = experienceAdapter

        // Education Adapter
        val educationAdapter = EducationAdapter(userProfile.educationList)
        binding.educationRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.educationRecyclerView.adapter = educationAdapter

        // Self Post Adapter
        val selfPostAdapter = SelfPostAdapter(userProfile.selfPosts)
        binding.selfPostRCV.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.selfPostRCV.adapter = selfPostAdapter
    }

    private fun hideContentViews() {
        binding.experienceRecyclerView.visibility = View.GONE
        binding.educationRecyclerView.visibility = View.GONE
        binding.selfPostRCV.visibility = View.GONE
        binding.cityAddress.visibility = View.GONE
        binding.userName.visibility = View.GONE
        binding.userBio.visibility = View.GONE
        binding.aboutContent.visibility = View.GONE
    }

    private fun showContentViews() {
        binding.experienceRecyclerView.visibility = View.VISIBLE
        binding.educationRecyclerView.visibility = View.VISIBLE
        binding.selfPostRCV.visibility = View.VISIBLE
        binding.cityAddress.visibility = View.VISIBLE
        binding.userName.visibility = View.VISIBLE
        binding.userBio.visibility = View.VISIBLE
        binding.aboutContent.visibility = View.VISIBLE
    }

    private fun showError() {
        // Show an error message or retry button if needed
        binding.cityAddress.text = "Error loading user data"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
