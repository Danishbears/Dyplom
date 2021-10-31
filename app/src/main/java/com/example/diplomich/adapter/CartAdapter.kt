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
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.ViewModel.Products

class CartAdapter(var context: Context,
                  var list:List<Cart>):RecyclerView.Adapter<CartAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val productsCart: Cart = list[position]
        holder.productName.text = productsCart.name
        holder.productPrice.text = productsCart.price
        holder.description.text = productsCart.description
        Glide.with(context)
            .load("https://firebasestorage.googleapis.com/v0/b/dyplom-867af.appspot.com/o/Product%20Images%2Fimage%3A24Oct%2029%2C%20202114%3A11%3A02.jpg?alt=media&token=ed745a9f-2a68-47fe-a217-27efb6328609")
            .into(holder.imageView)

    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.name_cart) as TextView
        val productPrice = itemView.findViewById(R.id.price_cart) as TextView
        val description = itemView.findViewById(R.id.description_cart) as TextView
        val imageView = itemView.findViewById(R.id.image_cart) as ImageView
    }

}