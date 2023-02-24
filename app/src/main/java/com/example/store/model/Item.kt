package com.example.store.model

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.database.Exclude
import com.squareup.picasso.Picasso

class Item {
    var mName: String? = null
    var mPrice: Int? = null
    var mDesc: String? = null
    var mImageUrl: String? = null
    var mKey: String? = null

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

    fun getKey(): String? {
        return mKey
    }

    fun setKey(key: String) {
        mKey = key
    }



}
