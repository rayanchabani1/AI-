package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

object SoundEffectsManager {
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            toneGenerator = null
        }
    }

    fun playClick() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
        } catch (e: Exception) {
            // Ignore if audio stream is unavailable
        }
    }

    fun playSuccess() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 120)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun playAiChime() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_DTMF_A, 80)
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    toneGenerator?.startTone(ToneGenerator.TONE_DTMF_D, 120)
                } catch (_: Exception) {}
            }, 100)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun playRootCommandSound() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 70)
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun playError() {
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_SUP_ERROR, 200)
        } catch (e: Exception) {
            // Ignore
        }
    }
}
