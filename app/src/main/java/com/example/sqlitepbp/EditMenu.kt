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
import android.widget.Spinner
import android.widget.TextView
import java.io.*

class EditMenu : AppCompatActivity() {

    private lateinit var editTextIdMenu: TextView
    private lateinit var editTextNamaMenu: EditText
    private lateinit var editTextHargaMenu: EditText
    private lateinit var editTextKategoriMenu: Spinner
    private lateinit var inputGambar: ImageView
    private lateinit var btnGambar: Button
    private lateinit var btnUpdate: Button
    private lateinit var dbHelper: DBHelper

    private var idMenu: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        editTextIdMenu = findViewById(R.id.editTextIdMenu)
        editTextNamaMenu = findViewById(R.id.editTextNamaMenu)
        editTextHargaMenu = findViewById(R.id.editTextHargaMenu)
        editTextKategoriMenu = findViewById(R.id.spinnerKategori)
        inputGambar = findViewById(R.id.inputgambarpreview)
        btnGambar = findViewById(R.id.btnGambar)
        btnUpdate = findViewById(R.id.btnUpdate)
        dbHelper = DBHelper(this)

        btnGambar.setOnClickListener {
            openGalleryGambar()
        }

        // Mendapatkan ID warung yang akan diedit dari intent
        idMenu = intent.getStringExtra("ID_MENU")

        // Mengisi formulir dengan informasi warung yang ada
        fillFormWithWarungData(idMenu)

        // Set listener untuk tombol Update
        btnUpdate.setOnClickListener {
            updateMenuData()
        }
    }

    private fun fillFormWithWarungData(idMenu: String?) {
        // Dapatkan data warung berdasarkan ID dari database
        val menuData = dbHelper.getDataMenu(idMenu)

        // Isi formulir dengan data warung
        if (menuData != null && menuData.moveToFirst()) {
            editTextIdMenu.setText(menuData.getString(menuData.getColumnIndex("idmenu")))
            editTextNamaMenu.setText(menuData.getString(menuData.getColumnIndex("namamenu")))
            editTextHargaMenu.setText(menuData.getString(menuData.getColumnIndex("hargamenu")))
//            editTextKategoriMenu.selectedItem.toString(menuData.getString(menuData.getColumnIndex("kategorimenu")))

            val gambar = menuData.getString(menuData.getColumnIndex("gambarmenu"))

            Glide.with(this)
                .load(gambar)
                .placeholder(R.drawable.placeholder_image)
                .into(inputGambar)

            Log.d("EditMenu", "Image URL: $gambar")
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

    private fun updateMenuData() {
        val idMenu = editTextIdMenu.text.toString()
        val namaMenu = editTextNamaMenu.text.toString()
        val hargaMenu = editTextHargaMenu.text.toString()
        val kategoriMenu = editTextKategoriMenu.selectedItem.toString()

        // Inisialisasi dengan data sebelumnya
        var gambarmenu = dbHelper.getMenuGambarById(idMenu) ?: ""

        // Jika user memilih gambar baru, salin ke penyimpanan internal
        if (selectedImageUri != null) {
            val gambarInternalUri = copyImageToInternalStorage(selectedImageUri)
            gambarmenu = gambarInternalUri?.toString() ?: gambarmenu
        }

        // Panggil fungsi untuk memperbarui data warung di database
        val isUpdated = dbHelper.updateMenu(idMenu, namaMenu, hargaMenu,kategoriMenu,gambarmenu)

        if (isUpdated) {
            val viewMenuIntent = Intent(this, ViewMenu::class.java)
            startActivity(viewMenuIntent)
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

