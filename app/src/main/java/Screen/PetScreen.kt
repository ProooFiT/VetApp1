package com.example.navhost

import Screen.PetViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun PetScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: PetViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    var petName by remember {
        mutableStateOf("")
    }
    var petAge by remember {
        mutableStateOf("")
    }
    var petType by remember {
        mutableStateOf("")
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(route = "login")
            else -> Unit
        }
    }


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        TextField(value = petName, onValueChange = { petName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "name") })
        TextField(value = petAge, onValueChange = { petAge = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "age") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Ustaw klawiaturę na numeryczną
            ))
        TextField(value = petType, onValueChange = { petType = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "type") })

        Button(onClick =
        {
            if (petName.isNotEmpty() && petAge.isNotEmpty() && petType.isNotEmpty()) {

                database = Firebase.database.reference
                val petImg = ""
                val petId = database.push().key!!
                val userID = Firebase.auth.currentUser?.uid.toString()
                viewModel.savePet(petName, petId, petAge, petType, userID, petImg)
            }

        }
        ) {
            Text(text = "save")
        }

    }

}
