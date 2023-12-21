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
import java.io.*

class CreateWarung : AppCompatActivity() {
    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_warung)

        val textidwarung = findViewById<EditText>(R.id.crtidwarung)
        val textnamawarung = findViewById<EditText>(R.id.crtnamawarung)
        val btnUploadImage = findViewById<Button>(R.id.crtimage)
        val btnUploadLogo = findViewById<Button>(R.id.crtlogo)
        val buttoncreate = findViewById<ImageView>(R.id.crtwarung)
        DB = DBHelper(this)

        btnUploadImage.setOnClickListener {
            openGalleryGambar()
        }

        btnUploadLogo.setOnClickListener {
            openGalleryLogo()
        }

        buttoncreate.setOnClickListener{
            val idWarung = textidwarung.text.toString()
            val namaWarung = textnamawarung.text.toString()
            val logo = selectedLogoUri.toString()
            val gambar = selectedImageUri.toString()

            if (idWarung.isEmpty() || namaWarung.isEmpty() || logo.isEmpty() || gambar.isEmpty()) {
                Toast.makeText(this, "Harap semua field diisi", Toast.LENGTH_SHORT).show()
            } else {
                val insert = DB.insertWarung(idWarung, namaWarung, logo, gambar)
                if (insert) {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ViewWarung::class.java)
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
    var selectedLogoUri: Uri? = null

    private val PICK_IMAGE_REQUEST = 1
    private val PICK_LOGO_REQUEST = 2

    private fun openGalleryGambar() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openGalleryLogo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_LOGO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedImageUri = copyImageToInternalStorage(data.data)
                    val previewGambar = findViewById<ImageView>(R.id.inputGambar)
                    previewGambar.setImageURI(selectedImageUri)
                    previewGambar.visibility = View.VISIBLE
                }
                PICK_LOGO_REQUEST -> {
                    selectedLogoUri = copyImageToInternalStorage(data.data)
                    val previewLogo = findViewById<ImageView>(R.id.inputLogo)
                    previewLogo.setImageURI(selectedLogoUri)
                    previewLogo.visibility = View.VISIBLE
                }
                else -> {
                    // Jika requestCode tidak sesuai dengan yang diharapkan
                    // Tambahkan penanganan kesalahan jika diperlukan
                }
            }
        }
    }
}