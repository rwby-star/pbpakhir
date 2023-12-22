package com.example.sqlitepbp

import DBHelper
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class MejaDetail : AppCompatActivity() {

    private lateinit var TextViewIdMeja: TextView
    private lateinit var TextViewIdWarung: TextView
    private lateinit var TextViewStatusMeja: TextView
    private lateinit var buttonDelete: Button
    private lateinit var dbHelper: DBHelper

    private var idMeja: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meja_detail)

        TextViewIdMeja = findViewById(R.id.idMeja)
        TextViewIdWarung = findViewById(R.id.idWarung)
        TextViewStatusMeja = findViewById(R.id.statusMeja)
        buttonDelete = findViewById(R.id.buttonDelete)
        dbHelper = DBHelper(this)

        // Mendapatkan ID meja yang akan ditampilkan dari intent
        idMeja = intent.getStringExtra("ID_MEJA")

        // Menampilkan data meja
        showMejaDetail(idMeja)

        buttonDelete.setOnClickListener {
            val dbHelper = DBHelper(this)
            dbHelper.hapusMeja(idMeja ?: "")

            val intent = Intent(applicationContext, ViewMeja::class.java)
            startActivity(intent)
            finish() // Close the DetailMeja activity after deleting data
        }
    }

    private fun showMejaDetail(idMeja: String?) {
        // Dapatkan data warung berdasarkan ID dari database
        val mejaData = dbHelper.getDataMeja(idMeja)

        // Pastikan cursor tidak null dan pindah ke posisi pertama
        if (mejaData != null && mejaData.moveToFirst()) {
            // Tampilkan data warung pada TextView
            TextViewIdMeja.text = "ID Meja: ${mejaData.getString(mejaData.getColumnIndex("idmeja"))}"
            TextViewIdWarung.text = "ID Warung: ${mejaData.getString(mejaData.getColumnIndex("idwarung"))}"
            TextViewStatusMeja.text = "Status Meja: ${mejaData.getString(mejaData.getColumnIndex("status"))}"
        }
    }
}

