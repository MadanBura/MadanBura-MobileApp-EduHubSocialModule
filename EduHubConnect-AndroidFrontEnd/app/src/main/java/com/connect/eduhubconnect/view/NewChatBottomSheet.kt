package com.connect.eduhubconnect.view

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.adapters.UserDtoAdapter
import com.connect.eduhubconnect.dto.UserDTO
import com.connect.eduhubconnect.model.ChatMessage
import com.connect.eduhubconnect.utils.CONSTANTS
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.viewmodel.ChatViewModel
import com.connect.eduhubconnect.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class NewChatBottomSheet(
    private val senderEmail: String
) : BottomSheetDialogFragment() {

    private val chatViewModel: ChatViewModel by activityViewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var userAdapter: UserDtoAdapter
    private lateinit var recipientInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var textWatcher: TextWatcher
    private var selectedUser: UserDTO? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_new_chat, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerContacts)
        recipientInput = view.findViewById(R.id.editTextTo)
        messageInput = view.findViewById(R.id.editMessage)
        val userLayout = view.findViewById<View>(R.id.userLayout)
        val sendButton = view.findViewById<Button>(R.id.btnSend)
        val selectedUsernameView = view.findViewById<TextView>(R.id.uName)
        val selectedUserBioView = view.findViewById<TextView>(R.id.uBio)
        val selectedUserProfilePic = view.findViewById<ImageView>(R.id.uPic)

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedUser = null
                userAdapter.filterList(s.toString())
                recyclerView.visibility = View.VISIBLE
                userLayout.visibility = View.GONE
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        recipientInput.addTextChangedListener(textWatcher)

        userAdapter = UserDtoAdapter { user ->
            selectedUser = user

            recipientInput.removeTextChangedListener(textWatcher)
            recipientInput.setText(user.email)
            recipientInput.addTextChangedListener(textWatcher)

            selectedUsernameView.text = user.name
            selectedUserBioView.text = user.bio
            user.profileImage
                ?.replace("localhost", CONSTANTS.IP_ADD)
                .let { url ->
                    Glide.with(this).load(url).into(selectedUserProfilePic)
                }

            recyclerView.visibility = View.GONE
            userLayout.visibility = View.VISIBLE
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userList.collect { result ->
                if (result is ResultState.Success) {
                    userAdapter.submitList(result.data)
                }
            }
        }
        userViewModel.getAllUsers()

        sendButton.setOnClickListener {
            val text = messageInput.text.toString().trim()
            if (selectedUser != null && text.isNotEmpty()) {
                val chatMessage = ChatMessage(
                    sender = senderEmail,
                    recipient = selectedUser!!.email,
                    content = text,
                    timestamp = LocalDateTime.now().toString()
                )
                Log.d("NewChatBottomSheet", "Sending message: $chatMessage")
                chatViewModel.sendMessage(chatMessage)
                dismiss()
            } else {
                Toast.makeText(context, "Select a user and enter a message", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
}

