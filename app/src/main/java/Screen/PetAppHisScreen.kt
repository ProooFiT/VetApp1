package Screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Komponent wyświetlający historię wizyt zwierzęcia.
 *
 * Ten ekran pobiera i wyświetla listę wizyt dla konkretnego zwierzęcia.
 * Zawiera datę, godzinę oraz powód każdej wizyty.
 *
 * @param viewModel ViewModel odpowiedzialny za pobieranie danych o wizytach.
 * @param navController NavController używany do nawigacji między ekranami.
 * @param petID ID zwierzęcia, którego historia wizyt jest wyświetlana.
 * @param userID ID użytkownika, który żąda historii wizyt.
 */
@Composable
fun PetAppHisScreen(
    viewModel: PetViewModel,
    navController: NavController,
    petID: String,
    userID: String
) {
    // Stan do przechowywania listy wizyt
    var appointmentHistory by remember { mutableStateOf<List<Appointment>>(emptyList()) }

    // Pobieranie historii wizyt przy użyciu viewModel
    LaunchedEffect(petID) {
        viewModel.getAppointmentHistory(petID, userID) { appointments ->
            appointmentHistory = appointments
        }
    }

    // Główna kolumna wyświetlająca zawartość ekranu
    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(16.dp))

        // Wyświetlanie tytułu
        Text(
            text = "Historia wizyt:",
            style = MaterialTheme.typography.bodyLarge
        )

        // Wyświetlanie komunikatu, jeśli nie znaleziono wizyt
        if (appointmentHistory.isEmpty()) {
            Text("Brak wizyt.")
        } else {
            // Wyświetlanie historii wizyt w LazyColumn
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

        // Przycisk do powrotu do poprzedniego ekranu
        Button(onClick = { navController.popBackStack() }) {
            Text(text = "Powrót")
        }
    }
}
