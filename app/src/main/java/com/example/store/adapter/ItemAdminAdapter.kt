package com.example.store.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.Item
import com.squareup.picasso.Picasso

class ItemAdminAdapter(
    private val context: Context,
    private val items: ArrayList<Item>
) : RecyclerView.Adapter<ItemAdminAdapter.ItemAdminViewHolder>() {

    class ItemAdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.tvItemAdminTitle)
        val textViewDesc: TextView = view.findViewById(R.id.tvItemAdminDesc)
        val textViewPrice: TextView = view.findViewById(R.id.tvItemAdminPrice)
        val imageItemView: ImageView = view.findViewById(R.id.ivAdminItemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdminViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_item, parent, false)
        return ItemAdminViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemAdminViewHolder, position: Int) {
        val currentItem = items[position]

        holder.textViewTitle.text = currentItem.getName()
        holder.textViewPrice.text = currentItem.getPrice().toString() + " $ "
        holder.textViewDesc.text = currentItem.getDesc()

        val itemTarget = currentItem.getImageUrl()

        Picasso.get()
            .load(itemTarget)
            .into(holder.imageItemView)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}



