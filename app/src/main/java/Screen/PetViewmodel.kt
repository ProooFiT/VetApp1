package Screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {
    var PetListViewState = mutableStateOf(emptyList<Pet>())

    val petCollectionRef = FirebaseDatabase.getInstance().getReference("pets")
    fun savePet(
        petName: String,
        petID: String,
        petAge: String,
        petType: String,
    ) {

        petCollectionRef.child(petID).child("petName").setValue(petName)
        petCollectionRef.child(petID).child("petAge").setValue(petAge)
        petCollectionRef.child(petID).child("petType").setValue(petType)
    }

    fun readPetData(){
        val petList = mutableListOf<Pet>()
        petCollectionRef.get().addOnSuccessListener{
            it.children.forEach{ child ->
                val petID = child.key.toString()
                val petName = it.child(petID).child("petName").getValue(String::class.java).toString()
                val petAge = it.child(petID).child("petAge").getValue(String::class.java).toString()
                val petType = it.child(petID).child("petType").getValue(String::class.java).toString()

                val petData = Pet(petName, petType, petAge, petID)
                petList.add(petData)
            }
            PetListViewState.value = petList
        }

    }
}