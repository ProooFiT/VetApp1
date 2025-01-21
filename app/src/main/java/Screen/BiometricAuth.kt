package com.loc.composebiometricauth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.example.aplikacja_sportowa.BiometricAuthStatus

/**
 * A class responsible for managing biometric authentication (e.g., fingerprint, face recognition) on the device.
 * It provides methods to check biometric authentication availability and prompt users to authenticate using biometric methods.
 *
 * @param appContext The application context used for interacting with the biometric manager.
 */
class BiometricAuthenticator(
    private val appContext: Context
) {

    /**
     * Information about the biometric authentication prompt.
     */
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    /**
     * The [BiometricManager] used to interact with the system's biometric features.
     */
    private val biometricManager = BiometricManager.from(appContext.applicationContext)

    /**
     * The [BiometricPrompt] used to display the authentication dialog.
     */
    private lateinit var biometricPrompt: BiometricPrompt

    /**
     * Checks if biometric authentication is available on the device.
     *
     * @return A [BiometricAuthStatus] indicating the status of biometric authentication availability.
     */
    fun isBiometricAuthAvailable(): BiometricAuthStatus {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthStatus.NOT_AVAILABLE
        }
    }

    /**
     * Prompts the user for biometric authentication (e.g., fingerprint or face recognition).
     *
     * @param title The title to display on the authentication dialog.
     * @param subTitle The subtitle to display on the authentication dialog.
     * @param negativeButtonText The text for the negative button (cancel button).
     * @param fragmentActivity The [FragmentActivity] that will host the biometric prompt dialog.
     * @param onSuccess A callback invoked when authentication succeeds.
     * @param onFailed A callback invoked when authentication fails.
     * @param onError A callback invoked when an error occurs or the biometric prompt cannot be shown.
     */
    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        when(isBiometricAuthAvailable()) {
            BiometricAuthStatus.NOT_AVAILABLE -> {
                onError(BiometricAuthStatus.NOT_AVAILABLE.id, "Not available for this device")
                return
            }
            BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE -> {
                onError(BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE.id, "Not available at this moment")
                return
            }
            BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED.id, "You should add a fingerprint or face id first")
                return
            }
            else -> Unit
        }

        // Initialize the biometric prompt
        biometricPrompt =
            BiometricPrompt(
                fragmentActivity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        onError(errorCode, errString)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )

        // Build the prompt info
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()

        // Authenticate
        biometricPrompt.authenticate(promptInfo)
    }

}
