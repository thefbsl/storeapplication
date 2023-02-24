package com.example.store

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.store.databinding.ActivityUserProfileBinding
import com.example.store.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentUser = User()

        FirebaseDatabase.getInstance()
            .getReference("users")
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        currentUser.username = snapshot.child("username").toString()
                        currentUser.email = snapshot.child("email").toString()
                    }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        binding.tvName.text = "Username: " + currentUser.username
        binding.tvEmail.text = "Email: " + currentUser.email
        binding.tvCash.text = "Current cash " + (10_000..100_000).random().toString()
        binding.btnLogOut.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}