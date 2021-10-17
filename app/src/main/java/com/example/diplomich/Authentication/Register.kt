package com.example.diplomich.Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var editTextName:EditText
    private lateinit var editTextSurname:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var editTextPhoneNumber:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var progressBar:ProgressBar
    private lateinit var fAuth:FirebaseAuth
    private lateinit var fUser:FirebaseUser
    private lateinit var registerButton: Button
    private lateinit var textToLogin:TextView
    private lateinit var fStore:FirebaseFirestore
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextName = findViewById(R.id.editName)
        editTextSurname = findViewById(R.id.editLastName)
        editTextEmail = findViewById(R.id.editEmailAddress)
        editTextPhoneNumber = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editPasswordAddress)
        progressBar = findViewById(R.id.progressBar)
        textToLogin = findViewById(R.id.textToLogin)
        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()
        registerButton = findViewById(R.id.RegistrationButton)

        currentUser()

        registerButton.setOnClickListener{
            performAuth()
        }
        textToLogin.setOnClickListener{
            startActivity(Intent(applicationContext,LoginActivity::class.java))
        }
    }

    private fun performAuth(){
       val email:String = editTextEmail.text.toString().trim()
       val password:String = editTextPassword.text.toString().trim()
       val Name:String = editTextName.text.toString().trim()
       val Surname:String = editTextSurname.text.toString().trim()
       val phoneNumber:String = editTextPhoneNumber.text.toString().trim()

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

        fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this) {task->
                onComplete(task,Name,Surname,phoneNumber,email)
                buildCategories(Name,Surname,phoneNumber,email)
            }

    }

    private fun onComplete(
        task: Task<AuthResult>,
        Name: String,
        Surname: String,
        phoneNumber: String,
        email: String
    ) {
        if(task.isSuccessful){

            //send verification link
            verificationEmail()

            Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show()
            userId = fAuth.currentUser!!.uid
            var documentReference:DocumentReference = fStore.collection("users").document(userId)

            var user:HashMap<String,Any?> = buildCategories(Name,Surname,phoneNumber,email)

            documentReference.set(user).addOnSuccessListener {
                Log.d("TAG", "onSuccess: user profile is created for$userId")
            }
            startActivity(Intent(applicationContext,MainActivity::class.java))

        }else{
            Toast.makeText(this," ERROR! " + task.exception!!.message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun currentUser(){
        if(fAuth.currentUser != null){
            startActivity(Intent(applicationContext,MainActivity::class.java))
            finish()
        }
    }

    private fun buildCategories(Name: String,
                                Surname: String,
                                phoneNumber: String,
                                email: String):HashMap<String,Any?>{
        return hashMapOf(
           "Name" to Name,
           "Surname" to Surname,
           "Email" to email,
           "PhoneNumber" to phoneNumber,
            "isUser" to "1"
        )
    }

    private fun verificationEmail(){
        val user:FirebaseUser = fAuth.currentUser!!
        user.sendEmailVerification().addOnSuccessListener {
            Toast.makeText(this,R.string.VerificationEmail,Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                Log.d("TAG","On Failure ${it.message}")
            }
    }

}