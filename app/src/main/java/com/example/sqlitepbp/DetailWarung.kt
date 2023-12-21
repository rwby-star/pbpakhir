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

class DetailWarung : AppCompatActivity() {

    private lateinit var textViewIdWarungDetail: TextView
    private lateinit var textViewNamaWarungDetail: TextView
    private lateinit var previewGambar: ImageView
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonMenuWarung: Button
    private lateinit var dbHelper: DBHelper

    private var idWarung: String? = null
    private var PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_warung)

        textViewIdWarungDetail = findViewById(R.id.textViewIdWarungDetail)
        textViewNamaWarungDetail = findViewById(R.id.textViewNamaWarungDetail)
        previewGambar = findViewById(R.id.imageViewGambarDetail)
        buttonEdit = findViewById(R.id.buttonEdit)
        buttonMenuWarung = findViewById(R.id.buttonMenuWarung)
        buttonDelete = findViewById(R.id.buttonDelete)
        dbHelper = DBHelper(this)

        // Mendapatkan ID warung yang akan ditampilkan dari intent
        idWarung = intent.getStringExtra("ID_WARUNG")

        // Menampilkan data warung
        showWarungDetails(idWarung)

        // Menghapus Warung
        buttonDelete.setOnClickListener {
            val dbHelper = DBHelper(this)
            dbHelper.hapusWarung(idWarung ?: "")

            val intent = Intent(applicationContext, ViewWarung::class.java)
            startActivity(intent)
            finish() // Close the DetailWarung activity after deleting data
        }

        // Pindah ke Halaman EditMenu
        buttonEdit.setOnClickListener{
            val intent = Intent(this@DetailWarung, EditWarung::class.java)
            intent.putExtra("ID_WARUNG", idWarung)
            startActivity(intent)
        }

        // Pindah ke Halaman ViewMenuWarung
        buttonMenuWarung.setOnClickListener{
            val intent = Intent(this@DetailWarung, ViewMenuWarung::class.java)
            intent.putExtra("ID_WARUNG", idWarung)
            startActivity(intent)
        }
    }

    private fun showWarungDetails(idwarung: String?) {
        // Dapatkan data warung berdasarkan ID dari database
        val warungData = dbHelper.getDataWarung(idwarung)

        // Pastikan cursor tidak null dan pindah ke posisi pertama
        if (warungData != null && warungData.moveToFirst()) {
            // Tampilkan data warung pada TextView
            textViewIdWarungDetail.text = "${warungData.getString(warungData.getColumnIndex("idwarung"))}"
            textViewNamaWarungDetail.text = "${warungData.getString(warungData.getColumnIndex("namawarung"))}"

            val gambar = warungData.getString(warungData.getColumnIndex("gambar"))
            Log.d("DetailWarung", "Image URL: $gambar")

            Glide.with(this)
                .load(gambar)
                .placeholder(R.drawable.placeholder_image)
                .into(previewGambar)

//            textViewLogoDetail.text = "Logo: ${warungData.getString(warungData.getColumnIndex("logo"))}"
//            textViewGambarDetail.text = "Gambar: ${warungData.getString(warungData.getColumnIndex("gambar"))}"
        }
    }
}

