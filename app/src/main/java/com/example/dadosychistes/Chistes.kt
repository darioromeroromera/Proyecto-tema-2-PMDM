package com.example.dadosychistes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dadosychistes.databinding.ActivityChistesBinding
import java.util.Locale
import kotlin.random.Random
import kotlin.text.StringBuilder

class Chistes : AppCompatActivity() {
    private lateinit var binding : ActivityChistesBinding
    private lateinit var textToSpeech : TextToSpeech
    private lateinit var listaChistes : MutableList<String>
    private var lastChiste : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChistesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setList()
        setTextToSpeech()
        initEvent()
    }

    private fun setList() {
        val bundle = intent.getBundleExtra("bundle")
        if (bundle!!.getBoolean("useLongList"))
            listaChistes = ObjChistes.longList.toMutableList()
        else
            listaChistes = ObjChistes.shortList.toMutableList()
    }

    private fun setTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { code ->
            if (code == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale("spa", "ESP"))
            }
        })

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                // Si pongo este hilo me sale error
                // java.lang.NullPointerException: Can't toast on a thread that has not called Looper.prepare()
                runOnUiThread {
                    viewsAppear()
                    binding.btnGustado.setOnClickListener { view ->
                        viewsDissapear()
                        Toast.makeText(applicationContext, "Gracias por la aportación", Toast.LENGTH_SHORT).show()
                    }

                    binding.btnNoGustado.setOnClickListener { view ->
                        viewsDissapear()
                        listaChistes.removeAt(lastChiste)
                        Toast.makeText(applicationContext, "Chiste eliminado de la lista", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onError(utteranceId: String?) = Unit

            override fun onStart(utteranceId: String?) = Unit
        })
    }

    private fun viewsDissapear() {
        binding.btnGustado.visibility = View.GONE
        binding.btnNoGustado.visibility = View.GONE
        binding.tvGustar.visibility = View.GONE
    }

    private fun viewsAppear() {
        binding.tvGustar.visibility = View.VISIBLE
        binding.btnGustado.visibility = View.VISIBLE
        binding.btnNoGustado.visibility = View.VISIBLE
    }

    private fun initEvent() {
        binding.btnContarChiste.setOnClickListener { view ->
            if (listaChistes.size == 0)
                Toast.makeText(this, "No quedan chistes en la lista", Toast.LENGTH_SHORT).show()
            else {
                var chistePos = Random.nextInt(0,listaChistes.size)
                lastChiste = chistePos
                contarChiste(listaChistes.get(chistePos))
            }
        }
    }

    private fun contarChiste(chiste : String) = textToSpeech.speak(chiste, TextToSpeech.QUEUE_FLUSH, null, "utteranceID")
}