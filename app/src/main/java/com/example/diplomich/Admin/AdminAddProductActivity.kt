package com.example.diplomich.Admin

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.example.diplomich.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
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
    private lateinit var fStore:FirebaseFirestore
    private lateinit var productRandKey:String
    private lateinit var productImageRef:StorageReference
    private lateinit var downloadImageUri:String
    private lateinit var loadingBar:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_product)
        categoryName = intent.extras!!.get("category").toString()
        Toast.makeText(this,categoryName,Toast.LENGTH_SHORT).show()

        imageView = findViewById(R.id.select_product)
        addNewProductButt = findViewById(R.id.addButton)
        inputProdName = findViewById(R.id.product_name)
        fStore = FirebaseFirestore.getInstance()
        inputProdDescription = findViewById(R.id.product_description)
        inputProdPrice = findViewById(R.id.product_price)
        productImageRef = FirebaseStorage.getInstance().reference.child("Product Images")
        loadingBar = ProgressDialog(this)
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
            Toast.makeText(this, R.string.imageRequired,Toast.LENGTH_SHORT).show()
        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, R.string.descriptionRequired,Toast.LENGTH_LONG).show()
        }else if(TextUtils.isEmpty(price)){
            Toast.makeText(this, R.string.priceRequired,Toast.LENGTH_SHORT).show()
        }
        else{
            storeProductInfo()
        }
    }

    private fun storeProductInfo() {

        loadingBar.setTitle(R.string.LoadingTitle)
        loadingBar.setMessage(resources.getString(R.string.loadingMessage))
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()

        val calendar:Calendar = Calendar.getInstance()
        val currentDate:SimpleDateFormat = SimpleDateFormat("MMM dd, yyyy")
        saveCurrentDate = currentDate.format(calendar.time)

        val currentTime:SimpleDateFormat = SimpleDateFormat("HH:mm:ss")
        saveCurrentTime = currentTime.format(calendar.time)

        productRandKey = saveCurrentDate + saveCurrentTime


        val filePath:StorageReference = productImageRef.child(productRandKey + ".jpg")
        uploadTask = filePath.putFile(imageUri)
        uploadTask.addOnFailureListener{
            val message:String = it.toString()
            Toast.makeText(this,"Error: $message",Toast.LENGTH_SHORT).show()
            loadingBar.dismiss()
        }
            .addOnSuccessListener {
                Toast.makeText(this, R.string.uploadedImage,Toast.LENGTH_SHORT).show()
                var uriTask: Task<Uri> = uploadTask.continueWithTask(Continuation {

                    if(!it.isSuccessful){
                        throw it.exception!!
                    }
                    downloadImageUri = filePath.downloadUrl.toString()
                    return@Continuation filePath.downloadUrl

                })
            }.addOnCompleteListener(OnCompleteListener {
                if(it.isSuccessful){
                    downloadImageUri = it.result.toString()
                    Toast.makeText(this, R.string.AdminMessage,Toast.LENGTH_SHORT).show()
                    saveProductInfoToDatabase()
                }
            })

    }

    private fun saveProductInfoToDatabase() {
        val user:HashMap<String,Any> = hashMapOf(
            "pid" to productRandKey,
            "date" to saveCurrentDate,
            "time" to saveCurrentTime,
            "description" to description,
            "image" to downloadImageUri,
            "category" to categoryName,
            "price" to price,
            "name" to name)


        var documentReference: DocumentReference = fStore.collection("products").document(productRandKey)
        documentReference.set(user).addOnSuccessListener {
            loadingBar.dismiss()
            Log.d("TAG", "onSuccess: product has been added is$productRandKey")
            Toast.makeText(this,"Product added successfully",Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, AdminActivity::class.java))
        }
            .addOnFailureListener{
                loadingBar.dismiss()
                Log.d("ERR","Error ${it.message}")
            }

    }

    private fun openGallery(){
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
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
        private lateinit var uploadTask:UploadTask
    }
}