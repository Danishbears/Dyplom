package com.example.diplomich

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.adapter.CartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class CartFragment : Fragment() {
private lateinit var recyclerCart:RecyclerView
private lateinit var priceButton:Button
private lateinit var mDatabaseRef1: FirebaseFirestore
private lateinit var mUploads:MutableList<Cart>
private lateinit var mAdapter: CartAdapter
private lateinit var fAuth: FirebaseAuth
private lateinit var userId:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_cart, container, false)
        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()
        fAuth = FirebaseAuth.getInstance()
        userId = fAuth.currentUser!!.uid
        priceButton = root.findViewById(R.id.price_button)
        recyclerCart = root.findViewById(R.id.recycler_cart)
        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager = LinearLayoutManager(root.context)

        val docRef = mDatabaseRef1.collection("CartList").document(userId)
            .collection("ProductId")


        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Cart>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
            mAdapter = CartAdapter(requireActivity().applicationContext,mUploads,activity)
            recyclerCart.adapter = mAdapter
        }

        return root
    }

}