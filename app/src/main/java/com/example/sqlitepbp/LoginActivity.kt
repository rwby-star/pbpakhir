package com.example.sqlitepbp

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
    private lateinit var signup: Button
    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username1)
        password = findViewById(R.id.password1)
        btnLogin = findViewById(R.id.btnsignin1)
        signup = findViewById(R.id.btnsignup)
        DB = DBHelper(this)

        btnLogin.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Semua Kolom Harus Diisi", Toast.LENGTH_SHORT).show()
            } else {
                val checkUserPass = DB.checkUsernamePassword(user, pass)
                if (checkUserPass) {
                    Toast.makeText(this, "Selamat Datang", Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, ViewWarung::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Data Akun Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
        signup.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
    }

}
