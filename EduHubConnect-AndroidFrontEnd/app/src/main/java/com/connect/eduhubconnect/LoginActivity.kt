package com.connect.eduhubconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.connect.eduhubconnect.databinding.ActivityLoginBinding
import com.connect.eduhubconnect.model.AuthRequest
import com.connect.eduhubconnect.repository.UserRepository
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
import com.connect.eduhubconnect.view.activities.DashboardActivity
import com.connect.eduhubconnect.viewmodel.UserViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: UserViewModel by viewModels()

    @Inject
    lateinit var tokenManager : TokenManager
    @Inject
    lateinit var userRepository:UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvSignUp.setOnClickListener {
            Intent(this, MainActivity::class.java).also { startActivity(it) }
        }

        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

//                val email = "swapnil@test.com"
//                val password= "swapnil1234"
                val loginRequest = AuthRequest(email, password)
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLogin.visibility = View.INVISIBLE

                viewModel.login(loginRequest)
            }
        }

        lifecycleScope.launch {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is ResultState.Loading -> {
//                        binding.progressBar.visibility = View.VISIBLE
//                        binding.btnLogin.visibility = View.INVISIBLE
                    }

                    is ResultState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.visibility = View.VISIBLE
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                        // Save userId first if needed
                        tokenManager.saveUserId(state.data.userId)

                        // Send FCM token
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            tokenManager.storeFCMToken(token)
                            lifecycleScope.launch {
                                userRepository.saveFcmToken(state.data.userId, token)
                                Log.d("FCM", "Token sent to backend after login")
                            }
                        }

                        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                        finish()
                    }

                    is ResultState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnLogin.visibility = View.VISIBLE
                        Toast.makeText(this@LoginActivity, state.exception.message ?: "Login failed", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmail.error = "Email is required"
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Enter a valid email"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password is required"
            return false
        }
        if (password.length < 6) {
            binding.etPassword.error = "Password must be at least 6 characters"
            return false
        }

        return true
    }
}