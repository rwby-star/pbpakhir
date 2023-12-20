package com.example.sqlitepbp

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class WarungAdapter(private val context: Context, private val cursor: Cursor) :
    RecyclerView.Adapter<WarungAdapter.WarungViewHolder>() {

    class WarungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idWarungTextView: TextView = itemView.findViewById(R.id.textViewIdWarung)
        val namaWarungTextView: TextView = itemView.findViewById(R.id.textViewNamaWarung)
        val imagelogo: ImageView = itemView.findViewById(R.id.imageviewlogo)
        val imagegambar: ImageView = itemView.findViewById(R.id.imageviewgambar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarungViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_warung, parent, false)
        return WarungViewHolder(view)
    }

    // Interface untuk listener klik item warung
    interface OnWarungItemClickListener {
        fun onWarungItemClick(idWarung: String)
    }

    // Variabel listener
    private var warungItemClickListener: OnWarungItemClickListener? = null

    // Metode untuk mengatur listener
    fun setOnWarungItemClickListener(listener: OnWarungItemClickListener) {
        warungItemClickListener = listener
    }

    override fun onBindViewHolder(holder: WarungViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) {
            return
        }

        val idWarung = cursor.getString(cursor.getColumnIndex("idwarung"))
        val namaWarung = cursor.getString(cursor.getColumnIndex("namawarung"))
        val logo = cursor.getString(cursor.getColumnIndex("logo"))
        val gambar = cursor.getString(cursor.getColumnIndex("gambar"))

        holder.idWarungTextView.text = "$idWarung"
        holder.namaWarungTextView.text = "$namaWarung"
//        holder.logoTextView.text = "Logo: $logo"
//        holder.gambarTextView.text = "Gambar: $gambar"
        // Load and display the image using Glide with caching
        Glide.with(context)
            .load(logo)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imagelogo)

        Log.d("WarungAdapter", "Image URL: ${logo}")

        Glide.with(context)
            .load(gambar)
            .placeholder(R.drawable.placeholder_image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imagegambar)

        Log.d("WarungAdapter", "Image URL: ${gambar}")

        // Menambahkan listener klik ke setiap item
        holder.itemView.setOnClickListener {
            warungItemClickListener?.onWarungItemClick(idWarung)
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}
