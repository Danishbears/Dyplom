package com.example.diplomich.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.diplomich.Admin.AdminActivity
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var loginMail:EditText
    private lateinit var loginPassword:EditText
    private lateinit var loginButton:Button
    private lateinit var fAuth:FirebaseAuth
    private lateinit var loginToRegText:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var forgetPassword:TextView
    private lateinit var fStore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginMail = findViewById(R.id.editLoginEmailAddress)
        loginPassword = findViewById(R.id.editLoginPasswordAddress)
        loginButton = findViewById(R.id.LoginButton)
        fAuth = FirebaseAuth.getInstance()
        loginToRegText = findViewById(R.id.RegisterText)
        progressBar = findViewById(R.id.progressBarLogin)
        forgetPassword = findViewById(R.id.textForgetPassword)
        fStore = FirebaseFirestore.getInstance()
        //userId = fAuth.currentUser!!.uid


        loginButton.setOnClickListener{
            performLogin()
        }

        loginToRegText.setOnClickListener{
            startActivity(Intent(applicationContext,Register::class.java))
        }

        forgetPassword.setOnClickListener{
            resetPass(it)
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


        //check user in database
      /*  fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){task->onCompleteLogin(task)
            //checkUserLevel(it.user!!.uid)

        }.addOnFailureListener{
            Toast.makeText(this,"Smert'",Toast.LENGTH_SHORT).show()
        }*/
        fAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener{
           task ->
            task.user?.let {
                progressBar.visibility = View.VISIBLE
                checkUserLevel(it.uid) }
        }
            .addOnFailureListener{
                Toast.makeText(this,"Not valid information",Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkUserLevel(task: String) {
        var df:DocumentReference = fStore.collection("users").document(task)
        df.get().addOnSuccessListener(){ documentSnapshot ->
            if(documentSnapshot.getString("isAdmin")!=null){
                Toast.makeText(this,"Login successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, AdminActivity::class.java))
                finish()
            }
            if(documentSnapshot.getString("isUser")!=null){
                startActivity(Intent(applicationContext,MainActivity::class.java))
                finish()
            }
        }
    }


    private fun resetPass(view: View) {

        val passwordEditText = EditText(this)
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle(R.string.ResetDialog)
        builder.setMessage(R.string.ResetEmail)
        builder.setView(passwordEditText)
        builder.setPositiveButton(R.string.Yes){dialog,_ ->
            var mail:String = passwordEditText.text.toString()
            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                Toast.makeText(this,R.string.ResetLinkSent,Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
        }
        builder.setNegativeButton(R.string.No,null)
        builder.create().show()
    }
}