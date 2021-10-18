package com.example.diplomich

import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class AdminAddProductActivity : AppCompatActivity() {

    private lateinit var categoryName:String
    private lateinit var imageView: ImageView
    private lateinit var addNewProductButt: Button
    private lateinit var inputProdName:EditText
    private lateinit var inputProdDescription:EditText
    private lateinit var inputProdPrice:EditText
    private lateinit var imageUri:Uri
    private lateinit var name:String
    private lateinit var description:String
    private lateinit var price:String
    private lateinit var saveCurrentDate:String
    private lateinit var saveCurrentTime:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)
        categoryName = intent.extras!!.get("category").toString()
        Toast.makeText(this,categoryName,Toast.LENGTH_SHORT).show()

        imageView = findViewById(R.id.select_product)
        addNewProductButt = findViewById(R.id.addButton)
        inputProdName = findViewById(R.id.product_name)
        inputProdDescription = findViewById(R.id.product_description)
        inputProdPrice = findViewById(R.id.product_price)

        imageView.setOnClickListener {
            openGallery()
        }
        addNewProductButt.setOnClickListener{
            validateProduct()
        }

    }

    private fun validateProduct() {
        description = inputProdDescription.text.toString()
        name = inputProdName.text.toString()
        price = inputProdPrice.text.toString()

        if(imageUri == null){
            Toast.makeText(this,R.string.imageRequired,Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(this,R.string.descriptionRequired,Toast.LENGTH_LONG).show()
        }else if(TextUtils.isEmpty(price) && price.toInt()<=0){
            Toast.makeText(this,R.string.priceRequired,Toast.LENGTH_SHORT).show()
        }
        else{
            storeProductInfo()
        }
    }

    private fun storeProductInfo() {

        val calendar:Calendar = Calendar.getInstance()
        val currentDate:SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime:SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calendar.time)



    }

    private fun openGallery(){
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/"
        startActivityForResult(galleryIntent, GalleryPick)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GalleryPick && resultCode == RESULT_OK  && data!= null){
            imageUri = data.data!!
            imageView.setImageURI(imageUri)
        }
    }

    companion object{
        private const val GalleryPick = 1
    }
}