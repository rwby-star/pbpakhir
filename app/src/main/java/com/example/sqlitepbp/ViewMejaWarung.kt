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

class ViewMejaWarung : AppCompatActivity() {

    private lateinit var DB: DBHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewMejaWarungAdapter: ViewMejaWarungAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_meja_warung)

        DB = DBHelper(this)
        // Mendapatkan ID warung yang akan diambil dari intent
        val idWarung = intent.getStringExtra("ID_WARUNG")

        recyclerView = findViewById(R.id.recyclerViewMejaWarung)
        viewMejaWarungAdapter = ViewMejaWarungAdapter(this, DB.viewMejaWarung(idWarung ?: ""))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = viewMejaWarungAdapter

        viewMejaWarungAdapter.setOnViewMejaWarungItemClickListener(object : ViewMejaWarungAdapter.OnMejaWarungItemClickListener {
            override fun onMejaWarungItemClick(idMeja: String) {
                // Navigasi ke DetailMenu dengan mengirimkan ID MENU
                val intent = Intent(this@ViewMejaWarung, MejaDetail::class.java)
                intent.putExtra("ID_MEJA", idMeja)
                startActivity(intent)
            }
        })



    }
}