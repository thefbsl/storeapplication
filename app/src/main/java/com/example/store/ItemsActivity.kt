package com.example.store

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.adapter.ItemAdminAdapter
import com.example.store.databinding.ActivityItemsBinding
import com.example.store.model.Item
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemsBinding
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mStorage: FirebaseStorage
    private lateinit var items: ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mStorage = FirebaseStorage.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items")
        binding.rvItemsAdmin.setHasFixedSize(true)
        binding.rvItemsAdmin.layoutManager = LinearLayoutManager(this)
        items = ArrayList()
        val adapter = ItemAdminAdapter(this@ItemsActivity, items)
        binding.rvItemsAdmin.adapter = adapter

        mDatabaseRef.addValueEventListener(object: ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                if(snapshot.exists()) {
                    for (postSnapshot in snapshot.children) {
                        val item = postSnapshot.getValue(Item::class.java)
                        items.add(item!!)
                    }
                    adapter.notifyDataSetChanged()
                    //binding.rvItemsAdmin.adapter = ItemAdminAdapter(this@ItemsActivity, items)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


    }

    }