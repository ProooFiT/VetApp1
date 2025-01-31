package Screen

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

/**
 * Composable function that displays a button for selecting a date.
 * It opens a [DatePickerDialog] when clicked and updates the selected date.
 * The date is passed back to the parent composable via the [onDateSelected] callback.
 *
 * @param onDateSelected A lambda function that is called with the selected date in "dd/MM/yyyy" format.
 */
@Composable
fun DatePickerField(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    var dateText by remember { mutableStateOf("Wybierz datę") }

    val calendar = Calendar.getInstance()

    // DatePickerDialog to select a date
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            // Format and display the selected date
            val formattedDate = "$day/${month + 1}/$year"
            dateText = formattedDate
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Button to show the date picker
    Button(onClick = { datePickerDialog.show() }) {
        Text(dateText)
    }
}

/**
 * Composable function that displays a button for selecting a time.
 * It opens a [TimePickerDialog] when clicked and updates the selected time.
 * The time is passed back to the parent composable via the [onTimeSelected] callback.
 *
 * @param onTimeSelected A lambda function that is called with the selected time in "HH:mm" format.
 */
@Composable
fun TimePickerField(onTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    var timeText by remember { mutableStateOf("Wybierz godzinę") }

    // TimePickerDialog to select a time
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            // Format and display the selected time
            val formattedTime = String.format("%02d:%02d", hour, minute)
            timeText = formattedTime
            onTimeSelected(formattedTime)
        },
        12, 0, true
    )

    // Button to show the time picker
    Button(onClick = { timePickerDialog.show() }) {
        Text(timeText)
    }
}
