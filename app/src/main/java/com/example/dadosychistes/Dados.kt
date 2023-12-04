package com.example.dadosychistes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import com.example.dadosychistes.databinding.ActivityDadosBinding
import com.example.dadosychistes.databinding.ActivityMainBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class Dados : AppCompatActivity() {
    private lateinit var binding : ActivityDadosBinding
    private var total = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDadosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        binding.txtResultado.visibility = View.INVISIBLE
        binding.imageButton.setOnClickListener { view ->
            binding.txtResultado.visibility = View.VISIBLE
            initGame()
        }
    }

    private fun initGame() {
        val bundle = intent.getBundleExtra("bundle")
        val NUMERO_HILOS = bundle!!.getInt("throwNumber")
        val scheduleExecutor = Executors.newScheduledThreadPool(NUMERO_HILOS)
        val milisegundos : Long = bundle.getInt("throwTime") * 1_000L
        for (i in 1..NUMERO_HILOS) {
            val hilo = Runnable { tirarDados() }
            scheduleExecutor.schedule(hilo, milisegundos * i, TimeUnit.MILLISECONDS)
        }

        scheduleExecutor.schedule({
            handler.post {
                binding.txtResultado.text = total.toString()
                if (total < 8)
                    binding.ivCharacter.setImageResource(R.drawable.orc)
                else if (total < 13)
                    binding.ivCharacter.setImageResource(R.drawable.knight)
                else
                    binding.ivCharacter.setImageResource(R.drawable.king)
            }}, milisegundos *  (NUMERO_HILOS + 1), TimeUnit.MILLISECONDS)

        scheduleExecutor.shutdown()
    }

    private fun tirarDados() {
        val numeros = arrayOf(Random.nextInt(1, 7), Random.nextInt(1, 7), Random.nextInt(1, 7))
        val imagenes = arrayOf(binding.imagviewDado1, binding.imagviewDado2, binding.imagviewDado3)

        total = numeros.sum()
        for (i in 0..2) {
            cambiarImagenes(numeros[i], imagenes[i])
        }
    }

    private fun cambiarImagenes(numero: Int, imagen: ImageView) {
        handler.post {
            when (numero) {
                1 -> imagen.setImageResource(R.drawable.dado1)
                2 -> imagen.setImageResource(R.drawable.dado2)
                3 -> imagen.setImageResource(R.drawable.dado3)
                4 -> imagen.setImageResource(R.drawable.dado4)
                5 -> imagen.setImageResource(R.drawable.dado5)
                6 -> imagen.setImageResource(R.drawable.dado6)
            }
        }
    }
}