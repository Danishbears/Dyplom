package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AdminAddProductActivity : AppCompatActivity() {
    private lateinit var categoryName:String
    private lateinit var addNewProductButt: Button
    private lateinit var inputProdName:EditText
    private lateinit var inputProdDescription:EditText
    private lateinit var inputProdPrice:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)
        categoryName = intent.extras!!.get("category").toString()
        Toast.makeText(this,categoryName,Toast.LENGTH_SHORT).show()

    }
}