package com.example.resqr.clienthome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale
import android.media.AudioManager

class AlertEmergencyListener(val context: Context) {

    private fun muteBeepSounds(mute: Boolean) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            if (mute) AudioManager.ADJUST_MUTE else AudioManager.ADJUST_UNMUTE,
            0
        )
    }

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

    }

    fun startListening(clientViewmodel: ClientViewmodel) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(rmsdB: Float) {

            }

            override fun onBufferReceived(buffer: ByteArray?) {

            }

            override fun onEndOfSpeech() {
                muteBeepSounds(false)
            }

            override fun onError(error: Int) {
                muteBeepSounds(true)
                speechRecognizer.startListening(speechIntent)
            }

            override fun onResults(results: Bundle?) {
                muteBeepSounds(true)
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.d("matches", matches.toString())
                matches?.let {
                    for (match in it) {
                        if (match.lowercase(Locale.getDefault()).contains("help")) {
                            clientViewmodel.startCountdown()
                        }
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {

            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }
        })
        speechRecognizer.startListening(speechIntent)
    }
}