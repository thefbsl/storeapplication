package com.example.store.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.eventbus.UpdateCartEvent
import com.example.store.model.Cart
import com.google.firebase.database.FirebaseDatabase
import org.greenrobot.eventbus.EventBus

class MyCartAdapter(
    private val context: Context,
    private val cartModelList: List<Cart>
): RecyclerView.Adapter<MyCartAdapter.MyCartViewHolder>() {

    class MyCartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var btnMinus: ImageView? = null
        var btnAdd: ImageView? = null
        var ivItem: ImageView? = null
        var txtName: TextView? = null
        var txtPrice: TextView? = null
        var txtQuantity: TextView? = null
        var btnDelete: ImageView? = null

        init {
            btnMinus = itemView.findViewById(R.id.btnMinus) as ImageView
            btnAdd = itemView.findViewById(R.id.btnAdd) as ImageView
            ivItem = itemView.findViewById(R.id.ivItem) as ImageView
            txtName = itemView.findViewById(R.id.txtName) as TextView
            txtPrice = itemView.findViewById(R.id.txtPrice) as TextView
            txtQuantity = itemView.findViewById(R.id.txtQuantity) as TextView
            btnDelete = itemView.findViewById(R.id.btnDelete) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartViewHolder {
        return MyCartViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.layout_cart_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyCartViewHolder, position: Int) {
        Glide.with(context)
            .load(cartModelList[position].image)
            .into(holder.ivItem!!)
        holder.txtName!!.text = StringBuilder().append(cartModelList[position].name)
        holder.txtPrice!!.text = StringBuilder("$").append(cartModelList[position].price)
        holder.txtQuantity!!.text = StringBuilder().append(cartModelList[position].quantity)

        holder.btnMinus!!.setOnClickListener{_ -> minusCartItem(holder, cartModelList[position])}
        holder.btnAdd!!.setOnClickListener{_ -> addCartItem(holder, cartModelList[position])}
        holder.btnDelete!!.setOnClickListener{_ ->
            val dialog = AlertDialog.Builder(context)
                .setTitle("Delete item")
                .setMessage("Do you really want to delete this item")
                .setNegativeButton("CANCEL") {dialog, _ -> dialog.dismiss()}
                .setPositiveButton("DELETE") {dialog, _ ->
                    notifyItemRemoved(position)
                    FirebaseDatabase.getInstance()
                        .getReference("Cart")
                        .child("UNIQUE_USER_ID")
                        .child(cartModelList[position].key!!)
                        .removeValue()
                        .addOnSuccessListener{
                            EventBus.getDefault().postSticky(UpdateCartEvent())
                        }
                }
                .create()
            dialog.show()
        }
    }

    private fun addCartItem(holder: MyCartViewHolder, cart: Cart) {
        cart.quantity += 1
        cart.totalPrice = cart.quantity * cart.price!!.toFloat()
        holder.txtQuantity!!.text = StringBuilder("").append(cart.quantity)
        updateFirebase(cart)
    }

    private fun minusCartItem(holder: MyCartViewHolder, cart: Cart) {
        if(cart.quantity > 1){
            cart.quantity -= 1
            cart.totalPrice = cart.quantity * cart.price!!.toFloat()
            holder.txtQuantity!!.text = StringBuilder("").append(cart.quantity)
            updateFirebase(cart)
        }
    }

    private fun updateFirebase(cart: Cart) {
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .child(cart.key!!)
            .setValue(cart)
            .addOnSuccessListener {
                EventBus.getDefault().postSticky(UpdateCartEvent())
            }

        FirebaseDatabase.getInstance()
            .getReference("users")
            .child("cart")
            .child(cart.key!!)
            .setValue(cart)
            .addOnSuccessListener {
                EventBus.getDefault().postSticky(UpdateCartEvent())
            }
    }

    override fun getItemCount(): Int {
        return cartModelList.size
    }
}