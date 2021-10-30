package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var productPrice:TextView
    private lateinit var productDescription:TextView
    private lateinit var productName:TextView
    private lateinit var productImage:ImageView
    private lateinit var buttonCart:Button
    private lateinit var buttonBuyNow:Button
    private lateinit var productId:String
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

        getProductDetails(productId)

    }

    private fun getProductDetails(productId: String) {

    }


}