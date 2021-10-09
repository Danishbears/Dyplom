package com.example.diplomich.Authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import com.example.diplomich.R
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var editTextName:EditText
    private lateinit var editTextSurname:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var editTextPhoneNumber:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var fAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextName = findViewById(R.id.editName)
        editTextSurname = findViewById(R.id.editLastName)
        editTextEmail = findViewById(R.id.editEmailAddress)
        editTextPhoneNumber = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editPasswordAddress)
        progressBar = findViewById(R.id.progressBar)
        fAuth = FirebaseAuth.getInstance()

    }
}