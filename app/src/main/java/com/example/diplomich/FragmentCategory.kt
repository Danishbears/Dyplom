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
import com.example.diplomich.common.Constants
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class FragmentCategory : AppCompatActivity() {
    private  var mouse:String =""
    private  var keyboard:String =""
    private  var gameController =""
    private  var monitor =""
    private  var phone =""
    private  var videoCard =""
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
        getGameControllerValue()
        getMonitorValue()
        getPhoneNumber()
        getVideoCard()

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

    private fun getVideoCard() {
        videoCard = intent.getStringExtra(Constants.VIDEO_CARD_ID).toString()
    }

    private fun getPhoneNumber() {
        phone = intent.getStringExtra(Constants.PHONE_ID).toString()
    }

    private fun getMonitorValue() {
        monitor = intent.getStringExtra(Constants.MONITOR_ID).toString()
    }

    private fun getGameControllerValue() {
        gameController = intent.getStringExtra(Constants.GAME_CONTROLLER_ID).toString()
    }


    private fun getKeyboardValue(){
        keyboard = intent.getStringExtra(Constants.KEYBOARD_ID).toString()
    }

    private fun getMouseValue(){
        mouse = intent.getStringExtra(Constants.MOUSE_ID).toString()
    }

    private fun intentSwitch(docRef: CollectionReference) {

        if(mouse.contains(Constants.MOUSE_ID)){
            getFirestoreCollection(docRef,mouse)
            return
        }
        if(keyboard.contains(Constants.KEYBOARD_ID)){
            getFirestoreCollection(docRef,keyboard)
            return
        }
        if(gameController.contains(Constants.GAME_CONTROLLER_ID)){
            getFirestoreCollection(docRef,gameController)
            return
        }
        if(monitor.contains(Constants.MONITOR_ID)){
            getFirestoreCollection(docRef,monitor)
            return
        }

        if(phone.contains(Constants.PHONE_ID)){
            getFirestoreCollection(docRef,phone)
            return
        }

        if(videoCard.contains(Constants.VIDEO_CARD_ID)){
            getFirestoreCollection(docRef,videoCard)
            return
        }

        else{
            Toast.makeText(this,application.getString(R.string.NotFoundCategory),Toast.LENGTH_SHORT).show()
        }

    }


    private fun getFirestoreCollection(docRef: CollectionReference, category: String) {
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    if(city[eachIndex].category == category){
                    mUploads.add(city[eachIndex])}}

            }
            catalogAdapter = CatalogAdapter(applicationContext,mUploads,category)
            mRecyclerView.adapter = catalogAdapter
        }
    }

    companion object {

    }
}