package com.example.dadosychistes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.dadosychistes.databinding.ActivitySecondBinding

class Second : AppCompatActivity() {
    private lateinit var binding : ActivitySecondBinding
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted)
            call()
        else
            Toast.makeText(this, "Error: permisos denegados", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initEvent()
    }

    private fun initEvent() {
        binding.btnCall.setOnClickListener { view ->
            requestPermission()
        }

        binding.btnBack.setOnClickListener { view ->
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasPermission())
                call()
            else
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        } else
            call()
    }

    private fun call() {
        var intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:603779410")
        }
        startActivity(intent)
    }

    private fun hasPermission() : Boolean = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
}