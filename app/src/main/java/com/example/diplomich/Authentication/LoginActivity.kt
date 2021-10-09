package com.example.diplomich.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var loginMail:EditText
    private lateinit var loginPassword:EditText
    private lateinit var loginButton:Button
    private lateinit var fAuth:FirebaseAuth
    private lateinit var loginToRegText:TextView
    private lateinit var progressBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginMail = findViewById(R.id.editLoginEmailAddress)
        loginPassword = findViewById(R.id.editLoginPasswordAddress)
        loginButton = findViewById(R.id.LoginButton)
        fAuth = FirebaseAuth.getInstance()
        loginToRegText = findViewById(R.id.RegisterText)
        progressBar = findViewById(R.id.progressBarLogin)

        loginButton.setOnClickListener{
            performLogin()
        }

        loginToRegText.setOnClickListener{
            startActivity(Intent(applicationContext,Register::class.java))
        }

    }
    private fun performLogin(){
        var email:String = loginMail.text.toString().trim()
        var password:String = loginPassword.text.toString().trim()
        if(TextUtils.isEmpty(email)){
            loginMail.error = "Email is Required"
            return
        }
        if(TextUtils.isEmpty(password)){
            loginPassword.error = "Password is Required"
            return
        }
        if(password.length <6){
            loginPassword.error ="Minimum length for password is 6 "
            return
        }
//to check a special characters !!
        progressBar.visibility = View.VISIBLE

        //check user in database
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
            task->onCompleteLogin(task)
        }
    }
    private fun onCompleteLogin(task: Task<AuthResult>) {
        if(task.isSuccessful){
            Toast.makeText(this,"Login successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, MainActivity::class.java))

        }else{
            Toast.makeText(this," ERROR! " + task.exception!!.message, Toast.LENGTH_SHORT).show()
        }
    }
}