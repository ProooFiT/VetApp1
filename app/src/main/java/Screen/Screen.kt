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


class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String, navController: NavController) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun loginWithBiometric(
        biometricAuthenticator: BiometricAuthenticator,
        fragmentActivity: FragmentActivity,
        navController: NavController
    ) {
        _authState.value = AuthState.Loading


        biometricAuthenticator.promptBiometricAuth(
            title = "Biometric Login",
            subTitle = "Log in using your fingerprint or face",
            negativeButtonText = "Cancel",
            fragmentActivity = fragmentActivity,
            onSuccess = { result ->
                // Zalogowano biometrycznie
                val user = auth.currentUser
                if (user != null) {
                    _authState.value = AuthState.Authenticated
                    navController.navigate("home")
                } else {
                    _authState.value = AuthState.Error("No user found for biometric authentication.")
                }
            },
            onFailed = {
                _authState.value = AuthState.Error("Biometric authentication failed.")
            },
            onError = { errorCode, errString ->
                _authState.value = AuthState.Error("Error $errorCode: $errString")
            }
        )
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}





