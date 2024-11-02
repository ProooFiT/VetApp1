package com.example.navhost


import Screen.PetViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel, navController: NavHostController){

    val viewModel: PetViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login", modifier = modifier){
        composable(route = "login"){
            LoginScreen(modifier,navController,authViewModel)
        }
        composable(route = "signup"){
            SignupScreen(modifier,navController,authViewModel)
        }
        composable(route = "home"){
            HomeScreen(modifier,navController,authViewModel, viewModel)
        }
        composable(route = "pet"){
            PetScreen(modifier,navController,authViewModel, viewModel)
        }
        composable(route = "settings"){
            SettingScreen(modifier,navController,authViewModel)
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
//    val hasNews: Boolean,
//    val badgeCount: Int? = null
)

@Composable
fun BottomNavigationTheme(navController: NavController){

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BottomNavigationItem(
            title = "Pets",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            route = "pet"
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "settings"
        ),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if(currentRoute != "login" && currentRoute != "signup"){
        NavigationBar() {
            items.forEach(){ item ->
                NavigationBarItem(currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(item.title)
                    })
            }
        }
    }
}


//fun NavController.navigateSingleTopTo(route: String) =
//    this.navigate(route) {
//        popUpTo(
//            this@navigateSingleTopTo.graph.findStartDestination().id
//        ) {
//            saveState = true
//        }
//        launchSingleTop = true
//        restoreState = true
//    }
//
