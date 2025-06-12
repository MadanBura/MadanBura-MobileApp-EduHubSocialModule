package com.connect.eduhubconnect.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.connect.eduhubconnect.view.fragments.NetworkFragment
import com.connect.eduhubconnect.view.fragments.NotificationFragment
import com.connect.eduhubconnect.view.fragments.PostFragment
import com.connect.eduhubconnect.view.fragments.ProfileFragment
import com.connect.eduhubconnect.databinding.ActivityDashboardBinding
import com.connect.eduhubconnect.view.fragments.DashboardFragment
import com.connect.eduhubconnect.R
import com.connect.eduhubconnect.utils.ResultState
import com.connect.eduhubconnect.utils.TokenManager
import com.connect.eduhubconnect.view.RecentChatActivity
import com.connect.eduhubconnect.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var navController: NavController

    val userViewModel: UserViewModel by viewModels()
    private var userName = ""
    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewDashBoard) as NavHostFragment
        navController = navHostFragment.navController

        // 🔗 Connect BottomNav with NavController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setupWithNavController(navController)

        binding.messageIcon.setOnClickListener {
            Intent(this, RecentChatActivity::class.java).also {
                it.putExtra("userEmail", userName)
                startActivity(it)
            }
        }


        tokenManager.getUserId()?.let { userViewModel.getDashboard(it) }

        lifecycleScope.launch {
            userViewModel.dashboardState.collect{
                when(it){
                    is ResultState.Loading ->{}
                    is ResultState.Success ->{


                        userName = it.data.userProfile.email
                        val count = it.data.unread_notificationCount
                      updateNotificationBadge(bottomNav,count)
                    }
                    is ResultState.Loading ->{}
                    else ->{}
                }
            }
        }

//        loadFragment(DashboardFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(DashboardFragment())
                R.id.nav_network -> loadFragment(NetworkFragment())
                R.id.nav_post -> loadFragment(PostFragment())
                R.id.nav_notifications -> loadFragment(NotificationFragment())
                R.id.nav_profile -> loadFragment(ProfileFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerViewDashBoard, fragment)
            .commit()
    }


    private fun updateNotificationBadge(bottomNav: BottomNavigationView, count: Int) {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_notifications)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count // Set the badge number dynamically
        } else {
            badge.isVisible = false
        }
    }
}
