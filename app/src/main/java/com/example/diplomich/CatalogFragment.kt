package com.example.diplomich

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.diplomich.ViewModel.Products
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class CatalogFragment : Fragment() {

    private lateinit var layout1:View
    private lateinit var layout2:View
    private lateinit var layout3:View
    private lateinit var layout4:View
    private lateinit var layout5:View
    private lateinit var layout6:View
    private lateinit var mDatabaseRef1: FirebaseFirestore
    private lateinit var mUploads:MutableList<Products>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewroot =  inflater.inflate(R.layout.fragment_catalog, container, false)
        layout1 = viewroot.findViewById(R.id.Linear1)
        layout2 = viewroot.findViewById(R.id.Linear2)
        layout3 = viewroot.findViewById(R.id.Linear3)
        layout4 = viewroot.findViewById(R.id.Linear4)
        layout5 = viewroot.findViewById(R.id.Linear5)
        layout6 = viewroot.findViewById(R.id.Linear6)
        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()
        val docRef = mDatabaseRef1.collection("products")

        layout1.setOnClickListener {
            getFirestoreCollection(docRef,"mouse")
        }

        layout2.setOnClickListener {
            getFirestoreCollection(docRef,"keyboard")
        }

        return viewroot
    }

    private fun getFirestoreCollection(docRef: CollectionReference, category: String) {
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
        }
    }

}