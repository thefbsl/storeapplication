package com.example.store.model

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.database.Exclude
import com.squareup.picasso.Picasso

class Item {
    private var mName: String? = null
    private var mPrice: Int? = null
    private var mDesc: String? = null
    private var mImageUrl: String? = null
    private var mKey: String? = null

    constructor() {

    }

    constructor(name: String, price: Int, desc: String, imageUrl: String) {
        mName = name
        mPrice = price
        mDesc = desc
        mImageUrl = imageUrl
    }

    fun getName(): String? {
        return mName
    }

    fun setName(name: String) {
        mName = name
    }

    fun getPrice(): Int? {
        return mPrice
    }

    fun setPrice(price: Int) {
        mPrice = price
    }

    fun getDesc(): String? {
        return mDesc
    }

    fun setDesc(desc: String) {
        mDesc = desc
    }

    fun getImageUrl(): String? {
        return mImageUrl
    }

    fun setImageUrl(imageUrl: String) {
        mImageUrl = imageUrl
    }



}
