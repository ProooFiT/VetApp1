package Screen

import DatePickerField
import TimePickerField
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppointmentScreen(
    viewModel: PetViewModel,
    navController: NavController,
    petID: String,
    userID: String,
) {
    val petData = viewModel.PetListViewState.value.find { it.ID == petID }

    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf(" ") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            DatePickerField(onDateSelected = { selectedDate -> date = selectedDate })



            TimePickerField(onTimeSelected = { selectedTime -> time = selectedTime })


            TextField(
                value = reason,
                onValueChange = { reason = it },
                label = {
                    Text(
                        "Powód wizyty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    petData?.let {
                        val appointment = Appointment(date, time, reason)
                        viewModel.bookAppointment(it, appointment, userID)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Umów wizytę")
            }
        }
    }
}

