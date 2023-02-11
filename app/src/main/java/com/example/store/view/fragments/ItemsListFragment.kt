package com.example.store.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.adapter.ItemAdapter
import com.example.store.databinding.FragmentItemsListBinding
import com.example.store.model.Item
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class ItemsListFragment : Fragment() {
    private lateinit var binding: FragmentItemsListBinding
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mStorage: FirebaseStorage
    private lateinit var items: ArrayList<Item>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentItemsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mStorage = FirebaseStorage.getInstance()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("items")
        binding.rvItemList.setHasFixedSize(true)
        binding.rvItemList.layoutManager = LinearLayoutManager(context)
        items = ArrayList()
        val adapter = ItemAdapter(items)
        binding.rvItemList.adapter = adapter

        mDatabaseRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                items.clear()
                if (snapshot.exists()) {
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






