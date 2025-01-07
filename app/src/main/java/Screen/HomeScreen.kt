package com.example.navhost

import Screen.Pet
import Screen.PetViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vetapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.placeholder.placeholder.PlaceholderPlugin

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
            showPet(Modifier,petData, navController, viewModel)
        }

        item {
            TextButton(onClick = { authViewModel.signout() }) {
                Text(text = "sign out")
            }
        }


    }


}
@Composable
fun showPet(modifier: Modifier = Modifier, petData: Pet, navController: NavController, viewModel: PetViewModel) {
        val userID = Firebase.auth.currentUser?.uid.toString()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = petData.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = petData.type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = petData.age,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            GlideImage(
                imageModel = {petData?.Img}, modifier = modifier.size(128.dp), component = rememberImageComponent {
                    +PlaceholderPlugin.Failure(
                        painterResource(id = R.drawable.doges)
                    )
                }
            )

            Button(onClick = { navigateToEditPet(navController, petData.ID)}) {
                Text(text = "Edit")
            }
            Button(onClick = {viewModel.deletePet(petData.ID, userID)}) {
                Text(text = "Delete pet")
            }
            Button(onClick = { navigateToAppointmentScreen(navController, petData.ID) }) {
                Text(text = "umow wizyte")
            }
        }
    }


}

fun navigateToEditPet(
    navController: NavController,
    petID: String
){
    navController.navigate(route = "edit?petID=$petID")
}

fun navigateToAppointmentScreen(
    navController: NavController,
    petID: String) {
    navController.navigate(route = "appointment?petID=$petID")
}