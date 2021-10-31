package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var editTextName:EditText
    private lateinit var editTextSurname:EditText
    private lateinit var editTextPhoneNumber:EditText
    private lateinit var editTextButton: Button
    private lateinit var totalPrice:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        totalPrice = intent.getStringExtra("Total price").toString()
        Toast.makeText(this,"Total Price=${totalPrice.toString()}$",Toast.LENGTH_SHORT).show()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        editTextButton = findViewById(R.id.EditTextButton)
        editTextName = findViewById(R.id.EditTextName)
        editTextSurname = findViewById(R.id.EditTextSurName)
        editTextPhoneNumber = findViewById(R.id.EditTextPhoneNumber)

    }
}