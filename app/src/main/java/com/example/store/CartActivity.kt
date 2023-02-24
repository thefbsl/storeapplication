package com.example.store

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.adapter.MyCartAdapter
import com.example.store.databinding.ActivityCartBinding
import com.example.store.eventbus.UpdateCartEvent
import com.example.store.listener.CartLoadListener
import com.example.store.model.Cart
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class CartActivity : AppCompatActivity(), CartLoadListener {
    private lateinit var binding: ActivityCartBinding
    var cartLoadListener: CartLoadListener? = null

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if(EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java)){
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        }
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent){
        loadCartFromDatabase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        loadCartFromDatabase()
    }

    private fun init(){
        cartLoadListener = this
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.layoutManager = layoutManager
        binding.recyclerCart.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun loadCartFromDatabase() {
        val cartModels: MutableList<Cart> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(Cart::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener!!.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener!!.onLoadCartFailed(error.message)
                }

            })
    }

    override fun onLoadCartSuccess(cartModelList: List<Cart>) {
        var sum = 0.0
        for(cartModel in cartModelList){
            sum += cartModel.totalPrice
        }
        binding.txtTotal.text = StringBuilder("$").append(sum)
        val adapter = MyCartAdapter(this, cartModelList)
        binding.recyclerCart.adapter = adapter
    }

    override fun onLoadCartFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}