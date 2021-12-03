package com.example.diplomich.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Ordered
import com.example.diplomich.ViewModel.Products
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NotificationAdapter(var context: Context,
                          var list:List<Ordered>): RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.notification_view,parent,false)
        return MyViewHolder(view)
    }



    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
            val products: Ordered = list[position]
            holder.productName.text = products.name
            holder.address.text = products.address
            holder.time.text = products.time

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById(R.id.product_name) as TextView
        val address = itemView.findViewById(R.id.address) as TextView
        val time = itemView.findViewById(R.id.ordered_time) as TextView

    }

    override fun getItemCount(): Int {
       return list.size
    }


}