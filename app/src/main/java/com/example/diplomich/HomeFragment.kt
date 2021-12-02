package com.example.diplomich

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Products

import com.example.diplomich.adapter.MyAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeFragment : Fragment() {

    private lateinit var mRecyclerView:RecyclerView
    private lateinit var mAdapter: MyAdapter
    private lateinit var mDatabaseRef1: FirebaseFirestore
    private lateinit var mUploads:MutableList<Products>
    private lateinit var imageStorage:StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView:View = inflater.inflate(R.layout.fragment_home, container, false)
        mDatabaseRef1 = FirebaseFirestore.getInstance()
        mUploads = ArrayList()
        mRecyclerView = rootView.findViewById(R.id.recycler_home)
        mRecyclerView.setHasFixedSize(true)
        imageStorage = FirebaseStorage.getInstance().reference
        mRecyclerView.layoutManager = LinearLayoutManager(rootView.context)
        val docRef = mDatabaseRef1.collection("products")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Products>()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                mUploads.add(city[eachIndex])
            }
            }
            mAdapter = MyAdapter(requireActivity().applicationContext,mUploads)
            mRecyclerView.adapter = mAdapter
            //Log.d("VIBECHEdada", city[0].toString())
        }


        return rootView

    }

}
