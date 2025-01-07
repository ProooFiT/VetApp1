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

        petRef.child("0").setValue(appointment)
            .addOnSuccessListener {
                println("Wizyta została pomyślnie zapisana dla zwierzęcia ${pet.ID}.")
            }
            .addOnFailureListener { exception ->
                println("Błąd podczas zapisywania wizyty: ${exception.message}")
            }
    }
}






