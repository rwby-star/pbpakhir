package com.example.sqlitepbp

import DBHelper
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.content.Context
import android.view.View
import java.io.*

class EditWarung : AppCompatActivity() {

    private lateinit var editTextIdWarung: EditText
    private lateinit var editTextNamaWarung: EditText
    private lateinit var inputLogo: ImageView
    private lateinit var inputGambar: ImageView
//    private lateinit var editTextLogo: EditText
//    private lateinit var editTextGambar: EditText
    private lateinit var btnLogo: Button
    private lateinit var btnGambar: Button
    private lateinit var btnUpdate: Button
    private lateinit var dbHelper: DBHelper

    private var idWarung: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_warung)

        editTextIdWarung = findViewById(R.id.editTextIdWarung)
        editTextNamaWarung = findViewById(R.id.editTextNamaWarung)
        inputLogo = findViewById(R.id.inputlogopreview)
        inputGambar = findViewById(R.id.inputgambarpreview)
//        editTextLogo = findViewById(R.id.editTextLogo)
//        editTextGambar = findViewById(R.id.editTextGambar)
        btnLogo = findViewById(R.id.btnLogo)
        btnGambar = findViewById(R.id.btnGambar)
        btnUpdate = findViewById(R.id.btnUpdate)
        dbHelper = DBHelper(this)

        btnLogo.setOnClickListener {
            openGalleryLogo()
        }

        btnGambar.setOnClickListener {
            openGalleryGambar()
        }

        // Mendapatkan ID warung yang akan diedit dari intent
        idWarung = intent.getStringExtra("ID_WARUNG")

        // Mengisi formulir dengan informasi warung yang ada
        fillFormWithWarungData(idWarung)

        // Set listener untuk tombol Update
        btnUpdate.setOnClickListener {
            updateWarungData()
        }
    }

    private fun fillFormWithWarungData(idwarung: String?) {
        // Dapatkan data warung berdasarkan ID dari database
        val warungData = dbHelper.getDataWarung(idwarung)

        // Isi formulir dengan data warung
        if (warungData != null && warungData.moveToFirst()) {
            editTextIdWarung.setText(warungData.getString(warungData.getColumnIndex("idwarung")))
            editTextNamaWarung.setText(warungData.getString(warungData.getColumnIndex("namawarung")))
        }
    }

//    private fun updateWarungData() {
//        val idWarung = editTextIdWarung.text.toString()
//        val namaWarung = editTextNamaWarung.text.toString()
//        val logo = selectedLogoUri.toString()
//        val gambar = selectedImageUri.toString()
//
//        // Panggil fungsi untuk memperbarui data warung di database
//        val isUpdated = dbHelper.updateWarung(idWarung, namaWarung, logo, gambar)
//
//        if (isUpdated) {
//            val viewWarungIntent = Intent(this, ViewWarung::class.java)
//            startActivity(viewWarungIntent)
//            finish() // Kembali ke halaman sebelumnya setelah pembaruan berhasil
//        } else {
//            // Gagal memperbarui data warung
//            // Lakukan sesuatu jika pembaruan gagal
//            // Misalnya, tampilkan pesan kesalahan
//            Toast.makeText(this, "Gagal memperbarui data warung", Toast.LENGTH_SHORT).show()
//        }
//    }

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

    private fun updateWarungData() {
        val idWarung = editTextIdWarung.text.toString()
        val namaWarung = editTextNamaWarung.text.toString()

        // Menyalin gambar ke penyimpanan internal sebelum menyimpan URI ke database
        val logoInternalUri = copyImageToInternalStorage(selectedLogoUri)
        val gambarInternalUri = copyImageToInternalStorage(selectedImageUri)

        val logo = logoInternalUri?.toString() ?: ""
        val gambar = gambarInternalUri?.toString() ?: ""

        // Panggil fungsi untuk memperbarui data warung di database
        val isUpdated = dbHelper.updateWarung(idWarung, namaWarung, logo, gambar)

        if (isUpdated) {
            val viewWarungIntent = Intent(this, ViewWarung::class.java)
            startActivity(viewWarungIntent)
            finish() // Kembali ke halaman sebelumnya setelah pembaruan berhasil
        } else {
            // Gagal memperbarui data warung
            Toast.makeText(this, "Gagal memperbarui data warung", Toast.LENGTH_SHORT).show()
        }
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
                    // Ini untuk pemilihan gambar
                    selectedImageUri = data.data // Memperbarui variabel kelas dengan URI gambar yang dipilih
                    val previewGambar = findViewById<ImageView>(R.id.inputgambarpreview)
                    previewGambar.setImageURI(selectedImageUri)
                    previewGambar.visibility = View.VISIBLE
                }
                PICK_LOGO_REQUEST -> {
                    // Ini untuk pemilihan logo
                    selectedLogoUri = data.data // Memperbarui variabel kelas dengan URI logo yang dipilih
                    val previewLogo = findViewById<ImageView>(R.id.inputlogopreview)
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

