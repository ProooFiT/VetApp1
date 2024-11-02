package com.example.vetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.navhost.AuthViewModel
import com.example.navhost.BottomNavigationTheme
import com.example.navhost.MyAppNavigation
import com.example.vetapp.ui.theme.VetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            VetAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {BottomNavigationTheme(navController = navController  )}) { innerPadding ->
                    MyAppNavigation(modifier = Modifier.padding(innerPadding),authViewModel = authViewModel, navController = navController )
                }
            }
        }
    }
}


