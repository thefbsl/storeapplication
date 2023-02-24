package com.example.store

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.store.databinding.ActivityMainAdminBinding
import com.example.store.model.Item
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class MainActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminBinding
    private var mImageUri: Uri? = null
    private var mStorageRef = FirebaseStorage.getInstance()
    //private lateinit var mDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //mDatabaseRef = FirebaseDatabase.getInstance().getReference("items")

        binding.btnChooseImage.setOnClickListener(){
            openFileChooser()
        }

        binding.btnAddItem.setOnClickListener(){
            uploadFile()
        }

        binding.tvShowItems.setOnClickListener(){
            startActivity(Intent(this, ItemsActivity::class.java))
        }
    }

    private fun openFileChooser(){
        getResult.launch("image/*")
    }


    private val getResult = registerForActivityResult(
            ActivityResultContracts.GetContent(), ActivityResultCallback {
                binding.ivAdminItem.setImageURI(it)
                mImageUri = it
            }
        )

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    private fun uploadFile(){
        if (mImageUri != null) {

            mStorageRef.getReference("items").child(System.currentTimeMillis().toString())
                .putFile(mImageUri!!)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {

                            val item = Item(
                                binding.etItemName.text.toString().trim(),
                                binding.etPrice.text.toString().toInt(),
                                binding.etDesc.text.toString().trim(),
                                it.toString())

                            val mDatabaseRef = FirebaseDatabase.getInstance().getReference("items")
                            val itemId = mDatabaseRef.push().key
                            mDatabaseRef.child(itemId!!).setValue(item)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show()
                                }


                        }
                }

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }
}


//private fun uploadFile(){
//    if (mImageUri != null) {
//
//        val fileReference = mStorageRef.child(System.currentTimeMillis().toString())
//
//        mImageUri?.let {
//            fileReference.putFile(mImageUri!!)
//                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { task ->
//                    val item = Item(
//                        binding.etItemName.text.toString().trim(),
//                        binding.etPrice.text.toString().toInt(),
//                        task.metadata!!.reference!!.downloadUrl.toString())
//                    val itemId = mDatabaseRef.push().key
//                    mDatabaseRef.child(itemId!!).setValue(item)
//                    Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show()
//                })
//                .addOnFailureListener(OnFailureListener { e ->
//                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//                })
//        }
//
//    } else {
//        Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
//    }
//}


