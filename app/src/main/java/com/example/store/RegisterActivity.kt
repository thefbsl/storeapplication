package com.example.store

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.store.databinding.ActivityRegisterBinding
import com.example.store.model.Cart
import com.example.store.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.tvLogin.setOnClickListener(){
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnLogin.setOnClickListener(){
            val username = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                Toast.makeText(this, "Fill the gaps", Toast.LENGTH_SHORT).show()
            else if (password != confirmPassword)
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            else if (username == "Admin" && email == "admin@admin.com" && password == "admin123456")
                singUpAdmin(email, password)
            else singUp(username, email, password)


        }
    }

    private fun singUp(username: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(username, email, auth.currentUser?.uid!!)
                    startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(username: String, email: String, uid: String) {
        mRef = FirebaseDatabase.getInstance().getReference()
        mRef.child("users").child(uid).setValue(User(username, email, uid))
    }

    private fun singUpAdmin(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this@RegisterActivity, MainActivityAdmin::class.java))
                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
    }
}