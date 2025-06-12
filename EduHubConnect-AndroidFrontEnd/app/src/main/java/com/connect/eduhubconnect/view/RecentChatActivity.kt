package com.connect.eduhubconnect.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.adapters.RecentChatAdapter
import com.connect.eduhubconnect.dto.RecentChatRes
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.viewmodel.ChatViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentChatActivity : AppCompatActivity() {

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var recentChatAdapter: RecentChatAdapter
    private val recentChats = mutableListOf<RecentChatRes>()
    private lateinit var currentUserEmail: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent)

        currentUserEmail = intent.getStringExtra("userEmail") ?: ""

        val recyclerView = findViewById<RecyclerView>(R.id.recentChatsRecyclerView)
        recentChatAdapter = RecentChatAdapter(recentChats) { recentChat ->
            // Navigate to ChatActivity with selected user
            startActivity(Intent(this, ChatActivity::class.java).apply {
                putExtra("senderEmail", currentUserEmail)
                putExtra("recipientEmail", recentChat.userEmail)
            })
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RecentChatActivity)
            adapter = recentChatAdapter
        }

        // Observe LiveData
        chatViewModel.recentChatsLiveData.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    // Show loading spinner (optional)
                }
                is ResultState.Success -> {
                    recentChats.clear()
                    recentChats.addAll(result.data)
                    recentChatAdapter.notifyDataSetChanged()
                }

                is ResultState.Error -> {
                    Log.e("RecentChatActivity", "Error: ${result.exception.message}")
                    // Show error to the user (optional)
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.newchat)
            .setOnClickListener {
                NewChatBottomSheet(currentUserEmail)
                    .show(supportFragmentManager, "NewChatBottomSheet")
                Log.e("CurrentUserEmail", currentUserEmail)
            }
    }


    override fun onResume() {
        super.onResume()
        chatViewModel.fetchRecentChats(currentUserEmail)

    }
}
