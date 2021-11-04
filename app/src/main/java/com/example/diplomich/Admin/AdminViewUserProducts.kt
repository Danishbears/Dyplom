package com.example.diplomich.Admin


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.R
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.adapter.AdminUserAdapter
import com.example.diplomich.adapter.CartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class AdminViewUserProducts : AppCompatActivity() {
    private lateinit var userId:String
    private lateinit var recyclerCart: RecyclerView
    private lateinit var mDatabaseRef1: FirebaseFirestore
    private lateinit var mUploads:MutableList<Cart>
    private lateinit var mAdapter: AdminUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_view_user_products)
        userId = intent.getStringExtra("uid").toString()

        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()

        recyclerCart = findViewById(R.id.admin_view_user_products)
        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager = LinearLayoutManager(applicationContext)

        val documentRef = mDatabaseRef1.collection("CartList")
            .document(userId)
            .collection("ProductId")
        documentRef.get().addOnSuccessListener {
                documentSnapshot ->
            val city = documentSnapshot.toObjects<Cart>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
            mAdapter = AdminUserAdapter(applicationContext,mUploads,this)
            recyclerCart.adapter = mAdapter
        }
    }
}