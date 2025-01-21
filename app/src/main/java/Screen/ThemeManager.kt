package com.example.screen

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

/**
 * ThemeManager odpowiada za dostosowanie motywu aplikacji na podstawie poziomu światła w otoczeniu.
 * Nasłuchuje zdarzeń czujnika światła i przełącza motyw między jasnym a ciemnym w zależności od poziomu światła.
 * Motyw zmienia się na ciemny, jeśli poziom światła jest poniżej określonego progu,
 * a na jasny, gdy poziom światła jest powyżej progu.
 *
 * @param context Kontekst używany do uzyskania dostępu do usług systemowych, takich jak menedżer czujników.
 */
class ThemeManager(private val context: Context) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val sensorEventListener = object : SensorEventListener {

        /**
         * Zawiadamia o zmianie danych z czujnika światła.
         * Motyw zmienia się na podstawie poziomu światła. Próg zmiany motywu ustawiony jest na 1000 lux.
         *
         * @param event Zdarzenie czujnika zawierające dane o poziomie światła.
         */
        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null && event.sensor.type == Sensor.TYPE_LIGHT) {
                val lightLevel = event.values[0]
                Log.d("LightSensor", "Poziom światła: $lightLevel")
                changeThemeBasedOnLight(lightLevel)
            }
        }

        /**
         * Zawiadamia o zmianie dokładności czujnika.
         * Ta metoda nie jest używana w logice zmiany motywu, ale jest wymagana przez interfejs SensorEventListener.
         *
         * @param sensor Czujnik, którego dokładność została zmieniona.
         * @param accuracy Nowy poziom dokładności.
         */
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    /**
     * Rozpoczyna nasłuchiwanie danych z czujnika światła.
     * Aplikacja dostosuje motyw na podstawie poziomu światła z czujnika.
     */
    fun startListening() {
        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_UI)
    }

    /**
     * Zatrzymuje nasłuchiwanie danych z czujnika światła. Po wywołaniu tej metody motyw nie będzie się już zmieniał.
     */
    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }

    /**
     * Zmienia motyw aplikacji na podstawie bieżącego poziomu światła.
     * Jeśli poziom światła jest poniżej 1000 lux, włączany jest tryb ciemny. W przeciwnym razie włączany jest tryb jasny.
     * Próg został wybrany na podstawie typowych warunków oświetleniowych w pomieszczeniach, gdzie preferowany jest tryb ciemny przy niskim oświetleniu.
     *
     * @param lightLevel Bieżący poziom światła z czujnika, mierzony w lux.
     */
    private fun changeThemeBasedOnLight(lightLevel: Float) {
        if (lightLevel < 1000) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)  // Włącz tryb ciemny
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)   // Włącz tryb jasny
        }
    }
}
