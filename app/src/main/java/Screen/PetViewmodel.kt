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
import java.io.ByteArrayOutputStream

class PetViewModel : ViewModel() {
    var PetListViewState = mutableStateOf(emptyList<Pet>())

    val petCollectionRef = FirebaseDatabase.getInstance().getReference("users")
    fun savePet(
        petName: String,
        petID: String,
        petAge: String,
        petType: String,
        userID: String,
        petImg: String,
    ) {

        petCollectionRef.child(userID).child("pets").child(petID).child("petName").setValue(petName)
        petCollectionRef.child(userID).child("pets").child(petID).child("petAge").setValue(petAge)
        petCollectionRef.child(userID).child("pets").child(petID).child("petType").setValue(petType)
        petCollectionRef.child(userID).child("pets").child(petID).child("petImg").setValue(petImg)

    }

    fun readPetData(userID: String){
        val petList = mutableListOf<Pet>()
        petCollectionRef.child(userID).child("pets").get().addOnSuccessListener{
            it.children.forEach{ child ->
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


    fun deletePet(
        petID: String,
        userID: String,
    ) {
        petCollectionRef.child(userID).child("pets").child(petID).removeValue().addOnSuccessListener { readPetData(userID) }


    }

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
    fun getAppointmentHistory(petID: String, userID: String, onComplete: (List<Appointment>) -> Unit) {
        if (petID.isEmpty() || userID.isEmpty()) {
            println("Error: petID or userID is empty.")
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
                onComplete(appointments) // Przekazuje listę wizyt do wywołującego kodu
            }
            .addOnFailureListener { exception ->
                println("Błąd podczas pobierania historii wizyt: ${exception.message}")
                onComplete(emptyList()) // Zwraca pustą listę w przypadku błędu
            }
    }


}






