package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.GridLayoutManager
import com.example.store.adapter.MyItemAdapter
import com.example.store.databinding.ActivityMainBinding
import com.example.store.eventbus.UpdateCartEvent
import com.example.store.listener.CartLoadListener
import com.example.store.listener.ItemLoadListener
import com.example.store.model.Cart
import com.example.store.model.Item
import com.example.store.utils.SpaceItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), ItemLoadListener, CartLoadListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemLoadListener: ItemLoadListener
    private lateinit var cartLoadListener: CartLoadListener
    private lateinit var actionBar:ActionBar;



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
    fun onUpdateCartEvent(event:UpdateCartEvent){
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        verifyUserLoggedIn()
        init()
        loadItemFromDatabase()
        countCartFromFirebase()
        binding.bottomNavigation.selectedItemId = R.id.ic_home

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.ic_profile -> startActivity(Intent(this, UserProfileActivity::class.java))
                R.id.ic_home -> startActivity(Intent(this, MainActivity::class.java))
                R.id.ic_about_us -> startActivity(Intent(this, AboutUs::class.java))
            }
            true
        }
    }

    private fun countCartFromFirebase(){
        val cartModels: MutableList<Cart> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("UNIQUE_USER_ID")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(cartSnapshot in snapshot.children){
                        val cartModel = cartSnapshot.getValue(Cart::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }

            })
    }

    private fun loadItemFromDatabase(){
        val itemModels : MutableList<Item> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("items")
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(itemSnapshot in snapshot.children){
                            val itemModel = itemSnapshot.getValue(Item::class.java)
                            itemModel!!.mKey = itemSnapshot.key
                            itemModels.add(itemModel)
                        }
                        itemLoadListener.onItemLoadSuccess(itemModels)
                    }else{
                        itemLoadListener.onItemLoadFailed("Items not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun init(){
        itemLoadListener = this
        cartLoadListener = this
        val gridLayoutManager = GridLayoutManager(this, 2)
        binding.recycleItem.layoutManager = gridLayoutManager
        binding.recycleItem.addItemDecoration(SpaceItemDecoration())
        binding.btnCart.setOnClickListener{
            startActivity(Intent(this@MainActivity, CartActivity::class.java))
        }

    }

    override fun onItemLoadSuccess(itemModelList: List<Item>?) {
        val adapter = MyItemAdapter(this, itemModelList!!, cartLoadListener)
        binding.recycleItem.adapter = adapter
    }

    override fun onItemLoadFailed(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<Cart>) {
        var cartSum = 0
        for(cartModel in cartModelList) cartSum += cartModel.quantity
        binding.badge.setNumber(cartSum)
    }

    override fun onLoadCartFailed(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun verifyUserLoggedIn(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}