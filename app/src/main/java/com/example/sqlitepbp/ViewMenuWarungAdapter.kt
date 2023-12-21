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

class ViewMenuWarungAdapter(private val context: Context, private val cursor: Cursor) :
    RecyclerView.Adapter<ViewMenuWarungAdapter.MenuWarungViewHolder>() {

    class MenuWarungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaMenuTextView: TextView = itemView.findViewById(R.id.textViewNamaMenuWarung)
        val kategoriMenu: TextView = itemView.findViewById(R.id.textViewKategoriMenuWarung)
        val hargaMenu: TextView = itemView.findViewById(R.id.textViewHargaMenuWarung)
        val gambarMenu: ImageView = itemView.findViewById(R.id.viewgambarmenuwarung)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuWarungViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false)
        return MenuWarungViewHolder(view)
    }

    //Interface untuk listener klik item warung
    interface OnMenuWarungItemClickListener {
        fun onMenuWarungItemClick(idMenu: String)
    }

    //Variabel listener
    private var menuWarungItemClickListener: OnMenuWarungItemClickListener? = null

    // Metode untuk mengatur listener
    fun setOnViewMenuWarungItemClickListener(listener: OnMenuWarungItemClickListener) {
        menuWarungItemClickListener = listener
    }

    override fun onBindViewHolder(holder: MenuWarungViewHolder, position: Int) {
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
        holder.hargaMenu.text = "Rp $hargaMenu,00,-"
        // Load and display the image using Glide with caching
        Glide.with(context)
            .load(gambarMenu)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.gambarMenu)

        Log.d("WarungAdapter", "Image URL: ${gambarMenu}")

        //Menambahkan listener klik ke setiap item
        holder.itemView.setOnClickListener {
            menuWarungItemClickListener?.onMenuWarungItemClick(idMenu)
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}