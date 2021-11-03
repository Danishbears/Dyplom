package com.example.diplomich.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.Admin.AdminNewOrderActivity
import com.example.diplomich.R
import com.example.diplomich.ViewModel.UserOrders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OrdersAdapter(
    var context: Context,
    var list: List<UserOrders>,
    var activity: Activity?
): RecyclerView.Adapter<OrdersAdapter.MyViewHolder>() {

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
        holder.itemView.setOnClickListener {
            val option = arrayOf(
                //R.string.RemoveFromCart.toString(), R.string.Change.toString()
                activity!!.resources.getString(R.string.Yes), activity!!.resources.getString(R.string.No)
            )


            Log.d("HOW",products.id.toString())

            val builder = AlertDialog.Builder(activity)
                .setTitle("Are u sure?")
                .setItems(option) { _, listener ->
                    when(listener){
                        0-> GlobalScope.launch {
                            holder.db.collection("Orders").document(products.id.toString())
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "REMOVED", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                        1-> Toast.makeText(context,"Adada", Toast.LENGTH_LONG).show()
                    }
                }
            builder.create()
            builder.show()
        }
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
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    }
}