package com.example.diplomich.Order

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.cardform.view.CardForm
import kotlinx.android.synthetic.main.fragment_card_form.*
import android.text.InputType
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.example.diplomich.MainActivity
import com.example.diplomich.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay


class CardFormView :AppCompatActivity()
 {

    private lateinit var cardForm:CardForm
    private lateinit var buttonBuyProduct:Button
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var userId:String

    override fun onCreate(
        savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_card_form)
        // Inflate the layout for this fragment

        cardForm = findViewById<View>(R.id.card_form) as CardForm
        buttonBuyProduct = findViewById(R.id.btnBuy)
        setUpCardViewForm()
        buttonBuyProduct.setOnClickListener {
            if(cardForm.isValid){
                alertDialog()
                insertCardDataToDatabase()
            }else{
                Toast.makeText(this,"Please complete the form",Toast.LENGTH_SHORT).show()
            }
        }
    }

     private fun insertCardDataToDatabase() {
         fAuth = FirebaseAuth.getInstance()
         fStore = FirebaseFirestore.getInstance()
         userId = fAuth.currentUser!!.uid

         fStore.collection("Orders").document(userId).update("Card Number",cardForm.cardNumber,
         "Postal code",cardForm.postalCode,
         )
         returnPositiveIntentValue()
     }

     private fun alertDialog() {
         val alertBuilder = AlertDialog.Builder(this)
         alertBuilder.setTitle("Confirm before purchase")
         alertBuilder.setMessage(
             """
                            Card number: ${cardForm.cardNumber}
                            Card expiry date: ${cardForm.expirationDateEditText.text.toString()}
                            Card CVV: ${cardForm.cvv}
                            Postal code: ${cardForm.postalCode}
                            """.trimIndent()
         )
         alertBuilder.setPositiveButton("Confirm",
             DialogInterface.OnClickListener { dialogInterface, i ->
                 dialogInterface.dismiss()
                 Toast.makeText(this@CardFormView, R.string.purchase.toString(), Toast.LENGTH_SHORT)
                     .show()
                 insertCardDataToDatabase()
             })
         alertBuilder.setNegativeButton("Cancel",
             DialogInterface.OnClickListener { dialogInterface, i ->
                 dialogInterface.dismiss()
                 returnFalseIntentValue()
             })
         val alertDialog = alertBuilder.create()
         alertDialog.show()
     }

     private fun returnFalseIntentValue(){
            val intent = Intent()
            intent.putExtra("name", false)
            setResult(RESULT_CANCELED,intent)
            finish()
     }

     private fun returnPositiveIntentValue() {
         val intent = Intent()
         intent.putExtra("name", true)
         setResult(RESULT_OK,intent)
         finish()
     }

     private fun setUpCardViewForm() {

        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(true)
            .setup(this)

        cardForm.cvvEditText.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

    }


}