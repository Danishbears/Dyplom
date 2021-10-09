package com.example.diplomich.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.diplomich.HomeFragment
import com.example.diplomich.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var editTextName:EditText
    private lateinit var editTextSurname:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var editTextPhoneNumber:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var fAuth:FirebaseAuth
    private lateinit var registerButton: Button

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
        registerButton = findViewById(R.id.RegistrationButton)
        registerButton.setOnClickListener{
            performAuth()
        }

    }
    private fun performAuth(){
       var email:String = editTextEmail.toString().trim()
       var password:String = editTextPassword.toString().trim()
        if(TextUtils.isEmpty(email)){
            editTextEmail.error = "Email is Required"
            return
        }
        if(TextUtils.isEmpty(password)){
            editTextPassword.error = "Password is Required"
            return
        }
        if(password.length <6){
            editTextPassword.error ="Minimum length for password is 6 "
            return
        }
//to check a special characters !!
        progressBar.visibility = View.VISIBLE

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
            OnCompleteListener {
                onComplete(it)
            })

    }
    private fun onComplete(task: Task<AuthResult>) {
        if(task.isSuccessful){
            Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext,HomeFragment::class.java))

        }else{
            Toast.makeText(this," ERROR! " + task.exception!!.message,Toast.LENGTH_SHORT).show()
        }
    }
}