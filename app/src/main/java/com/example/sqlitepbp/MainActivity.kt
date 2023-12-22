package com.example.sqlitepbp

import DBHelper
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var repassword: EditText
    private lateinit var signup: Button
    private lateinit var signin: Button
    private lateinit var DB: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        repassword = findViewById(R.id.repassword)
        signup = findViewById(R.id.btnsignup)
        signin = findViewById(R.id.btnsignin)
        DB = DBHelper(this)

        signup.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val repass = repassword.text.toString()

            if (user.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                Toast.makeText(this, "Semua Kolom Harus Diisi", Toast.LENGTH_SHORT).show()
            } else {
                if (pass == repass) {
                    val checkuser = DB.checkUsername(user)
                    if (!checkuser) {
                        val insert = DB.insertData(user, pass)
                        if (insert) {
                            Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Akun yang Sama Ditemukan, Harap Login !", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Password Tidak Sama", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
