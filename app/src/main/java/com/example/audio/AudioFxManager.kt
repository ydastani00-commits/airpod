package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.Virtualizer
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

class AudioFxManager(private val context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var virtualizer: Virtualizer? = null

    private var toneJob: Job? = null
    private var isPlayingTone = false

    var isSessionGlobalSupported = false
        private set

    init {
        initAudioEffects()
    }

    private fun initAudioEffects() {
        try {
            // Attempt to attach to system output session 0
            val eq = Equalizer(0, 0)
            equalizer = eq
            eq.enabled = true
            isSessionGlobalSupported = true

            val bb = BassBoost(0, 0)
            bassBoost = bb
            bb.enabled = true

            val virt = Virtualizer(0, 0)
            virtualizer = virt
            virt.enabled = true
        } catch (e: Exception) {
            Log.w("AudioFxManager", "Global session 0 restricted on this device: ${e.message}")
            isSessionGlobalSupported = false
        }
    }

    fun setEqualizerEnabled(enabled: Boolean) {
        try {
            equalizer?.enabled = enabled
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to set equalizer enabled: ${e.message}")
        }
    }

    fun applyPresetBands(levels: List<Float>) {
        val eq = equalizer ?: return
        try {
            val numBands = eq.numberOfBands.toInt()
            val minEq = eq.bandLevelRange[0]
            val maxEq = eq.bandLevelRange[1]
            val range = (maxEq - minEq).toFloat()

            for (i in 0 until numBands) {
                val normalizedVal = levels.getOrElse(i) { 0f } // -6 to +6 dB scale
                // Map from [-6, +6] to [minEq, maxEq]
                val levelMb = (minEq + ((normalizedVal + 6f) / 12f * range)).toInt()
                    .coerceIn(minEq.toInt(), maxEq.toInt())
                eq.setBandLevel(i.toShort(), levelMb.toShort())
            }
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to apply band levels: ${e.message}")
        }
    }

    fun setCustomBandLevel(bandIndex: Int, normalizedVal: Float) {
        val eq = equalizer ?: return
        try {
            if (bandIndex < eq.numberOfBands) {
                val minEq = eq.bandLevelRange[0]
                val maxEq = eq.bandLevelRange[1]
                val range = (maxEq - minEq).toFloat()
                val levelMb = (minEq + ((normalizedVal + 6f) / 12f * range)).toInt()
                    .coerceIn(minEq.toInt(), maxEq.toInt())
                eq.setBandLevel(bandIndex.toShort(), levelMb.toShort())
            }
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to set band level $bandIndex: ${e.message}")
        }
    }

    fun setBassBoost(strength: Int) {
        try {
            bassBoost?.let {
                if (it.strengthSupported) {
                    it.setStrength(strength.coerceIn(0, 1000).toShort())
                    it.enabled = strength > 0
                }
            }
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to set bass boost: ${e.message}")
        }
    }

    fun set3dAudio(enabled: Boolean) {
        try {
            virtualizer?.let {
                if (it.strengthSupported) {
                    it.setStrength(if (enabled) 1000 else 0)
                    it.enabled = enabled
                }
            }
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to set 3d audio: ${e.message}")
        }
    }

    // Volume Management
    fun getMediaVolume(): Int {
        return try {
            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        } catch (e: Exception) {
            7
        }
    }

    fun getMaxMediaVolume(): Int {
        return try {
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        } catch (e: Exception) {
            15
        }
    }

    fun getMediaVolumePercentage(): Int {
        val current = getMediaVolume()
        val max = getMaxMediaVolume()
        return if (max > 0) ((current.toFloat() / max) * 100).toInt() else 50
    }

    fun setMediaVolumePercentage(percent: Int) {
        try {
            val max = getMaxMediaVolume()
            val target = ((percent.coerceIn(0, 100).toFloat() / 100f) * max).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, target, 0)
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to set volume: ${e.message}")
        }
    }

    fun adjustVolume(delta: Int) {
        val current = getMediaVolume()
        val max = getMaxMediaVolume()
        val target = (current + delta).coerceIn(0, max)
        try {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, target, 0)
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Failed to adjust volume: ${e.message}")
        }
    }

    fun toggleMute(): Boolean {
        return try {
            val current = getMediaVolume()
            if (current > 0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
                true
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, getMaxMediaVolume() / 2, 0)
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // Media Controls via KeyEvent
    fun playPauseMedia() {
        dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
    }

    fun nextMedia() {
        dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT)
    }

    fun previousMedia() {
        dispatchMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
    }

    private fun dispatchMediaKey(keyCode: Int) {
        try {
            val eventDown = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            audioManager.dispatchMediaKeyEvent(eventDown)
            val eventUp = KeyEvent(KeyEvent.ACTION_UP, keyCode)
            audioManager.dispatchMediaKeyEvent(eventUp)
        } catch (e: Exception) {
            Log.w("AudioFxManager", "Could not dispatch media key: ${e.message}")
        }
    }

    // High pitched finder chirp
    fun startChirpTone(scope: CoroutineScope, onStop: () -> Unit) {
        if (isPlayingTone) return
        isPlayingTone = true

        toneJob = scope.launch(Dispatchers.Default) {
            val sampleRate = 44100
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            )

            val audioTrack = try {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .build()
            } catch (e: Exception) {
                null
            }

            audioTrack?.play()

            try {
                val freqs = listOf(2800.0, 3500.0, 4200.0, 3200.0)
                var freqIdx = 0
                val samples = ShortArray(bufferSize)

                while (isActive && isPlayingTone) {
                    val currentFreq = freqs[freqIdx % freqs.size]
                    freqIdx++

                    for (i in samples.indices) {
                        val angle = 2.0 * Math.PI * i / (sampleRate / currentFreq)
                        samples[i] = (sin(angle) * Short.MAX_VALUE * 0.7).toInt().toShort()
                    }

                    audioTrack?.write(samples, 0, samples.size)
                    delay(300)
                }
            } catch (e: Exception) {
                Log.e("AudioFxManager", "Tone loop interrupted: ${e.message}")
            } finally {
                try {
                    audioTrack?.stop()
                    audioTrack?.release()
                } catch (ignored: Exception) {}
                isPlayingTone = false
                launch(Dispatchers.Main) { onStop() }
            }
        }
    }

    fun stopChirpTone() {
        isPlayingTone = false
        toneJob?.cancel()
        toneJob = null
    }

    fun isChirpActive(): Boolean = isPlayingTone

    fun release() {
        stopChirpTone()
        try {
            equalizer?.release()
            bassBoost?.release()
            virtualizer?.release()
        } catch (e: Exception) {
            Log.e("AudioFxManager", "Error releasing effects: ${e.message}")
        }
    }
}
