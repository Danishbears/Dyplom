package com.example.diplomich.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.R
import com.example.diplomich.ViewModel.UserOrders
import com.example.diplomich.adapter.OrdersAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class AdminNewOrderActivity : AppCompatActivity() {
    private lateinit var recyclerCart: RecyclerView
    private lateinit var mDatabaseRef1: FirebaseFirestore
    private lateinit var mUploads:MutableList<UserOrders>
    private lateinit var mAdapter: OrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mUploads = ArrayList()
        recyclerCart = findViewById(R.id.new_orders_list)
        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager = LinearLayoutManager(applicationContext)
        mDatabaseRef1 = FirebaseFirestore.getInstance()

        val docRef = mDatabaseRef1.collection("Orders")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<UserOrders>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
            mAdapter = OrdersAdapter(applicationContext,mUploads,this)
            recyclerCart.adapter = mAdapter
            Log.d("VIBECHEdada", city[0].toString())
        }
            .addOnFailureListener{
                Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                Log.d("BAD",it.message.toString())
            }

    }
}