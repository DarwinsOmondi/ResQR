package com.example.resqr.clienthome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale

class AlertEmergencyListener(val context: Context) {

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

    }

    fun startListening(clientViewmodel: ClientViewmodel) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onBeginningOfSpeech() {
                TODO("Not yet implemented")
            }

            override fun onRmsChanged(rmsdB: Float) {
                TODO("Not yet implemented")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                TODO("Not yet implemented")
            }

            override fun onEndOfSpeech() {
                TODO("Not yet implemented")
            }

            override fun onError(error: Int) {
                speechRecognizer.startListening(speechIntent)
            }

            override fun onResults(results: Bundle?) {
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
                TODO("Not yet implemented")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                TODO("Not yet implemented")
            }
        })
    }
}