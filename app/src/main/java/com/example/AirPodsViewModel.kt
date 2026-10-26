package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioFxManager
import com.example.bluetooth.AirPodsBluetoothManager
import com.example.data.AirPodsSettings
import com.example.data.AppPreferences
import com.example.data.BatteryInfo
import com.example.data.ConnectionStatus
import com.example.data.DefaultPresets
import com.example.data.EqualizerPreset
import com.example.data.ScannedDevice
import com.example.data.SoundModeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    EQUALIZER,
    BATTERY,
    SOUND_CONTROL,
    SOUND_MODES,
    FIND_AIRPODS,
    SETTINGS,
    ADVANCED_SETTINGS
}

class AirPodsViewModel(application: Application) : AndroidViewModel(application) {
    val prefs = AppPreferences(application)
    val audioFx = AudioFxManager(application)
    val bluetoothManager = AirPodsBluetoothManager(application)

    // Navigation Stack
    private val _screenStack = MutableStateFlow(listOf(AppScreen.HOME))
    val currentScreen: StateFlow<AppScreen> = MutableStateFlow(AppScreen.HOME)
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)

    // Audio & Equalizer States
    private val _mediaVolumePercent = MutableStateFlow(audioFx.getMediaVolumePercentage())
    val mediaVolumePercent: StateFlow<Int> = _mediaVolumePercent.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isEqualizerEnabled = MutableStateFlow(prefs.isEqualizerEnabled)
    val isEqualizerEnabled: StateFlow<Boolean> = _isEqualizerEnabled.asStateFlow()

    private val _currentPresetId = MutableStateFlow(prefs.currentPresetId)
    val currentPresetId: StateFlow<String> = _currentPresetId.asStateFlow()

    private val _bandLevels = MutableStateFlow(
        List(10) { index -> prefs.getBandLevel(index, 0f) }
    )
    val bandLevels: StateFlow<List<Float>> = _bandLevels.asStateFlow()

    private val _bassBoost = MutableStateFlow(prefs.bassBoostLevel)
    val bassBoost: StateFlow<Int> = _bassBoost.asStateFlow()

    private val _is3dAudio = MutableStateFlow(prefs.is3dAudioEnabled)
    val is3dAudio: StateFlow<Boolean> = _is3dAudio.asStateFlow()

    private val _soundMode = MutableStateFlow(prefs.soundMode)
    val soundMode: StateFlow<SoundModeType> = _soundMode.asStateFlow()

    private val _isPlayingChirp = MutableStateFlow(false)
    val isPlayingChirp: StateFlow<Boolean> = _isPlayingChirp.asStateFlow()

    private val _settings = MutableStateFlow(prefs.getSettings())
    val settings: StateFlow<AirPodsSettings> = _settings.asStateFlow()

    // Pass-through flows from Bluetooth
    val connectionStatus: StateFlow<ConnectionStatus> = bluetoothManager.connectionStatus
    val connectedDeviceName: StateFlow<String> = bluetoothManager.connectedDeviceName
    val batteryInfo: StateFlow<BatteryInfo> = bluetoothManager.batteryInfo
    val scannedDevices: StateFlow<List<ScannedDevice>> = bluetoothManager.scannedDevices
    val isScanning: StateFlow<Boolean> = bluetoothManager.isScanning

    init {
        // Apply initial audio effect parameters
        audioFx.setEqualizerEnabled(prefs.isEqualizerEnabled)
        val initialPreset = DefaultPresets.find { it.id == prefs.currentPresetId } ?: DefaultPresets.first()
        if (prefs.isEqualizerEnabled) {
            audioFx.applyPresetBands(initialPreset.bandLevels)
        }
        audioFx.setBassBoost(prefs.bassBoostLevel)
        audioFx.set3dAudio(prefs.is3dAudioEnabled)
    }

    // Navigation
    fun navigateTo(screen: AppScreen) {
        val current = _screenStack.value
        if (current.lastOrNull() != screen) {
            _screenStack.value = current + screen
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        val current = _screenStack.value
        if (current.size > 1) {
            val updated = current.dropLast(1)
            _screenStack.value = updated
            _currentScreen.value = updated.last()
            return true
        }
        return false
    }

    fun selectBottomTab(screen: AppScreen) {
        _screenStack.value = listOf(screen)
        _currentScreen.value = screen
    }

    // Audio Controls
    fun setVolume(percent: Int) {
        _mediaVolumePercent.value = percent
        audioFx.setMediaVolumePercentage(percent)
    }

    fun adjustVolumeDelta(delta: Int) {
        audioFx.adjustVolume(delta)
        _mediaVolumePercent.value = audioFx.getMediaVolumePercentage()
    }

    fun toggleMute() {
        audioFx.toggleMute()
        _mediaVolumePercent.value = audioFx.getMediaVolumePercentage()
    }

    fun togglePlayPause() {
        val next = !_isPlaying.value
        _isPlaying.value = next
        audioFx.playPauseMedia()
    }

    fun mediaNext() {
        audioFx.nextMedia()
    }

    fun mediaPrevious() {
        audioFx.previousMedia()
    }

    // Equalizer
    fun setEqualizerEnabled(enabled: Boolean) {
        _isEqualizerEnabled.value = enabled
        prefs.isEqualizerEnabled = enabled
        audioFx.setEqualizerEnabled(enabled)
    }

    fun selectPreset(preset: EqualizerPreset) {
        _currentPresetId.value = preset.id
        prefs.currentPresetId = preset.id
        _bandLevels.value = preset.bandLevels
        preset.bandLevels.forEachIndexed { i, level ->
            prefs.setBandLevel(i, level)
        }
        audioFx.applyPresetBands(preset.bandLevels)
    }

    fun setBandLevel(bandIndex: Int, level: Float) {
        val current = _bandLevels.value.toMutableList()
        if (bandIndex in current.indices) {
            current[bandIndex] = level
            _bandLevels.value = current
            prefs.setBandLevel(bandIndex, level)
            audioFx.setCustomBandLevel(bandIndex, level)
        }
    }

    fun setBassBoost(strength: Int) {
        _bassBoost.value = strength
        prefs.bassBoostLevel = strength
        audioFx.setBassBoost(strength)
    }

    fun set3dAudio(enabled: Boolean) {
        _is3dAudio.value = enabled
        prefs.is3dAudioEnabled = enabled
        audioFx.set3dAudio(enabled)
    }

    fun setSoundMode(mode: SoundModeType) {
        _soundMode.value = mode
        prefs.soundMode = mode
    }

    // Bluetooth / Radar
    fun startScan() {
        bluetoothManager.startScan()
    }

    fun stopScan() {
        bluetoothManager.stopScan()
    }

    fun refreshBattery() {
        bluetoothManager.checkConnectedDevice()
    }

    fun toggleChirp() {
        if (_isPlayingChirp.value) {
            audioFx.stopChirpTone()
            _isPlayingChirp.value = false
        } else {
            _isPlayingChirp.value = true
            audioFx.startChirpTone(viewModelScope) {
                _isPlayingChirp.value = false
            }
        }
    }

    // Settings
    fun updateInEar(enabled: Boolean) {
        prefs.inEarDetection = enabled
        _settings.value = prefs.getSettings()
    }

    fun updateMic(enabled: Boolean) {
        prefs.micOptimization = enabled
        _settings.value = prefs.getSettings()
    }

    fun updateLowLatency(enabled: Boolean) {
        prefs.lowLatency = enabled
        _settings.value = prefs.getSettings()
    }

    fun updateFloatingShortcut(enabled: Boolean) {
        prefs.floatingShortcut = enabled
        _settings.value = prefs.getSettings()
    }

    fun updateAmbientSound(level: Float) {
        prefs.ambientSoundLevel = level
        _settings.value = prefs.getSettings()
    }

    fun resetToDefaults() {
        prefs.resetToDefaults()
        _isEqualizerEnabled.value = prefs.isEqualizerEnabled
        _currentPresetId.value = prefs.currentPresetId
        _bandLevels.value = List(10) { i -> prefs.getBandLevel(i, 0f) }
        _bassBoost.value = prefs.bassBoostLevel
        _is3dAudio.value = prefs.is3dAudioEnabled
        _soundMode.value = prefs.soundMode
        _settings.value = prefs.getSettings()

        audioFx.setEqualizerEnabled(prefs.isEqualizerEnabled)
        val defaultPreset = DefaultPresets.first()
        audioFx.applyPresetBands(defaultPreset.bandLevels)
        audioFx.setBassBoost(prefs.bassBoostLevel)
        audioFx.set3dAudio(prefs.is3dAudioEnabled)
    }

    override fun onCleared() {
        super.onCleared()
        audioFx.release()
        bluetoothManager.onDestroy()
    }
}
