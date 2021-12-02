package com.example.diplomich.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.diplomich.CartFragment
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter(
    var context: Context,
    var list: List<Cart>,
    var activity: FragmentActivity?
):RecyclerView.Adapter<CartAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val productsCart: Cart = list[position]
        //holder.elegantbutton.number = productsCart.count!!.toInt().toString()
        holder.productName.text = productsCart.name
        holder.productPrice.text = productsCart.price
        holder.description.text = productsCart.description

        holder.itemView.setOnClickListener{
            val option = arrayOf(
                //R.string.RemoveFromCart.toString(), R.string.Change.toString()
           activity!!.resources.getString(R.string.RemoveFromCart), activity!!.resources.getString(R.string.Change)
            )


            val builder = AlertDialog.Builder(activity)
            .setTitle("Card options")
            .setItems(option) { _, listener ->
                when(listener){
                0->GlobalScope.launch {
                    holder.db.collection("CartList").document(holder.userId)
                        .collection("ProductId").document(productsCart.pid!!)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "REMOVED", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                        }
                }
                    1->Toast.makeText(context,"Adada",Toast.LENGTH_LONG).show()
            }
            }
            builder.create()
            builder.show()
        }
        Glide.with(context)
            .load("https://firebasestorage.googleapis.com/v0/b/dyplom-867af.appspot.com/o/Product%20Images%2Fimage%3A24Oct%2029%2C%20202114%3A11%3A02.jpg?alt=media&token=ed745a9f-2a68-47fe-a217-27efb6328609")
            .into(holder.imageView)

    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.name_cart) as TextView
        val productPrice = itemView.findViewById(R.id.price_cart) as TextView
        val description = itemView.findViewById(R.id.description_cart) as TextView
        val imageView = itemView.findViewById(R.id.image_cart) as ImageView
        val elegantbutton = itemView.findViewById(R.id.number_count_cart) as ElegantNumberButton
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userId:String = fAuth.currentUser!!.uid
    }

}