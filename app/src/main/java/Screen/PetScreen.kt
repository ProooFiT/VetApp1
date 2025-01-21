package com.example.navhost

import Screen.PetViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

private lateinit var database: DatabaseReference

/**
 * Composable screen for adding a new pet to the database.
 *
 * This screen allows the user to input details about their pet, including the name, age, and type.
 * After filling out the form, the user can save the pet's information to the Firebase database.
 *
 * @param modifier Modifier to apply to the layout.
 * @param navController The NavController for handling navigation between screens.
 * @param authViewModel The ViewModel that handles user authentication state.
 * @param viewModel The ViewModel that handles pet-related data operations.
 */
@Composable
fun PetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: PetViewModel
) {
    // Observing authentication state
    val authState = authViewModel.authState.observeAsState()

    // States for pet details input
    var petName by remember { mutableStateOf("") }
    var petAge by remember { mutableStateOf("") }
    var petType by remember { mutableStateOf("") }

    // Redirecting to login screen if user is not authenticated
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(route = "login")
            else -> Unit
        }
    }

    // Main Column to display the pet creation form
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pet name input field
        TextField(
            value = petName,
            onValueChange = { petName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "name") }
        )

        // Pet age input field with numeric keyboard
        TextField(
            value = petAge,
            onValueChange = { petAge = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Numeric keyboard for age input
            )
        )

        // Pet type input field
        TextField(
            value = petType,
            onValueChange = { petType = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "type") }
        )

        // Button to save the pet details
        Button(onClick = {
            // Saving pet data to Firebase if all fields are filled
            if (petName.isNotEmpty() && petAge.isNotEmpty() && petType.isNotEmpty()) {
                database = Firebase.database.reference
                val petImg = "" // Placeholder for pet image URL
                val petId = database.push().key!! // Generate a new ID for the pet
                val userID = Firebase.auth.currentUser?.uid.toString()
                viewModel.savePet(petName, petId, petAge, petType, userID, petImg)
            }
        }) {
            Text(text = "save")
        }
    }
}
