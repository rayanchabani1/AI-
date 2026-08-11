package com.example.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceSpeechManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("VoiceSpeechManager", "Failed to initialize TTS", e)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val arLocale = Locale("ar")
            val result = tts?.setLanguage(arLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.language = Locale.getDefault()
            }
            isTtsReady = true
        } else {
            Log.e("VoiceSpeechManager", "TTS initialization failed status=$status")
        }
    }

    fun speak(text: String, onComplete: () -> Unit = {}) {
        if (!isTtsReady || tts == null) return
        stopSpeaking()

        // Clean text from markdown symbols for clean Arabic voice reading
        val cleanText = text
            .replace(Regex("[*#`_~▶️📺📱🔍👈🏠🔊🔉⚡🎁🌐⚙️🛡️🎨📶💬📄🎮🛠️🔋🧩✨]"), "")
            .replace("-", " ")
            .trim()

        if (cleanText.isBlank()) return

        val params = Bundle()
        tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, params, "META_VOICE_UTTERANCE")
    }

    fun stopSpeaking() {
        try {
            if (tts?.isSpeaking == true) {
                tts?.stop()
            }
        } catch (e: Exception) {
            Log.e("VoiceSpeechManager", "Error stopping TTS", e)
        }
    }

    fun startListening(
        onResult: (String) -> Unit,
        onError: (String) -> Unit = {},
        onRmsChanged: (Float) -> Unit = {}
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("خدمة التعرف على الصوت غير متاحة حالياً على جهازك.")
            return
        }

        stopListening()

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                    }

                    override fun onBeginningOfSpeech() {}

                    override fun onRmsChanged(rmsdB: Float) {
                        onRmsChanged(rmsdB)
                    }

                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        isListening = false
                    }

                    override fun onError(error: Int) {
                        isListening = false
                        val msg = when (error) {
                            SpeechRecognizer.ERROR_NO_MATCH -> "لم يتم التمكن من سماع الصوت بوضوح، يرجى المحاولة مجدداً."
                            SpeechRecognizer.ERROR_AUDIO -> "خطأ في التقاط الصوت من الميكروفون."
                            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "خطأ بالاتصال بالشبكة لفك الصوت."
                            else -> "حدث خطأ بالتعرف على الصوت (كود $error)"
                        }
                        onError(msg)
                    }

                    override fun onResults(results: Bundle?) {
                        isListening = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val recognizedText = matches[0]
                            onResult(recognizedText)
                        } else {
                            onError("لم يتم التعرف على الكلمات المذنورة.")
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            onResult(matches[0])
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-SA")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ar")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "تحدث الآن إلى ميتا AI...")
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            isListening = false
            onError("تعذر تشغيل الميكروفون: ${e.localizedMessage}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
            speechRecognizer = null
            isListening = false
        } catch (e: Exception) {
            Log.e("VoiceSpeechManager", "Error stopping listener", e)
        }
    }

    fun shutdown() {
        stopSpeaking()
        stopListening()
        try {
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.e("VoiceSpeechManager", "Error shutting down TTS", e)
        }
    }
}
