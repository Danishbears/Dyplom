package com.example.diplomich.ViewModel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.R
import com.example.diplomich.`interface`.ClickInteface
import org.w3c.dom.Text



class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtProductName: TextView
    var txtProductDescription: TextView
    var txtProductPrice: TextView
    var imageView: ImageView
    var listner: ClickInteface? = null

    fun setItemClickListner(listner: ClickInteface?) {
        this.listner = listner
    }

    override fun onClick(view: View) {
        listner?.onClick(view, adapterPosition, false)
    }

    init {
        imageView = itemView.findViewById<View>(R.id.image_product) as ImageView
        txtProductName = itemView.findViewById<View>(R.id.product_name) as TextView
        txtProductDescription = itemView.findViewById<View>(R.id.product_description) as TextView
        txtProductPrice = itemView.findViewById<View>(R.id.product_price) as TextView
    }
}