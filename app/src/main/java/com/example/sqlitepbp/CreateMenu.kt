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

class CreateMenu : AppCompatActivity() {

    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_menu)

        val textidmenu = findViewById<EditText>(R.id.crtidmenu)
        val textnamamenu = findViewById<EditText>(R.id.crtnamamenu)
        val texthargamenu = findViewById<EditText>(R.id.crthargamenu)
        val textkategorimenu = findViewById<Spinner>(R.id.kategoriMenu)
        val spinnerWarung = findViewById<Spinner>(R.id.spinnerWarung)
        val btnInputImageMenu = findViewById<Button>(R.id.btnInputGambarMenu)
        val btncrtmenu = findViewById<ImageView>(R.id.crtmenu)

        DB = DBHelper(this)

        btnInputImageMenu.setOnClickListener {
            openGalleryGambar()
        }

        val idWarungList = DB.getIdWarung()

        val adapterIDWarung = ArrayAdapter(this, android.R.layout.simple_spinner_item, idWarungList)
        adapterIDWarung.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWarung.adapter = adapterIDWarung

        btncrtmenu.setOnClickListener{
            val idMenu = textidmenu.text.toString()
            val namaMenu = textnamamenu.text.toString()
            val hargamenu = texthargamenu.text.toString()
            val kategorimenu = textkategorimenu.selectedItem.toString()
            val gambarmenu = selectedImageUri.toString()
            val spinnerMenu = spinnerWarung.selectedItem.toString()

            if (idMenu.isEmpty() || namaMenu.isEmpty() || hargamenu.isEmpty() || kategorimenu.isEmpty() || gambarmenu.isEmpty() || spinnerMenu.isEmpty()) {
                Toast.makeText(this, "Harap semua field diisi", Toast.LENGTH_SHORT).show()
            } else {
                val insert = DB.insertMenu(idMenu, namaMenu, hargamenu, kategorimenu, gambarmenu, spinnerMenu)
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

    private fun copyImageToInternalStorage(uri: Uri?): Uri? {
        if (uri == null) return null

        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(filesDir, "${System.currentTimeMillis()}.jpg") // Membuat nama file unik

        try {
            val outputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream?.read(buf).also { len = it!! } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return Uri.fromFile(file)
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
                    selectedImageUri = copyImageToInternalStorage(data.data)
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