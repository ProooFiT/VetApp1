package Screen
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase


data class Pet (
    var name: String,
    var type: String,
    var age: String,
    var ID: String,
    var Img: String,
)

data class Appointment(
    val date: String,
    val time: String,
    val reason: String
)


