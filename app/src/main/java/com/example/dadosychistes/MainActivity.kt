package com.example.dadosychistes

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.AlarmClock
import android.util.Log
import android.view.View
import com.example.dadosychistes.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        val bundle = intent.getBundleExtra("bundle")
        binding.tvSaludo.setText("Hola ${bundle?.getString("name")}")
        binding.tvSaludo.text
        binding.btnGotoCall.setOnClickListener { view ->
            val intent = Intent(this, Second::class.java)

            startActivity(intent)
        }

        binding.btnAlarm.setOnClickListener { view ->
            // NO PONER UN DELAY MAYOR A 60
            val DELAY = 2
            var hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            var minutos = Calendar.getInstance().get(Calendar.MINUTE) + DELAY

            if (minutos >= 60) {
                minutos -= 60
                if (hora != 23)
                    hora += 1
                else
                    hora = 0
            }

            var intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_HOUR, hora)
                putExtra(AlarmClock.EXTRA_MINUTES, minutos)
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarma creada por AppBotones")
            }


            if (intent.resolveActivity(packageManager) != null)
                startActivity(intent)
        }

        binding.btnWeb.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.w3schools.com")
            }

            startActivity(intent)
        }



        binding.btnMail.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_SEND).apply {
                type="*/*"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("dromrom0206@g.educaand.es"))
                putExtra(Intent.EXTRA_SUBJECT, "Correo desde AppBotones")
                putExtra(Intent.EXTRA_TEXT, "Este es el mensaje del correo")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        binding.btnDado.setOnClickListener { view ->
            val intent = Intent(this, Dados::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }

        binding.btnChistes.setOnClickListener { view ->
            val intent = Intent(this, Chistes::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }

        binding.pbProgressBar.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        Thread {
            Thread.sleep(2000)
            handler.post {
                binding.pbProgressBar.visibility = View.GONE
            }
        }.start()
    }
}