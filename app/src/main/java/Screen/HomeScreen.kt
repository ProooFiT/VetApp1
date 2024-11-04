package com.example.navhost

import Screen.Pet
import Screen.PetViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    viewModel: PetViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val userID = Firebase.auth.currentUser?.uid.toString()
    viewModel.readPetData(userID)

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(route = "login")
            else -> Unit
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(viewModel.PetListViewState.value) { petData ->
            showPet(Modifier,petData)
        }

        item {
            TextButton(onClick = { authViewModel.signout() }) {
                Text(text = "sign out")
            }
        }


    }


}
@Composable
fun showPet(modifier: Modifier, petData: Pet,){
    Row(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
    ){
        Text(text = petData.name)
        Text(text = petData.type)
        Text(text = petData.age)
    }
}