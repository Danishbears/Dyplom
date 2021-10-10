package com.example.diplomich.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
    private lateinit var forgetPassword:TextView

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