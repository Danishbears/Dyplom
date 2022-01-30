package com.example.diplomich.Order

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class ConfirmOrderActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var editTextName:EditText
    private lateinit var editTextSurname:EditText
    private lateinit var editTextPhoneNumber:EditText
    private lateinit var editTextButton: Button
    private lateinit var editTextAddress:EditText
    private lateinit var totalPrice:String
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var userId:String
    private lateinit var orderMap: HashMap<String,Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)

        totalPrice = intent.getStringExtra("Total price").toString()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        editTextButton = findViewById(R.id.EditTextButton)
        editTextName = findViewById(R.id.EditTextName)
        editTextSurname = findViewById(R.id.EditTextSurName)
        editTextPhoneNumber = findViewById(R.id.EditTextPhoneNumber)
        editTextAddress = findViewById(R.id.EditTextAddress)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userId = fAuth.currentUser!!.uid
        val documentReference: DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }
            editTextName.hint = snapshot!!.getString("Name").toString()
            editTextSurname.hint = snapshot!!.getString("Surname").toString()
            editTextPhoneNumber.hint = snapshot!!.getString("PhoneNumber").toString()
        }

        editTextButton.setOnClickListener {
            checkInputInformation()
        }

    }

    private fun checkInputInformation() {
        if(TextUtils.isEmpty(editTextName.text.toString())){
            Toast.makeText(this," provide your Name",Toast.LENGTH_SHORT).show()

        }
        else if(TextUtils.isEmpty(editTextSurname.text.toString())){
            Toast.makeText(this," provide your Surname",Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(editTextPhoneNumber.text.toString()) && editTextPhoneNumber.length() <=8){
            Toast.makeText(this,"Not a valid phone number ",Toast.LENGTH_SHORT).show()
        }
        else{
            confirmOrder()
        }
    }

    private fun confirmOrder() {
        val saveCurrentDate:String
        val saveCurrentTime:String
        val calForData: Calendar = Calendar.getInstance()
        var currentData: SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentData.format(calForData.time)

        val currentTime: SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calForData.time)

        orderMap = hashMapOf(
            "id" to userId,
            "TotalPrice" to totalPrice.toString(),
            "date" to saveCurrentDate,
            "time" to saveCurrentTime,
            "phone" to editTextPhoneNumber.text.toString(),
            "name" to editTextName.text.toString(),
            "address" to editTextAddress.text.toString())

            intentToCardFormView()
    }

    private fun addToDataBase(orderMap:HashMap<String,Any>){
        var documentReference: DocumentReference = fStore.collection("Orders").document(userId)
        documentReference.set(orderMap).addOnSuccessListener {

            Toast.makeText(this,"Order added successfully", Toast.LENGTH_SHORT).show()
            clearCart()
            //intentToCardFormView()
        }
            .addOnFailureListener{
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun intentToCardFormView() {
        val intent = Intent(this,CardFormView::class.java)
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivityForResult(intent, 0)
        //finish()
    }

    private fun clearCart() {
        fStore.collection("CartList").document(userId)
            .collection("ProductId").document()
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "REMOVED", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(this,requestCode.toString(),Toast.LENGTH_SHORT).show()
        if(resultCode == 0){
            Toast.makeText(this,"Order has benn failed",Toast.LENGTH_SHORT).show()
        }
        if (resultCode == -1){
            addToDataBase(orderMap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}