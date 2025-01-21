package com.example.navhost

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.loc.composebiometricauth.BiometricAuthenticator

/**
 * ViewModel odpowiedzialny za obsługę stanu autentykacji użytkownika oraz jego akcji.
 * Współpracuje z Firebase Authentication, umożliwiając logowanie, rejestrację, wylogowanie oraz autentykację biometryczną.
 */
class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // LiveData do obserwowania zmian stanu autentykacji
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    /**
     * Sprawdza stan autentykacji użytkownika.
     * Jeśli użytkownik jest zalogowany, ustawia stan na [AuthState.Authenticated],
     * w przeciwnym razie ustawia stan na [AuthState.Unauthenticated].
     */
    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    /**
     * Loguje użytkownika przy użyciu adresu e-mail i hasła.
     *
     * @param email Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     */
    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email lub hasło nie mogą być puste")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Coś poszło nie tak")
                }
            }
    }

    /**
     * Rejestruje nowego użytkownika przy użyciu adresu e-mail i hasła.
     *
     * @param email Adres e-mail nowego użytkownika.
     * @param password Hasło nowego użytkownika.
     * @param navController Kontroler nawigacji [NavController], aby przejść do ekranu głównego po pomyślnej rejestracji.
     */
    fun signup(email: String, password: String, navController: NavController) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email lub hasło nie mogą być puste")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Coś poszło nie tak")
                }
            }
    }

    /**
     * Wylogowuje użytkownika i aktualizuje stan autentykacji na [AuthState.Unauthenticated].
     */
    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    /**
     * Autentykuje użytkownika przy użyciu autentykacji biometrycznej (odcisk palca lub rozpoznawanie twarzy).
     *
     * @param biometricAuthenticator Obiekt [BiometricAuthenticator] do wywołania procesu autentykacji biometrycznej.
     * @param fragmentActivity Kontekst aktywności do wyświetlenia monitu o autentykację biometryczną.
     * @param navController Kontroler nawigacji [NavController] do przejścia do ekranu głównego po pomyślnej autentykacji.
     */
    fun loginWithBiometric(
        biometricAuthenticator: BiometricAuthenticator,
        fragmentActivity: FragmentActivity,
        navController: NavController
    ) {
        _authState.value = AuthState.Loading

        biometricAuthenticator.promptBiometricAuth(
            title = "Logowanie biometryczne",
            subTitle = "Zaloguj się używając odcisku palca lub rozpoznawania twarzy",
            negativeButtonText = "Anuluj",
            fragmentActivity = fragmentActivity,
            onSuccess = { result ->
                // Pomyślnie zalogowano za pomocą autentykacji biometrycznej
                val user = auth.currentUser
                if (user != null) {
                    _authState.value = AuthState.Authenticated
                    navController.navigate("home")
                } else {
                    _authState.value = AuthState.Error("Brak użytkownika do autentykacji biometrycznej.")
                }
            },
            onFailed = {
                _authState.value = AuthState.Error("Autentykacja biometryczna nie powiodła się.")
            },
            onError = { errorCode, errString ->
                _authState.value = AuthState.Error("Błąd $errorCode: $errString")
            }
        )
    }
}

/**
 * Reprezentuje różne stany autentykacji użytkownika.
 */
sealed class AuthState {
    object Authenticated : AuthState() // Użytkownik jest zalogowany
    object Unauthenticated : AuthState() // Użytkownik nie jest zalogowany
    object Loading : AuthState() // Trwa proces autentykacji
    data class Error(val message: String) : AuthState() // Błąd podczas autentykacji
}
