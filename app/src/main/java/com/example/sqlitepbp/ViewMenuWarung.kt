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

class ViewMenuWarung : AppCompatActivity() {

    private lateinit var DB: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewMenuWarungAdapter: ViewMenuWarungAdapter
    private var idWarung: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_menu_warung)

        DB = DBHelper(this)
        // Mendapatkan ID warung yang akan diambil dari intent
        idWarung = intent.getStringExtra("ID_WARUNG")

        recyclerView = findViewById(R.id.recyclerViewMenuWarung)
        viewMenuWarungAdapter = ViewMenuWarungAdapter(this, DB.viewMenuWarung())

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewMenuWarungAdapter

        viewMenuWarungAdapter.setOnViewMenuWarungItemClickListener(object : ViewMenuWarungAdapter.OnMenuWarungItemClickListener {
            override fun onMenuWarungItemClick(idMenu: String) {
                // Navigasi ke DetailMenu dengan mengirimkan ID MENU
                val intent = Intent(this@ViewMenuWarung, DetailMenu::class.java)
                intent.putExtra("ID_MENU", idMenu)
                startActivity(intent)
            }
        })



    }
}