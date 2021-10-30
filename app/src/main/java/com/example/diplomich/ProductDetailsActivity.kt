package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.diplomich.ViewModel.Products
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productPrice:TextView
    private lateinit var productDescription:TextView
    private lateinit var productName:TextView
    private lateinit var productImage:ImageView
    private lateinit var buttonCart:Button
    private lateinit var buttonBuyNow:Button
    private lateinit var productId:String
    private lateinit var fStore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        productId = intent.getStringExtra("pid").toString()

        productPrice = findViewById(R.id.product_price_details)
        productName = findViewById(R.id.product_name_details)
        productDescription = findViewById(R.id.product_description_details)
        productImage = findViewById(R.id.product_image_details)
        buttonBuyNow = findViewById(R.id.buy_now_details)
        buttonCart = findViewById(R.id.add_to_cart_details)
        fStore = FirebaseFirestore.getInstance()
        Log.d("PIDGET",productId)
        getProductDetails(productId)

    }

    private fun getProductDetails(productId: String) {
        val documentReference: DocumentReference = fStore.collection("products").document(productId)
        documentReference.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }
            productName.text = snapshot!!.getString("name")
            productDescription.text = snapshot.getString("description")
            productPrice.text = snapshot!!.getString("price")
            Glide.with(this)
                .load(snapshot.getString("image")!!.toUri())
                .into(productImage)
        }
    }


}