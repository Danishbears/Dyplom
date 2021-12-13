package com.example.diplomich.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.diplomich.CartFragment
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.ViewModel.Ordered
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.popular_item.view.*
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
       // holder.productPrice.text = productsCart.price
        holder.description.text = productsCart.description


        holder.elegantbutton.setOnValueChangeListener { view, oldVal, newVal ->
               // holder.productPrice =
                val num: String = holder.elegantbutton.number
                Log.d("Bra",holder.elegantbutton.number)
                val str = productsCart.price!!.toInt()
            Log.d("PriceS",str.toString())
                val result = num.toInt() * str!!.toInt()
                Log.d("Result",result.toString())
                holder.productPrice.text = result.toString()
                holder.db.collection("CartList").document(holder.userId)
                    .collection("ProductId").document(productsCart.pid!!)
                    .update("Currentprice", result.toString())

                productsCart.Currentprice = result.toString()
            Log.d("CurrentPrice",productsCart.Currentprice.toString())

                productsCart.count = num.toString()
               // productsCart.price = result.toString()
                holder.db.collection("CartList").document(holder.userId)
                    .collection("ProductId").document(productsCart.pid!!)
                    .update("count", num.toString())
        }

        holder.elegantbutton.number = productsCart.count

        val num: String = holder.elegantbutton.number
        Log.d("Bra",holder.elegantbutton.number)
        val str = productsCart.price!!.toInt()
        val result = num.toInt() * str!!.toInt()
        holder.productPrice.text = result.toString()

        /*holder.db.collection("CartList").document(holder.userId).collection("ProductId").
           get().addOnSuccessListener {documentSnapshot->
               val city = documentSnapshot.toObjects<Cart>()
               for (eachIndex in city.indices) {
                   if (eachIndex != null) {
                       holder.elegantbutton.number =  city[eachIndex].count.toString()
                   }
               }

           }*/

       // holder.elegantbutton.number


        holder.bd = FirebaseStorage.getInstance().getReference("Product Images/")
        holder.bd.child("${productsCart.pid}.jpg").downloadUrl.addOnCompleteListener{task->
            productsCart.image = task.result.toString()
            Glide.with(context)
                .load(productsCart.image)
                .into(holder.imageView)
        }
            .addOnFailureListener{
                Log.d("FailedCompleteListener",it.message.toString())
            }

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


    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.name_cart) as TextView
        val productPrice = itemView.findViewById(R.id.price_cart) as TextView
        val description = itemView.findViewById(R.id.description_cart) as TextView
        val imageView = itemView.findViewById(R.id.image_cart) as ImageView
        val elegantbutton = itemView.findViewById(R.id.number_count_cart) as ElegantNumberButton
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var CountMc:Int = 0
        lateinit var bd: StorageReference
        val fAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var userId:String = fAuth.currentUser!!.uid
    }

}