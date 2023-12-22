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

class ViewMejaWarungAdapter(private val context: Context, private val cursor: Cursor) :
    RecyclerView.Adapter<ViewMejaWarungAdapter.MejaWarungViewHolder>() {

    class MejaWarungViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewIdMeja: TextView = itemView.findViewById(R.id.textViewIdMejaWarung)
        val textViewStatusMeja: TextView = itemView.findViewById(R.id.textViewStatusMejaWarung)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MejaWarungViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_meja_warung, parent, false)
        return MejaWarungViewHolder(view)
    }

    //Interface untuk listener klik item warung
    interface OnMejaWarungItemClickListener {
        fun onMejaWarungItemClick(idMeja: String)
    }

    //Variabel listener
    private var mejaWarungItemClickListener: OnMejaWarungItemClickListener? = null

    // Metode untuk mengatur listener
    fun setOnViewMejaWarungItemClickListener(listener: OnMejaWarungItemClickListener) {
        mejaWarungItemClickListener = listener
    }

    override fun onBindViewHolder(holder: MejaWarungViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) {
            return
        }
        val idMeja = cursor.getString(cursor.getColumnIndex("idmeja"))
        val statusMeja = cursor.getString(cursor.getColumnIndex("status"))

        holder.textViewIdMeja.text = "ID Meja: $idMeja"
        holder.textViewStatusMeja.text = "Status: $statusMeja"

        //Menambahkan listener klik ke setiap item
        holder.itemView.setOnClickListener {
            mejaWarungItemClickListener?.onMejaWarungItemClick(idMeja)
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}