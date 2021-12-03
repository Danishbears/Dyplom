package com.example.diplomich.adapter

import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.Admin.AdminViewUserProducts
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.example.diplomich.ViewModel.UserOrders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
                                    addToAnotherDb(products.id,holder)
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

        holder.button.setOnClickListener {
            val intent = Intent(activity,AdminViewUserProducts::class.java)
            intent.putExtra("uid",products.id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.itemView.context.startActivity(intent)
        }

    }

    private fun addToAnotherDb(id: String?, holder: OrdersAdapter.MyViewHolder) {
        val saveCurrentDate:String
        val saveCurrentTime:String
        val calForData: Calendar = Calendar.getInstance()
        var currentData: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentData.format(calForData.time)

        val currentTime: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calForData.time)
        val orderMap: HashMap<String, Any> = hashMapOf(
            "id" to id.toString(),
            "TotalPrice" to holder.productPrice.toString(),
            "date" to saveCurrentDate,
            "time" to  saveCurrentTime,
            "phone" to holder.phone.text.toString(),
            "name" to holder.productName.text.toString(),
            "address" to holder.address.text.toString(),
        "isChecked" to 1 )

        var documentReference: DocumentReference = holder.db.collection("Maded").document(id.toString()).collection("ToUser").document(saveCurrentTime)
        documentReference.set(orderMap).addOnSuccessListener {

            Toast.makeText(context,"Order added successfully", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Toast.makeText(context,"${it.message}",Toast.LENGTH_SHORT)
                    .show()
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
        val button = itemView.findViewById(R.id.check_user_prod_btn) as Button
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userId:String = fAuth.currentUser!!.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    }
}