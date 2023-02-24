package com.example.store.adapter

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
import com.example.store.listener.CartLoadListener
import com.example.store.listener.RecycleClickListener
import com.example.store.model.Cart
import com.example.store.model.Item
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus

class MyItemAdapter(
    private val context: Context,
    private val list: List<Item>,
    private val cartListener: CartLoadListener
): RecyclerView.Adapter<MyItemAdapter.MyItemViewHolder>() {

    class MyItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var imageView: ImageView
        var name: TextView
        var price: TextView
        //var desc: TextView
        private var clickListener: RecycleClickListener? = null

        fun setClickListener(clickListener: RecycleClickListener){
            this.clickListener = clickListener
        }

        init {
            imageView = itemView.findViewById(R.id.ivItem) as ImageView
            name = itemView.findViewById(R.id.tvItemName) as TextView
            price = itemView.findViewById(R.id.tvItemPrice) as TextView
            //desc = itemView.findViewById(R.id.tvItemDesc) as TextView

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            clickListener!!.onItemClickListener(p0, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyItemViewHolder {
        return MyItemViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: MyItemViewHolder, position: Int) {
        Glide.with(context)
            .load(list[position].mImageUrl)
            .into(holder.imageView!!)
        holder.name!!.text = StringBuilder().append(list[position].mName)
        holder.price!!.text = StringBuilder("$").append(list[position].mPrice)
        //holder.desc!!.text = StringBuilder().append(list[position].mDesc)
        holder.setClickListener(object: RecycleClickListener{
            override fun onItemClickListener(view: View?, position: Int) {
                addToCart(list[position])
            }
        })
    }

    private fun addToCart(item: Item){
        val userCart = FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")

        userCart.child(item.mKey!!)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val cartModel = snapshot.getValue(Cart::class.java)
                        val updateData: MutableMap<String, Any> = HashMap()
                        cartModel!!.quantity = cartModel!!.quantity + 1
                        updateData["quantity"] = cartModel!!.quantity
                        updateData["totalPrice"] = cartModel!!.quantity * cartModel.price!!.toFloat()
                        userCart.child(item.mKey!!)
                            .updateChildren(updateData)
                            .addOnSuccessListener{
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onLoadCartFailed("Added to cart")
                            }
                            .addOnFailureListener { e -> e.message?.let {
                                cartListener.onLoadCartFailed(
                                    it
                                )
                            } }
                    }else{
                        val cart = Cart()
                        cart.key = item.mKey
                        cart.name = item.mName
                        cart.image = item.mImageUrl
                        cart.price = item.mPrice.toString()
                        cart.quantity = 1
                        cart.totalPrice = item.mPrice!!.toFloat()

                        userCart.child(item.mKey!!)
                            .setValue(cart)
                            .addOnSuccessListener {
                                EventBus.getDefault().postSticky(UpdateCartEvent())
                                cartListener.onLoadCartFailed("Added to cart")
                            }
                            .addOnFailureListener { e -> e.message?.let {
                                cartListener.onLoadCartFailed(
                                    it
                                )
                            }}


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun getItemCount(): Int {
        return list.size
    }
}



