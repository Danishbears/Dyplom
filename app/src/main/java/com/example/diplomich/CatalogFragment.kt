package com.example.diplomich

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.adapter.AdminUserAdapter
import com.example.diplomich.adapter.CatalogAdapter
import com.example.diplomich.adapter.MyAdapter
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
    private var mouse:String = "mouse"
    private var keyboard:String = "keyboard"

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

        layout1.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext,FragmentCategory::class.java)
            intent.putExtra("mouse",mouse)
            requireContext().startActivity(intent)
          //  getFirestoreCollection(docRef,"mouse")
        }

        layout2.setOnClickListener {
            val intent = Intent(requireActivity().applicationContext,FragmentCategory::class.java)
            intent.putExtra("keyboard",keyboard)
            requireContext().startActivity(intent)
           // getFirestoreCollection(docRef,"keyboard")
        }

        return viewroot
    }


}