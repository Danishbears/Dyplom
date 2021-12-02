package com.example.diplomich

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.searchviewexample.FindItem
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.adapter.SearchAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class SearchFragment : AppCompatActivity() {

private lateinit var adminSearch:String
private lateinit var inflater: MenuInflater
private lateinit var adapter:SearchAdapter
private lateinit var adapter1: FindItem
private lateinit var recyclerCart: RecyclerView
private lateinit var fStore: FirebaseFirestore
private lateinit var mUploads:MutableList<Products>
private lateinit var query:Query

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_search)
        fStore = FirebaseFirestore.getInstance()
        query = fStore.collection("products").orderBy("category")
        adminSearch = intent.getStringExtra("string").toString()
        recyclerCart = findViewById(R.id.searchRecycler)
        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager = LinearLayoutManager(applicationContext)
        processSearch("dada")
        mUploads = ArrayList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        inflater = menuInflater
        inflater.inflate(R.menu.menu_search,menu)
        var item: MenuItem = menu!!.findItem(R.id.search)
        var searchView: SearchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ChechOut",query.toString())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("CheckOut",newText.toString())

                adapter1.filter.filter(newText)
                return false
            }

        })
        return true
    }

    private fun processSearch(query1: String?) {
        Log.d("I'm here",query.toString())


        val docRef = fStore.collection("products")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }

            adapter1 = FindItem(mUploads)
            recyclerCart.adapter = adapter1

    }
    }

}