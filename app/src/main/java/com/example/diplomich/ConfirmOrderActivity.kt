package com.example.diplomich

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

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
            Check()
        }

    }

    private fun Check() {
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

        val orderMap: HashMap<String, Any> = hashMapOf(
            "id" to userId,
            "TotalPrice" to totalPrice.toString(),
            "date" to saveCurrentDate,
            "time" to saveCurrentTime,
            "phone" to editTextPhoneNumber.text.toString(),
            "name" to editTextName.text.toString(),
            "address" to editTextAddress.text.toString())


        var documentReference: DocumentReference = fStore.collection("Orders").document(userId)
        Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
        documentReference.set(orderMap).addOnSuccessListener {

            Toast.makeText(this,"Order added successfully", Toast.LENGTH_SHORT).show()
            clearCart()
            val intent = Intent(applicationContext,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
            .addOnFailureListener{
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT)
                    .show()
            }
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
}