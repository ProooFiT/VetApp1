package com.example.vetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.navhost.AuthViewModel
import com.example.navhost.BottomNavigationTheme
import com.example.navhost.MyAppNavigation
import com.example.vetapp.ui.theme.VetAppTheme

/**
 * Main activity of the VetApp application.
 * This activity serves as the entry point of the app, initializing navigation,
 * theming, and the ViewModel for authentication.
 */
class MainActivity : FragmentActivity() {
    /**
     * Manager responsible for handling theme-related changes dynamically.
     */
//    private lateinit var themeManager: ThemeManager

    /**
     * Called when the activity is created.
     * Initializes the ThemeManager, ViewModel, and sets up the Compose content.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize ThemeManager
//        themeManager = ThemeManager(this)

        val authViewModel: AuthViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            VetAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationTheme(navController = navController) }
                ) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel,
                        navController = navController
                    )
                }
            }
        }
    }}

    /**
     * Called when the activity enters the Resumed state.
     * Starts listening for theme-related changes via the ThemeManager.
     */
//    override fun onResume() {
//        super.onResume()
//        themeManager.startListening()
//    }

    /**
     * Called when the activity is about to enter the Paused state.
     * Stops listening for theme-related changes via the ThemeManager.
     */
//    override fun onPause() {
//        super.onPause()
//        themeManager.stopListening()
//    }
//}
