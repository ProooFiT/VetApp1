package Screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel odpowiedzialny za zarządzanie danymi zwierząt oraz wizytami.
 * Ten ViewModel współpracuje z Firebase Realtime Database w celu zapisywania, odczytywania i usuwania danych zwierząt,
 * a także zarządzania danymi dotyczącymi wizyt dla zwierząt.
 */
class PetViewModel : ViewModel() {

    // Stan przechowujący listę zwierząt
    var PetListViewState = mutableStateOf(emptyList<Pet>())

    // Referencja do Firebase Realtime Database dla danych zwierząt
    val petCollectionRef = FirebaseDatabase.getInstance().getReference("users")

    /**
     * Zapisuje dane zwierzęcia do Firebase Realtime Database.
     *
     * @param petName Nazwa zwierzęcia.
     * @param petID Unikalny identyfikator zwierzęcia.
     * @param petAge Wiek zwierzęcia.
     * @param petType Typ zwierzęcia (np. pies, kot).
     * @param userID ID użytkownika powiązanego z danym zwierzęciem.
     * @param petImg URL lub ścieżka do obrazu zwierzęcia (opcjonalnie).
     */
    fun savePet(
        petName: String,
        petID: String,
        petAge: String,
        petType: String,
        userID: String,
        petImg: String
    ) {
        petCollectionRef.child(userID).child("pets").child(petID).child("petName").setValue(petName)
        petCollectionRef.child(userID).child("pets").child(petID).child("petAge").setValue(petAge)
        petCollectionRef.child(userID).child("pets").child(petID).child("petType").setValue(petType)
        petCollectionRef.child(userID).child("pets").child(petID).child("petImg").setValue(petImg)
    }

    /**
     * Odczytuje dane zwierzęcia z Firebase Realtime Database.
     *
     * @param userID ID użytkownika, dla którego pobieramy dane zwierząt.
     */
    fun readPetData(userID: String) {
        val petList = mutableListOf<Pet>()
        petCollectionRef.child(userID).child("pets").get().addOnSuccessListener {
            it.children.forEach { child ->
                val petID = child.key.toString()
                val petName = it.child(petID).child("petName").getValue(String::class.java).toString()
                val petAge = it.child(petID).child("petAge").getValue(String::class.java).toString()
                val petType = it.child(petID).child("petType").getValue(String::class.java).toString()
                val petImg = it.child(petID).child("petImg").getValue(String::class.java).toString()

                val petData = Pet(petName, petType, petAge, petID, petImg)
                petList.add(petData)
            }
            PetListViewState.value = petList
        }
    }

    /**
     * Usuwa zwierzę z Firebase Realtime Database.
     *
     * @param petID Unikalny identyfikator zwierzęcia do usunięcia.
     * @param userID ID użytkownika powiązanego ze zwierzęciem.
     */
    fun deletePet(
        petID: String,
        userID: String
    ) {
        petCollectionRef.child(userID).child("pets").child(petID).removeValue().addOnSuccessListener {
            readPetData(userID)
        }
    }

    /**
     * Rezerwuje wizytę dla zwierzęcia.
     *
     * @param pet Obiekt zwierzęcia, dla którego rezerwujemy wizytę.
     * @param appointment Szczegóły wizyty (data, godzina, powód).
     * @param userID ID użytkownika powiązanego ze zwierzęciem.
     */
    fun bookAppointment(pet: Pet, appointment: Appointment, userID: String) {
        val petRef = petCollectionRef.child(userID).child("pets").child(pet.ID).child("appointments")
        val newAppointmentRef = petRef.push()

        newAppointmentRef.setValue(appointment)
            .addOnSuccessListener {
                println("Wizyta została pomyślnie zapisana dla zwierzęcia ${pet.ID}.")
            }
            .addOnFailureListener { exception ->
                println("Błąd podczas zapisywania wizyty: ${exception.message}")
            }
    }

    /**
     * Pobiera historię wizyt dla konkretnego zwierzęcia.
     *
     * @param petID ID zwierzęcia, dla którego pobieramy historię wizyt.
     * @param userID ID użytkownika powiązanego ze zwierzęciem.
     * @param onComplete Funkcja zwrotna, która zwraca listę wizyt.
     */
    fun getAppointmentHistory(petID: String, userID: String, onComplete: (List<Appointment>) -> Unit) {
        if (petID.isEmpty() || userID.isEmpty()) {
            println("Błąd: petID lub userID jest puste.")
            onComplete(emptyList())
            return
        }

        val petRef = petCollectionRef.child(userID).child("pets").child(petID).child("appointments")

        petRef.get()
            .addOnSuccessListener { snapshot ->
                val appointments = mutableListOf<Appointment>()
                for (childSnapshot in snapshot.children) {
                    val appointment = childSnapshot.getValue(Appointment::class.java)
                    if (appointment != null) {
                        appointments.add(appointment)
                    }
                }
                onComplete(appointments) // Zwróć listę wizyt do wywołującego
            }
            .addOnFailureListener { exception ->
                println("Błąd podczas pobierania historii wizyt: ${exception.message}")
                onComplete(emptyList()) // Zwróć pustą listę w przypadku błędu
            }
    }
}
