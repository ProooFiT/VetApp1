package com.example.navhost

import Screen.AppointmentScreen
import Screen.PetAppHisScreen
import Screen.PetViewModel
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vetapp.SettingsScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

/**
 * Funkcja composable, która zarządza przepływem nawigacji w aplikacji.
 * Definiuje wszystkie ścieżki i odpowiadające im ekrany. Każdy ekran
 * jest composable, do którego można przejść w zależności od działań użytkownika.
 *
 * @param modifier Modyfikator do zastosowania w komponencie.
 * @param authViewModel ViewModel zarządzający logiką uwierzytelniania.
 * @param navController Kontroler nawigacji wykorzystywany do nawigacji pomiędzy ekranami.
 */
@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, navController: NavHostController) {

    val viewModel: PetViewModel = viewModel()
    val userID = Firebase.auth.currentUser?.uid.toString()

    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable(route = "login") {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable(route = "signup") {
            SignupScreen(modifier, navController, authViewModel)
        }
        composable(route = "home") {
            HomeScreen(modifier, navController, authViewModel, viewModel)
        }
        composable(route = "pet") {
            PetScreen(modifier, navController, authViewModel, viewModel)
        }
        composable(route = "settings") {
            SettingsScreen(navController = navController)
        }
        composable(route = "edit?petID={petID}") { backStackEntry ->
            val petID = backStackEntry.arguments?.getString("petID") ?: ""
            viewModel.readPetData(userID)
            val petData = viewModel.PetListViewState.value.find { it.ID == petID }
            petData?.let {
                EditPetScreen(modifier, navController, authViewModel, viewModel, petID)
            }
        }
        composable(route = "appointment?petID={petID}") { backStackEntry ->
            val petID = backStackEntry.arguments?.getString("petID") ?: ""
            viewModel.readPetData(userID)
            val petData = viewModel.PetListViewState.value.find { it.ID == petID }
            petData?.let {
                AppointmentScreen(viewModel, navController, petID, userID)
            }
        }
        composable(route = "petAppHistory?petID={petID}") { backStackEntry ->
            val petID = backStackEntry.arguments?.getString("petID") ?: ""
            viewModel.readPetData(userID)
            val petData = viewModel.PetListViewState.value.find { it.ID == petID }
            petData?.let {
                PetAppHisScreen(viewModel, navController, petID, userID)
            }
        }
    }
}

/**
 * Klasa danych reprezentująca pozycje w dolnym pasku nawigacyjnym w aplikacji.
 * Każda pozycja zawiera tytuł, ikony dla stanu zaznaczonego i niezaznaczonego,
 * oraz trasę, do której prowadzi nawigacja.
 *
 * @param title Tytuł, który będzie wyświetlany w pozycji na pasku nawigacyjnym.
 * @param selectedIcon Ikona wyświetlana, gdy pozycja jest zaznaczona.
 * @param unselectedIcon Ikona wyświetlana, gdy pozycja nie jest zaznaczona.
 * @param route Trasa, do której prowadzi pozycja na pasku nawigacyjnym.
 */
data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)

/**
 * Funkcja composable, która definiuje dolny pasek nawigacyjny aplikacji.
 * Pasek nawigacyjny wyświetla ikony i tytuły głównych sekcji aplikacji
 * (Home, Pets, Settings). Pojawia się tylko wtedy, gdy bieżący ekran to nie
 * ekran logowania ani rejestracji.
 *
 * @param navController Kontroler nawigacji używany do zarządzania nawigacją.
 */
@Composable
fun BottomNavigationTheme(navController: NavController) {

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
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Wyświetl dolny pasek nawigacyjny dla ekranów innych niż logowanie i rejestracja
    if (currentRoute != "login" && currentRoute != "signup") {
        NavigationBar() {
            items.forEach { item ->
                NavigationBarItem(
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route)
                    },
                    icon = {
                        Icon(
                            imageVector = item.selectedIcon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(item.title)
                    }
                )
            }
        }
    }
}
