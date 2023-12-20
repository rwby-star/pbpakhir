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

class ViewMenu : AppCompatActivity() {

    private lateinit var DB: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var btnBack: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_menu)

        DB = DBHelper(this)
        recyclerView = findViewById(R.id.recyclerViewMenu)
        menuAdapter = MenuAdapter(this, DB.viewMenu())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = menuAdapter

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intent = Intent(this, CreateMenu::class.java)
            startActivity(intent)
        }

        // Inisialisasi BottomNavigationView
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set listener untuk BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btnStore -> {
                    val intent = Intent(this, ViewWarung::class.java) // Gantikan dengan nama Activity Menu Anda
                    startActivity(intent)
                    true
                }
                R.id.btnMenu -> {
                    // Navigasi ke halaman Menu

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




    }
}