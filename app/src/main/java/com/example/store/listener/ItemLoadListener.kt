package com.example.store.listener

import com.example.store.model.Item

interface ItemLoadListener {
    fun onItemLoadSuccess(itemModelList:List<Item>?)
    fun onItemLoadFailed(message:String?)
}