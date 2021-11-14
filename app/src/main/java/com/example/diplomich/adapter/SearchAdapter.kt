package com.example.diplomich.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diplomich.ProductDetailsActivity
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.`interface`.ClickInteface
import com.google.firebase.firestore.FirebaseFirestore

class SearchAdapter(var context: Context,
                    var list:List<Products>,
                    var search:String): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    lateinit var onItemListener: ClickInteface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_search_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.MyViewHolder, position: Int) {
        if(search.isNotEmpty()){
            val results:MutableList<Products> = ArrayList()
            for(product:Products in list){
                if(product.name !=null && product.name!!.contains(search)){
                    results.add(product)
                }
            }
            MyAdapter(context,results)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val description = itemView.findViewById(R.id.product_description) as TextView
        val imageView = itemView.findViewById(R.id.image_product) as ImageView
        private lateinit var mDatabaseRef1: FirebaseFirestore
        val docRef = mDatabaseRef1.collection("products")

    }


}