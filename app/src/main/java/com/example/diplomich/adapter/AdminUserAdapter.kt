package com.example.diplomich.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminUserAdapter(var context: Context,
                       var list: List<Cart>,
                       var activity: FragmentActivity?
):RecyclerView.Adapter<AdminUserAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUserAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.admin_user_view,parent,false)
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


    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.name_admin_cart) as TextView
        val productPrice = itemView.findViewById(R.id.price_admin_cart) as TextView
        val description = itemView.findViewById(R.id.description_admin_cart) as TextView
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userId:String = fAuth.currentUser!!.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    }



}