package com.codinginflow.searchviewexample

import android.content.Intent
import android.util.Log
import com.example.diplomich.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diplomich.ProductDetailsActivity
import com.example.diplomich.ViewModel.Products
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.popular_item.view.*


class FindItem(exampleList: MutableList<Products>) :
    RecyclerView.Adapter<FindItem.ExampleViewHolder>(),
    Filterable {
    private val exampleList: List<Products>
    private val exampleListFull: List<Products>

     class ExampleViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView1: TextView
        var textView2: TextView
        lateinit var db: StorageReference
        init {
           imageView = itemView.findViewById(R.id.image_product)
            textView1 = itemView.findViewById(R.id.product_name)
            textView2 = itemView.findViewById(R.id.product_description)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.popular_item,
            parent, false
        )
        return ExampleViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem: Products = exampleList[position]
        //holder.imageView.setImageResource(currentItem.image!!.toInt())
        holder.textView1.text = currentItem.name
        holder.textView2.text = currentItem.price


        holder.db = FirebaseStorage.getInstance().getReference("Product Images/")
        holder.db.child("${currentItem.pid}.jpg").downloadUrl.addOnCompleteListener{task->
            currentItem.image = task.result.toString()
            Glide.with(holder.imageView.context)
                .load(currentItem.image)
                .into(holder.imageView.image_product)
        }
            .addOnFailureListener{
                Log.d("FailedCompleteListener",it.message.toString())
            }
        holder.imageView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProductDetailsActivity::class.java)
            intent.putExtra("pid", currentItem.pid)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return exampleList.size
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    init {
        this.exampleList = exampleList
        exampleListFull = ArrayList<Products>(exampleList)
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<Products> = ArrayList<Products>()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(exampleListFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in exampleListFull) {
                    if (item.name!!.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            exampleList.clear()
            exampleList.addAll(results.values as List<Products>)
            notifyDataSetChanged()
        }
    }


}

