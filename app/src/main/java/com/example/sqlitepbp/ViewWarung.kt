package com.example.sqlitepbp

import DBHelper
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ViewWarung : AppCompatActivity() {

    private lateinit var DB: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var warungAdapter: WarungAdapter
    private lateinit var btnBack: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_warung)

        DB = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewWarung)
        warungAdapter = WarungAdapter(this, DB.viewWarung())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = warungAdapter

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, CreateWarung::class.java)
            startActivity(intent)
        }

        // Inisialisasi BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set listener untuk BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnStore -> {
                    // Tidak perlu melakukan apa-apa karena kita berada di halaman ViewWarung
                    true
                }
                R.id.btnMenu -> {
                    // Navigasi ke halaman Menu
                    val intent = Intent(this, ViewMenu::class.java) // Gantikan dengan nama Activity Menu Anda
                    startActivity(intent)
                    true
                }
                R.id.btnTable -> {
                    // Navigasi ke halaman Meja
                    val intent = Intent(this, MejaActivity::class.java) // Gantikan dengan nama Activity Meja Anda
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        warungAdapter.setOnWarungItemClickListener(object : WarungAdapter.OnWarungItemClickListener {
            override fun onWarungItemClick(idwarung: String) {
                // Navigasi ke DetailActivity dengan mengirimkan ID Warung
                val intent = Intent(this@ViewWarung, DetailWarung::class.java)
                intent.putExtra("ID_WARUNG", idwarung)
                startActivity(intent)
            }
        })
    }
}
