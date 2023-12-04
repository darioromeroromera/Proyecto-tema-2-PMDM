package com.example.dadosychistes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import com.example.dadosychistes.databinding.ActivityConfigBinding
import java.time.LocalDate

class ConfigActivity : AppCompatActivity() {
    private lateinit var binding : ActivityConfigBinding
    private lateinit var adapter : ArrayAdapter<String>
    private lateinit var spinnerItems : MutableList<String>
    private var hasRadioBeenSelected : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initList()
        initAdapter()
        initEvent()
    }

    private fun initEvent() {
        binding.rgRadioGroup.setOnCheckedChangeListener { group, checkedID ->
            hasRadioBeenSelected = true
        }

        binding.btnContinue.setOnClickListener { view ->
            if (binding.etName.text.toString().trim().isNotEmpty() && hasRadioBeenSelected)
                createIntent()
            else {
                Toast.makeText(this, "Los campos de nombre y de número de tiradas son obligatorios", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun createIntent() {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString("name", binding.etName.text.toString().trim())
        val radioID = binding.rgRadioGroup.checkedRadioButtonId
        when (radioID) {
            binding.rbRadio1.id -> bundle.putInt("throwNumber", Integer.parseInt(binding.rbRadio1.text.toString()))
            binding.rbRadio2.id -> bundle.putInt("throwNumber", Integer.parseInt(binding.rbRadio2.text.toString()))
            binding.rbRadio3.id -> bundle.putInt("throwNumber", Integer.parseInt(binding.rbRadio3.text.toString()))
            binding.rbRadio5.id -> bundle.putInt("throwNumber", Integer.parseInt(binding.rbRadio5.text.toString()))
        }
        bundle.putInt("throwTime", binding.spinner.selectedItemPosition + 1)
        bundle.putBoolean("useLongList", binding.cbUseLongList.isChecked)
        intent.putExtra("bundle", bundle)
        startActivity(intent)
    }

    private fun initList() {
        spinnerItems = Spinner_items.listaItems.toMutableList()
    }

    private fun initAdapter() {
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            spinnerItems
        )

        binding.spinner.adapter = adapter
    }
}