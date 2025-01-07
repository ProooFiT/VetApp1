import Screen.PetViewModel
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*



@Composable
fun DatePickerField(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    var dateText by remember { mutableStateOf("Wybierz datę") }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val formattedDate = "$day/${month + 1}/$year"
            dateText = formattedDate
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Button(onClick = { datePickerDialog.show() }) {
        Text(dateText)
    }
}



@Composable
fun TimePickerField(onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    var timeText by remember { mutableStateOf("Wybierz godzinę") }

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            val formattedTime = String.format("%02d:%02d", hour, minute)
            timeText = formattedTime
            onTimeSelected(formattedTime)
        },
        12, 0, true
    )

    Button(onClick = { timePickerDialog.show() }) {
        Text(timeText)
    }
}


