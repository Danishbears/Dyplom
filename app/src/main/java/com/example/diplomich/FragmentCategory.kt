package com.example.diplomich

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.adapter.CatalogAdapter
import com.example.diplomich.adapter.MyAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class FragmentCategory : AppCompatActivity() {
    private  var mouse:String =""
    private  var keyboard:String =""
    private lateinit var mDatabaseRef1: FirebaseFirestore
    private lateinit var mUploads:MutableList<Products>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var searchItem:EditText
    private lateinit var searchButton:Button
    private lateinit var searchInput:String


    override fun onCreate(savedInstanceState: Bundle?){
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_category)

        getKeyboardValue()
        getMouseValue()

        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()

        mRecyclerView = findViewById(R.id.recycler_catalog)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(applicationContext)

       // searchItem = findViewById(R.id.admin)
      //  searchButton = findViewById(R.id.button_search)

        val docRef = mDatabaseRef1.collection("products")
        intentSwitch(docRef)

       /* searchButton.setOnClickListener {
            Toast.makeText(this,"ada",Toast.LENGTH_SHORT).show()
            searchInput = searchItem.text.toString()
          //  mAdapter = SearchAdapter(this,mUploads,searchItem.text.toString())
           // mRecyclerView.adapter = mAdapter
        }*/
        onStop()
    }




    private fun getKeyboardValue(){
        keyboard = intent.getStringExtra("keyboard").toString()
    }

    private fun getMouseValue(){
        mouse = intent.getStringExtra("mouse").toString()
    }

    private fun intentSwitch(docRef: CollectionReference) {

        if(mouse.contains("mouse")){
            getFirestoreCollection(docRef,mouse)
            return
        }
        if(keyboard.contains("keyboard")){
            getFirestoreCollection(docRef,keyboard)
            return
        }
    }


    private fun getFirestoreCollection(docRef: CollectionReference, category: String) {
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    if(city.get(eachIndex).category == category){
                    mUploads.add(city[eachIndex])}}

            }
            catalogAdapter = CatalogAdapter(applicationContext,mUploads,category)
            mRecyclerView.adapter = catalogAdapter
        }
    }

    companion object {

    }
}