package com.example.diplomich.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.R
import com.example.diplomich.ViewModel.UserOrders
import com.google.firebase.auth.FirebaseAuth

class OrdersAdapter(var context: Context,
                    var list:List<UserOrders>): RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.orders_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersAdapter.MyViewHolder, position: Int) {
        val products: UserOrders = list[position]
        holder.productName.text = products.name
        holder.productPrice.text = products.totalPrice
        holder.address.text = products.address
        holder.phone.text = products.phone

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.user_name) as TextView
        val productPrice = itemView.findViewById(R.id.order_price) as TextView
        val phone = itemView.findViewById(R.id.user_phone) as TextView
        val address = itemView.findViewById(R.id.order_address) as TextView
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userId:String = fAuth.currentUser!!.uid

    }
}