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

class MejaAdapter(private val context: Context, private val cursor: Cursor) :
    RecyclerView.Adapter<MejaAdapter.MejaViewHolder>() {

    class MejaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idMeja: TextView = itemView.findViewById(R.id.textViewIdMeja)
        val statusMeja: TextView = itemView.findViewById(R.id.textViewStatusMeja)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MejaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_meja, parent, false)
        return MejaViewHolder(view)
    }

    //Interface untuk listener klik item warung
    interface OnMejaItemClickListener {
        fun onMejaItemClick(idMenu: String)
    }

    //Variabel listener
    private var mejaItemClickListener: OnMejaItemClickListener? = null

    // Metode untuk mengatur listener
    fun setOnMejaItemClickListener(listener: OnMejaItemClickListener) {
        mejaItemClickListener = listener
    }

    override fun onBindViewHolder(holder: MejaViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) {
            return
        }
        val idMeja = cursor.getString(cursor.getColumnIndex("idmeja"))
        val statusMeja = cursor.getString(cursor.getColumnIndex("status"))

        holder.idMeja.text = "$idMeja"
        holder.statusMeja.text = "$statusMeja"

        //Menambahkan listener klik ke setiap item
        holder.itemView.setOnClickListener {
            mejaItemClickListener?.onMejaItemClick(idMeja)
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}