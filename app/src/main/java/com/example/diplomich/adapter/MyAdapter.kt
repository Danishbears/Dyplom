package com.example.diplomich.adapter

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.example.diplomich.HomeFragment
import com.example.diplomich.MainActivity
import com.example.diplomich.ProductDetailsActivity
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.`interface`.ClickInteface
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
class MyAdapter(var context:Context,
                var list:List<Products>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    lateinit var onItemListener:ClickInteface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val products:Products = list[position]
        holder.productName.text = products.name
        holder.productPrice.text = products.price
        holder.description.text = products.description
        holder.imageView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ProductDetailsActivity::class.java)
            //  intent.putExtra("pid",products.pid)
            Log.d("Uniete",context.packageResourcePath)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.product_name) as TextView
        val productPrice = itemView.findViewById(R.id.product_price) as TextView
        val description = itemView.findViewById(R.id.product_description) as TextView
        val imageView = itemView.findViewById(R.id.image_product) as ImageView


    }


}