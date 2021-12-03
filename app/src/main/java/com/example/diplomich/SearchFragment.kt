package com.example.diplomich

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.searchviewexample.FindItem
import com.example.diplomich.ViewModel.Products
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class SearchFragment : AppCompatActivity() {

private lateinit var adminSearch:String
private lateinit var inflater: MenuInflater
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
        searchView.isFocusable = true;
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ChechOut",query.toString())
               // recyclerCart.visibility = View.VISIBLE
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("CheckOut",newText.toString())
                recyclerCart.visibility = View.VISIBLE
                adapter1.filter.filter(newText)
                return false
            }

        })

        searchView.setOnQueryTextFocusChangeListener(object :View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    recyclerCart.visibility = View.GONE
                } else if (!hasFocus) {
                    recyclerCart.visibility = View.GONE
                }
            }
        })
        return true
    }

    private fun processSearch(query1: String?) {

        val docRef = fStore.collection("products")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
            if(mUploads.isEmpty()){
                Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show()
            }
            if(mUploads.isNotEmpty()){
                //Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show()
                adapter1 = FindItem(mUploads)
                recyclerCart.adapter = adapter1
                recyclerCart.visibility = View.GONE

            }

    }
    }

}