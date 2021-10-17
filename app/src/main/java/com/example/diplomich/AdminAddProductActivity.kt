package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class AdminAddProductActivity : AppCompatActivity() {
    private lateinit var categoryName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)
        categoryName = intent.extras!!.get("category").toString()
        Toast.makeText(this,categoryName,Toast.LENGTH_SHORT).show()

    }
}