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

class DetailMenu : AppCompatActivity() {

    private lateinit var textViewNamaMenuDetail: TextView
    private lateinit var textViewHargaMenuDetail: TextView
    private lateinit var textViewKategoriMenuDetail: TextView
    private lateinit var previewGambar: ImageView
    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button
    private lateinit var dbHelper: DBHelper

    private var idMenu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_menu)

        textViewNamaMenuDetail = findViewById(R.id.textViewNamaMenu)
        textViewHargaMenuDetail = findViewById(R.id.textViewHargaMenu)
        textViewKategoriMenuDetail = findViewById(R.id.textViewKategoriMenu)
        previewGambar = findViewById(R.id.viewgambarmenu)
        buttonEdit = findViewById(R.id.buttonEdit)
        buttonDelete = findViewById(R.id.buttonDelete)
        dbHelper = DBHelper(this)

        // Mendapatkan ID warung yang akan ditampilkan dari intent
        idMenu = intent.getStringExtra("ID_MENU")

        // Menampilkan data warung
        showMenuDetail(idMenu)

        buttonDelete.setOnClickListener {
            val dbHelper = DBHelper(this)
            dbHelper.hapusMenu(idMenu ?: "")

            val intent = Intent(applicationContext, ViewMenu::class.java)
            startActivity(intent)
            finish() // Close the DetailWarung activity after deleting data
        }

        buttonEdit.setOnClickListener{
            val intent = Intent(this@DetailMenu, EditMenu::class.java)
            intent.putExtra("ID_MENU", idMenu)
            startActivity(intent)
        }
    }

    private fun showMenuDetail(idMenu: String?) {
        // Dapatkan data warung berdasarkan ID dari database
        val menuData = dbHelper.getDataMenu(idMenu)

        // Pastikan cursor tidak null dan pindah ke posisi pertama
        if (menuData != null && menuData.moveToFirst()) {
            // Tampilkan data warung pada TextView
            textViewNamaMenuDetail.text = "${menuData.getString(menuData.getColumnIndex("namamenu"))}"
            textViewHargaMenuDetail.text = "Rp ${menuData.getString(menuData.getColumnIndex("hargamenu"))},00,-"
            textViewKategoriMenuDetail.text = "${menuData.getString(menuData.getColumnIndex("kategorimenu"))}"

            val gambar = menuData.getString(menuData.getColumnIndex("gambarmenu"))
            Log.d("DetailMenu", "Image URL: $gambar")

            Glide.with(this)
                .load(gambar)
                .placeholder(R.drawable.placeholder_image)
                .into(previewGambar)
        }
    }
}

