package com.example.sqlitepbp

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import org.w3c.dom.Text

class MenuAdapter(private val context: Context, private val cursor: Cursor) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMenuTextView: TextView = itemView.findViewById(R.id.textViewNamaMenu)
        val kategoriMenu: TextView = itemView.findViewById(R.id.textViewKategoriMenu)
        val hargaMenu: TextView = itemView.findViewById(R.id.textViewHargaMenu)
        val gambarMenu: ImageView = itemView.findViewById(R.id.viewgambarmenu)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    // Interface untuk listener klik item warung
//    interface OnWarungItemClickListener {
//        fun onWarungItemClick(idWarung: String)
//    }

    // Variabel listener
//    private var warungItemClickListener: OnWarungItemClickListener? = null
//
//    // Metode untuk mengatur listener
//    fun setOnWarungItemClickListener(listener: OnWarungItemClickListener) {
//        warungItemClickListener = listener
//    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) {
            return
        }

        val idMenu = cursor.getString(cursor.getColumnIndex("idmenu"))
        val namaMenu = cursor.getString(cursor.getColumnIndex("namamenu"))
        val hargaMenu = cursor.getString(cursor.getColumnIndex("hargamenu"))
        val kategoriMenu = cursor.getString(cursor.getColumnIndex("kategorimenu"))
        val gambarMenu = cursor.getString(cursor.getColumnIndex("gambarmenu"))

        holder.namaMenuTextView.text = "$namaMenu"
        holder.kategoriMenu.text = "$kategoriMenu"
        holder.hargaMenu.text = "$hargaMenu"
//        holder.logoTextView.text = "Logo: $logo"
//        holder.gambarTextView.text = "Gambar: $gambar"
        // Load and display the image using Glide with caching
        Glide.with(context)
            .load(gambarMenu)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.gambarMenu)

        Log.d("WarungAdapter", "Image URL: ${gambarMenu}")

        // Menambahkan listener klik ke setiap item
//        holder.itemView.setOnClickListener {
//            warungItemClickListener?.onWarungItemClick(idWarung)
//        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}