package com.example.vetapp

import android.widget.Switch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

/**
 * Ekran ustawień aplikacji, który umożliwia konfigurację opcji, takich jak tryb ciemny.
 *
 * @param navController Kontroler nawigacji używany do przechodzenia między ekranami.
 */
@Composable
fun SettingsScreen(

    navController: NavController
) {
    // Pobranie stanu trybu ciemnego z ViewModelu
    // (w tym miejscu można zaimplementować funkcjonalność pobierania stanu trybu ciemnego z ViewModelu)

    // Layout ekranu
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Tekst informujący o trybie ciemnym
            Text(
                text = "Tryb Ciemny",
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            // Tu można dodać Switch lub Checkbox do przełączania trybu ciemnego
            // (obecnie brak elementu do zmiany stanu trybu ciemnego)
        }
    }
}
