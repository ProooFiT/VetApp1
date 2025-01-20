import Screen.Appointment
import Screen.Pet
import Screen.PetViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun PetAppHisScreen(
    viewModel: PetViewModel,
    navController: NavController,
    petID: String,
    userID: String
) {
    var appointmentHistory by remember { mutableStateOf<List<Appointment>>(emptyList()) }

    // Pobieranie historii wizyt
    LaunchedEffect(petID) {
        viewModel.getAppointmentHistory(petID, userID) { appointments ->
            appointmentHistory = appointments
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Wyświetlanie historii wizyt
        Text(
            text = "Historia wizyt:",
            style = MaterialTheme.typography.bodyLarge
        )

        // Sprawdzanie, czy historia wizyt jest pusta
        if (appointmentHistory.isEmpty()) {
            Text("Brak wizyt.")
        } else {
            // Wyświetlanie historii wizyt w formie listy
            LazyColumn {
                items(appointmentHistory) { appointment ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Data: ${appointment.date}")
                        Text("Czas: ${appointment.time}")
                        Text("Powód: ${appointment.reason}")
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Przycisk powrotu
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Powrót")
        }
    }
}


