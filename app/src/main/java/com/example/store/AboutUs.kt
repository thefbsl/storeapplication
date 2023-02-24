package com.example.store

import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.os.PersistableBundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.store.adapter.AboutCartAdapter
import com.example.store.adapter.MyCartAdapter
import com.example.store.databinding.AboutUsBinding
import com.example.store.databinding.ActivityMainBinding
import com.example.store.databinding.ActivityUserProfileBinding


class AboutUs(var title: String, var description: String, var image:Int):AppCompatActivity(){
    private lateinit var actionBar: ActionBar
    private lateinit var AboutUsList:ArrayList<AboutUs>
    private lateinit var myAdapter:AboutCartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_us)

        actionBar = this.supportActionBar!!
        loadCards()
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val title: AboutUsList[position].title;
                actionBar.title = title

            }

            override fun onPageSelected(position: Int) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun loadCards(){
        AboutUsList = ArrayList()

        AboutUsList.add(
            AboutUs(
                "Store 1",
            "description",
                R.drawable.sports_png_pic)
        )
        AboutUsList.add(
            AboutUs(
                "Store 2",
                "description",
                R.drawable.sports_png_pic)
        )
        AboutUsList.add(
            AboutUs(
                "Store 3",
                "description",
                R.drawable.sports_png_pic)
        )
        myAdapter = AboutCartAdapter(this,AboutUsList)
        viewPager.adapter = AboutCartAdapter
        viewPager.setPadding(100,0,100,0)
    }

}