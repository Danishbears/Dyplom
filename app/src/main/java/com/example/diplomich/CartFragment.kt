package com.example.diplomich

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Cart
import com.example.diplomich.adapter.CartAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import android.os.Build
import androidx.fragment.app.FragmentTransaction
import com.example.diplomich.Order.ConfirmOrderActivity
import com.example.diplomich.ViewModel.Ordered
import com.example.diplomich.ViewModel.Products


class CartFragment : Fragment() {
private lateinit var recyclerCart:RecyclerView
private lateinit var priceButton:Button
private lateinit var mDatabaseRef1: FirebaseFirestore
private lateinit var mUploads:MutableList<Cart>
private lateinit var mAdapter: CartAdapter
private lateinit var fAuth: FirebaseAuth
private lateinit var userId:String
private lateinit var totalPrice:String
private lateinit var imageVertButton:ImageButton
private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_cart, container, false)
        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()
        fAuth = FirebaseAuth.getInstance()
        userId = fAuth.currentUser!!.uid
        priceButton = root.findViewById(R.id.price_button)
        recyclerCart = root.findViewById(R.id.recycler_cart)
        imageVertButton = root.findViewById(R.id.imageAllVertical)
        db = FirebaseFirestore.getInstance()

        recyclerCart.setHasFixedSize(true)
        recyclerCart.layoutManager = LinearLayoutManager(root.context)

        priceButton.setOnClickListener {
            val intent = Intent(root.context, ConfirmOrderActivity::class.java)
            intent.putExtra("Total price", priceButton.text.toString())
            startActivity(intent)
        }

        imageVertButton.setOnClickListener {
            deleteAllFiles()
        }

        val docRef = mDatabaseRef1.collection("CartList").document(userId).collection("ProductId")

        /*  mDatabaseRef1.collection("CartList").document(userId).collection("ProductId").addSnapshotListener{snapshot,e->
            if (e != null) {
                Log.w("ListenerCollection", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                Log.d("CheckNotNull","${snapshot.documentChanges}")
                checkUpdate()
            } else {
                Log.d("CheckNull", "Current data: null")
            }
        }*/

        var totalCount: Int = 0
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Cart>()
            for (eachIndex in city.indices) {
                if (eachIndex != null) {
                    mUploads.add(city[eachIndex])
                }
            }
            for (eachPrice in city.indices) {
                totalCount += city[eachPrice].Currentprice?.toInt()!!
            }

                totalPrice = totalCount.toString()
                priceButton.text = totalPrice

                mAdapter = CartAdapter(requireActivity().applicationContext, mUploads, activity)
                recyclerCart.adapter = mAdapter

        }
            checkUpdate()
            return root
        }

    private fun checkUserCart() {
        /*var totalCount:Int =0
        val docRef = mDatabaseRef1.collection("CartList").document(userId)
            .collection("ProductId")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Cart>()
            for (eachIndex in city.indices) {
                if (eachIndex != null) {
                    mUploads.add(city[eachIndex])
                }
            }
            for (eachPrice in city.indices) {
                totalCount += city[eachPrice].price?.toInt()!!

            }
            totalPrice = totalCount.toString()
            priceButton.text = totalPrice

            mAdapter = CartAdapter(requireActivity().applicationContext, mUploads, activity)
            recyclerCart.adapter = mAdapter
        }*/

        val ft: FragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false)
        }
        ft.detach(this).attach(this).commit()

    }
    private fun checkUpdate(){
        mDatabaseRef1.collection("CartList").document(userId).collection("ProductId")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("Ych", "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {Log.d("NewWyser", "New city: ${dc.document.data}")
                           // checkUserCart()
                        }
                        DocumentChange.Type.MODIFIED -> {Log.d("Modify", "Modified city: ${dc.document.data}")
                           // checkUserCart()
                        }
                        DocumentChange.Type.REMOVED -> {
                            Log.d("Removed", "Removed city: ${dc.document.data}")
                            checkUserCart()
                        }
                    }
                }
            }
    }

    private fun deleteAllFiles() {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Do you want to delete all these items")
        builder.setSingleChoiceItems(
            CartFragment.arrayChoice,-1
        ) { dialog, which ->
            if (which == 0) {
                val docRef =   db.collection("CartList").document(userId).collection("ProductId")
                docRef.get().addOnSuccessListener {documentSnapshot ->
                    val city = documentSnapshot.toObjects<Products>()
                    for (eachIndex in city.indices) {
                        if (eachIndex != null) {
                            val products: Cart = mUploads[eachIndex]
                            db.collection("CartList").document(userId)
                                .collection("ProductId")
                                .document(products.pid.toString()).delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "REMOVED", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            } else if (which == 1) {
                dialog.dismiss()
            }
            dialog.dismiss()
        }
        val mDialog:AlertDialog = builder.create()
        mDialog.show()
    }
    companion object{
        val arrayChoice = arrayOf("Yes","No")
    }

}