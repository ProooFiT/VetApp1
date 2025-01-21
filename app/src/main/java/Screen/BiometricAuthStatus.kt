package com.example.aplikacja_sportowa

/**
 * Enum class representing the status of biometric authentication availability.
 * It is used to categorize various scenarios when checking the availability or success of biometric authentication.
 *
 * @property id The unique identifier associated with each authentication status.
 */
enum class BiometricAuthStatus (val id: Int) {
    /**
     * Biometric authentication is available and ready for use.
     */
    READY(1),

    /**
     * Biometric authentication is not available on the device.
     */
    NOT_AVAILABLE(-2),

    /**
     * Biometric authentication is temporarily unavailable (e.g., hardware issues).
     */
    TEMPORARY_NOT_AVAILABLE(-3),

    /**
     * Biometric authentication is available but no biometric data (e.g., fingerprint, face) is enrolled.
     */
    AVAILABLE_BUT_NOT_ENROLLED(-4),
}
