package com.example.diplomich

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.example.diplomich.ViewModel.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productPrice:TextView
    private lateinit var productDescription:TextView
    private lateinit var productName:TextView
    private lateinit var productImage:ImageView
    private lateinit var buttonCart:Button
    private lateinit var buttonBuyNow:Button
    private lateinit var productId:String
    private lateinit var fAuth: FirebaseAuth
    private lateinit var userId:String
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
        fAuth = FirebaseAuth.getInstance()
        userId = fAuth.currentUser!!.uid
        fStore = FirebaseFirestore.getInstance()
        getProductDetails(productId)
        buttonCart.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        val saveCurrentDate:String
        val saveCurrentTime:String
        val calForData:Calendar = Calendar.getInstance()
        var currentData:SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentData.format(calForData.time)

        val currentTime:SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calForData.time)

        val cartMap:HashMap<String,Any> = hashMapOf(
            "pid" to productId,
            "date" to saveCurrentDate,
            "time" to saveCurrentTime,
            "description" to productDescription.text.toString(),
            "price" to productPrice.text.toString(),
            "discount" to "",
            "name" to productName.text.toString(),
            "count" to "1")

        var documentReference: DocumentReference = fStore.collection("CartList").document(userId)
            .collection("ProductId").document(productId)
        documentReference.set(cartMap).addOnSuccessListener {

            Toast.makeText(this,"Product added successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext,MainActivity::class.java))
        }
            .addOnFailureListener{

            }

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
                .load("https://firebasestorage.googleapis.com/v0/b/dyplom-867af.appspot.com/o/Product%20Images%2Fimage%3A24Oct%2029%2C%20202114%3A11%3A02.jpg?alt=media&token=ed745a9f-2a68-47fe-a217-27efb6328609")
                .into(productImage)
        }
    }


}