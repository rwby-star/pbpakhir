package com.example.sqlitepbp

import DBHelper
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import java.io.*

class CreateMeja : AppCompatActivity() {

    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_meja)

        val textidmenu = findViewById<EditText>(R.id.crtidmeja)
        val spinnerMejaWarung = findViewById<Spinner>(R.id.spinnerMejaWarung)
        val btncrtmenu = findViewById<ImageView>(R.id.crtmenu)

        DB = DBHelper(this)

        val idWarungList = DB.getIdWarung()

        val adapterIDWarung = ArrayAdapter(this, android.R.layout.simple_spinner_item, idWarungList)
        adapterIDWarung.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMejaWarung.adapter = adapterIDWarung

        btncrtmenu.setOnClickListener{
            val idMeja = textidmenu.text.toString()
            val spinnerMeja = spinnerMejaWarung.selectedItem.toString()

            if (idMeja.isEmpty() || spinnerMeja.isEmpty()) {
                Toast.makeText(this, "Harap semua field diisi", Toast.LENGTH_SHORT).show()
            } else {
                val insert = DB.insertMeja(idMeja, spinnerMeja)
                if (insert) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ViewMeja::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}