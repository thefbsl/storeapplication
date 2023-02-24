package com.example.store.listener

import com.example.store.model.Cart

interface CartLoadListener {
    fun onLoadCartSuccess(cartModelList: List<Cart>)
    fun onLoadCartFailed(message:String)
}