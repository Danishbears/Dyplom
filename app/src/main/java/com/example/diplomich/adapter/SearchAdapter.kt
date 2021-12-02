package com.example.diplomich.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Products
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class SearchAdapter(var options: FirestoreRecyclerOptions<Products>):
    FirestoreRecyclerAdapter<Products, SearchAdapter.MyViewHolder>(options) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.popular_item,parent,false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int, product: Products) {
            //val products:Products = list[position]
            holder.productName.text = product.name
            holder.productPrice.text = product.price
            holder.description.text = product.description

            /*Glide.with(holder.imageView.context)
                .load(product.image)
                .into(holder.imageView)*/

        Glide.with(holder.imageView.context)
            .load("https://firebasestorage.googleapis.com/v0/b/dyplom-867af.appspot.com/o/Product%20Images%2Fimage%3A24Oct%2029%2C%20202114%3A11%3A02.jpg?alt=media&token=ed745a9f-2a68-47fe-a217-27efb6328609")
            .into(holder.imageView)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById(R.id.product_name) as TextView
        val productPrice = itemView.findViewById(R.id.product_price) as TextView
        val description = itemView.findViewById(R.id.product_description) as TextView
        val imageView = itemView.findViewById(R.id.image_product) as ImageView

    }




}