package com.example.data

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("airpods_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_EQ_ENABLED = "eq_enabled"
        private const val KEY_EQ_PRESET = "eq_preset"
        private const val KEY_BASS_BOOST = "bass_boost"
        private const val KEY_3D_AUDIO = "3d_audio"
        private const val KEY_SOUND_MODE = "sound_mode"
        private const val KEY_IN_EAR = "in_ear_detection"
        private const val KEY_MIC_OPT = "mic_optimization"
        private const val KEY_LOW_LATENCY = "low_latency"
        private const val KEY_FLOATING_SHORTCUT = "floating_shortcut"
        private const val KEY_AUTO_CONNECT = "auto_connect"
        private const val KEY_DEVICE_NAME = "device_name"
        private const val KEY_LAST_DEVICE_ADDR = "last_device_address"
        private const val KEY_LAST_SEEN_TIME = "last_seen_time"
        private const val KEY_LAST_RSSI = "last_rssi"
        private const val KEY_AMBIENT_SOUND = "ambient_sound_level"
        private const val KEY_EQ_BANDS_PREFIX = "eq_band_"
    }

    var isEqualizerEnabled: Boolean
        get() = prefs.getBoolean(KEY_EQ_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_EQ_ENABLED, value).apply()

    var currentPresetId: String
        get() = prefs.getString(KEY_EQ_PRESET, "default") ?: "default"
        set(value) = prefs.edit().putString(KEY_EQ_PRESET, value).apply()

    var bassBoostLevel: Int
        get() = prefs.getInt(KEY_BASS_BOOST, 350)
        set(value) = prefs.edit().putInt(KEY_BASS_BOOST, value).apply()

    var is3dAudioEnabled: Boolean
        get() = prefs.getBoolean(KEY_3D_AUDIO, false)
        set(value) = prefs.edit().putBoolean(KEY_3D_AUDIO, value).apply()

    var soundMode: SoundModeType
        get() {
            val name = prefs.getString(KEY_SOUND_MODE, SoundModeType.NORMAL.name)
            return try {
                SoundModeType.valueOf(name ?: SoundModeType.NORMAL.name)
            } catch (e: Exception) {
                SoundModeType.NORMAL
            }
        }
        set(value) = prefs.edit().putString(KEY_SOUND_MODE, value.name).apply()

    var inEarDetection: Boolean
        get() = prefs.getBoolean(KEY_IN_EAR, true)
        set(value) = prefs.edit().putBoolean(KEY_IN_EAR, value).apply()

    var micOptimization: Boolean
        get() = prefs.getBoolean(KEY_MIC_OPT, true)
        set(value) = prefs.edit().putBoolean(KEY_MIC_OPT, value).apply()

    var lowLatency: Boolean
        get() = prefs.getBoolean(KEY_LOW_LATENCY, false)
        set(value) = prefs.edit().putBoolean(KEY_LOW_LATENCY, value).apply()

    var floatingShortcut: Boolean
        get() = prefs.getBoolean(KEY_FLOATING_SHORTCUT, true)
        set(value) = prefs.edit().putBoolean(KEY_FLOATING_SHORTCUT, value).apply()

    var autoConnect: Boolean
        get() = prefs.getBoolean(KEY_AUTO_CONNECT, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_CONNECT, value).apply()

    var deviceName: String
        get() = prefs.getString(KEY_DEVICE_NAME, "AirPods Pro") ?: "AirPods Pro"
        set(value) = prefs.edit().putString(KEY_DEVICE_NAME, value).apply()

    var lastDeviceAddress: String?
        get() = prefs.getString(KEY_LAST_DEVICE_ADDR, null)
        set(value) = prefs.edit().putString(KEY_LAST_DEVICE_ADDR, value).apply()

    var lastSeenTimestamp: Long
        get() = prefs.getLong(KEY_LAST_SEEN_TIME, System.currentTimeMillis() - 5 * 60 * 1000)
        set(value) = prefs.edit().putLong(KEY_LAST_SEEN_TIME, value).apply()

    var lastRssi: Int
        get() = prefs.getInt(KEY_LAST_RSSI, -62)
        set(value) = prefs.edit().putInt(KEY_LAST_RSSI, value).apply()

    var ambientSoundLevel: Float
        get() = prefs.getFloat(KEY_AMBIENT_SOUND, 0.5f)
        set(value) = prefs.edit().putFloat(KEY_AMBIENT_SOUND, value).apply()

    fun getBandLevel(bandIndex: Int, defaultLevel: Float = 0f): Float {
        return prefs.getFloat(KEY_EQ_BANDS_PREFIX + bandIndex, defaultLevel)
    }

    fun setBandLevel(bandIndex: Int, level: Float) {
        prefs.edit().putFloat(KEY_EQ_BANDS_PREFIX + bandIndex, level).apply()
    }

    fun getSettings(): AirPodsSettings {
        return AirPodsSettings(
            inEarDetection = inEarDetection,
            micOptimization = micOptimization,
            lowLatency = lowLatency,
            floatingShortcut = floatingShortcut,
            ambientSoundLevel = ambientSoundLevel,
            autoConnect = autoConnect,
            deviceName = deviceName
        )
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
    }
}
