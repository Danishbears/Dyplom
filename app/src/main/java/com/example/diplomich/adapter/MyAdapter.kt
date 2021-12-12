package com.example.diplomich.adapter

import android.content.Context
import android.content.Intent
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideExperiments
import com.example.diplomich.ProductDetailsActivity
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.`interface`.ClickInteface
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.popular_item.view.*


class MyAdapter(var context:Context,
                var list:List<Products>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    lateinit var onItemListener:ClickInteface
    var items = arrayListOf<String>()
    var itemsName = ArrayMap<String,String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.popular_item,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val products:Products = list[position]
        holder.productName.text = products.name
        holder.productPrice.text = products.price
        holder.description.text = products.description

        holder.db = FirebaseStorage.getInstance().getReference("Product Images/")
        holder.db.child("${products.pid}.jpg").downloadUrl.addOnCompleteListener{task->
            products.image = task.result.toString()
            Glide.with(context)
                .load(products.image)
                .into(holder.imageView.image_product)
        }
            .addOnFailureListener{
                Log.d("FailedCompleteListener",it.message.toString())
            }


        holder.imageView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ProductDetailsActivity::class.java)
            intent.putExtra("pid",products.pid)
            Log.d("Uniete",context.packageResourcePath)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun checkTableItems(imageItem: String, name: String, pid: String?){
        var sub = ".jpg"
            itemsName[(name.substring(5, name.indexOf(".jpg")))] = imageItem
        Log.d("CheckItems",(name.substring(5,name.indexOf(".jpg"))))

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName = itemView.findViewById(R.id.product_name) as TextView
        val productPrice = itemView.findViewById(R.id.product_price) as TextView
        val description = itemView.findViewById(R.id.product_description) as TextView
        val imageView = itemView.findViewById(R.id.image_product) as ImageView
        lateinit var db:StorageReference

    }


}