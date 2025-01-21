package Screen

import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

/**
 * Klasa danych reprezentująca zwierzę.
 *
 * @param name Nazwa zwierzęcia.
 * @param type Typ lub gatunek zwierzęcia (np. Pies, Kot).
 * @param age Wiek zwierzęcia, reprezentowany jako String.
 * @param ID Unikalny identyfikator zwierzęcia, używany do identyfikacji w bazie danych lub wewnętrznej.
 * @param Img URL lub ścieżka do obrazu przedstawiającego zwierzę.
 */
data class Pet (
    var name: String,
    var type: String,
    var age: String,
    var ID: String,
    var Img: String
)

/**
 * Klasa danych reprezentująca wizytę związana ze zwierzęciem.
 *
 * @param date Data wizyty (w formacie String).
 * @param time Godzina wizyty (w formacie String).
 * @param reason Powód wizyty (np. kontrola, szczepienie).
 */
data class Appointment(
    val date: String = "",
    val time: String = "",
    val reason: String = ""
)
