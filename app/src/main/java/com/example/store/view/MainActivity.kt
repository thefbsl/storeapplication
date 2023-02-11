package com.example.store.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.store.R
import com.example.store.databinding.ActivityMainBinding
import com.example.store.view.fragments.BasketFragment
import com.example.store.view.fragments.FavouritesFragment
import com.example.store.view.fragments.ItemsListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ItemsListFragment())

        binding.bottomNavBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.items -> replaceFragment(ItemsListFragment())
                R.id.favourites -> replaceFragment(FavouritesFragment())
                R.id.basket -> replaceFragment(BasketFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFrameLayOut, fragment)
        fragmentTransaction.commit()
    }
}