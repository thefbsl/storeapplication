package com.example.store.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.listener.AddToCart
import com.example.store.model.Item
import com.squareup.picasso.Picasso

class ItemAdapter(
    private val items: ArrayList<Item>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textViewTitle: TextView = view.findViewById(R.id.tvItemName)
        val textViewDesc: TextView = view.findViewById(R.id.tvItemDesc)
        val textViewPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val imageItemView: ImageView = view.findViewById(R.id.ivItem)
        val button: Button = view.findViewById(R.id.btnAddItemToCart)
        private var btnClickListener: AddToCart? = null

        fun setBtnClickListener(btnClickListener: AddToCart){
            this.btnClickListener = btnClickListener
        }
        init {
            button.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            btnClickListener!!.onAddBtnListener(v, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        holder.textViewTitle.text = currentItem.getName()
        holder.textViewPrice.text = currentItem.getPrice().toString()
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



