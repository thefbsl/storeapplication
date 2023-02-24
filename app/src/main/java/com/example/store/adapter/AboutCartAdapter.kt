package com.example.store.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import com.example.store.AboutUs
import com.example.store.R


class AboutCartAdapter(private val context: Context, private val myModelArrayList: ArrayList<AboutUs>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return myModelArrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.about_us, container, false)
        val model = myModelArrayList[position]
        container.addView(view, position)
        val title = model.title
        val description = model.description
        val image = model.image


        view.bannerTv






        view.setOnClickListener{
            Toast.makeText(context, "$title \n %date", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}

