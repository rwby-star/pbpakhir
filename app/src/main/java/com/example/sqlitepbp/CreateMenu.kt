package com.example.sqlitepbp

import DBHelper
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class CreateMenu : AppCompatActivity() {

    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_menu)

        val textidmenu = findViewById<EditText>(R.id.crtidmenu)
        val textnamamenu = findViewById<EditText>(R.id.crtnamamenu)
        val texthargamenu = findViewById<EditText>(R.id.crthargamenu)
        val textkategorimenu = findViewById<EditText>(R.id.crtkategori)
//        val previewImageMenu = findViewById<ImageView>(R.id.inputGambarMenu)
        val btnInputImageMenu = findViewById<Button>(R.id.btnInputGambarMenu)
        val btncrtmenu = findViewById<ImageView>(R.id.crtmenu)

        DB = DBHelper(this)

        btnInputImageMenu.setOnClickListener {
            openGalleryGambar()
        }

        btncrtmenu.setOnClickListener{
            val idMenu = textidmenu.text.toString()
            val namaMenu = textnamamenu.text.toString()
            val hargamenu = texthargamenu.text.toString()
            val kategorimenu = textkategorimenu.text.toString()
            val gambarmenu = selectedImageUri.toString()

            if (idMenu.isEmpty() || namaMenu.isEmpty() || hargamenu.isEmpty() || kategorimenu.isEmpty() || gambarmenu.isEmpty()) {
                Toast.makeText(this, "Harap semua field diisi", Toast.LENGTH_SHORT).show()
            } else {
                val insert = DB.insertMenu(idMenu, namaMenu, hargamenu, kategorimenu, gambarmenu)
                if (insert) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ViewMenu::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var selectedImageUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1

    private fun openGalleryGambar() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    // Ini untuk pemilihan gambar
                    selectedImageUri = data.data // Memperbarui variabel kelas dengan URI gambar yang dipilih
                    val previewGambar = findViewById<ImageView>(R.id.inputGambarMenu)
                    previewGambar.setImageURI(selectedImageUri)
                    previewGambar.visibility = View.VISIBLE
                }
                else -> {
                    // Jika requestCode tidak sesuai dengan yang diharapkan
                    // Tambahkan penanganan kesalahan jika diperlukan
                }
            }
        }
    }
}