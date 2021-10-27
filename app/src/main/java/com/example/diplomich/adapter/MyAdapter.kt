package com.example.diplomich.adapter

import android.content.ClipDescription
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Products
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MyAdapter(var context:Context,
                var list:List<Products>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val products:Products = list[position]
        holder.productName.text = products.name
        holder.productPrice.text = products.price
        holder.description.text = products.description
        Glide.with(context)
            .load(holder.imgRef)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById(R.id.product_name) as TextView
        val productPrice = itemView.findViewById(R.id.product_price) as TextView
        val description = itemView.findViewById(R.id.product_description) as TextView
        val imageView = itemView.findViewById(R.id.image_product) as ImageView
        var imageStorage = FirebaseStorage.getInstance().reference
        val imgRef = imageStorage.child("image/*" + ".jpg")
    }

}