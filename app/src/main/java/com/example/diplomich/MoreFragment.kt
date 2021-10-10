package com.example.diplomich

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MoreFragment : Fragment() {
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var userName:TextView
    private lateinit var userPhoto:ImageView
    private lateinit var fAuth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    private lateinit var userId:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_more, container, false)
        userEmail = rootView.findViewById(R.id.userEmailInfo) as TextView
        userPhone = rootView.findViewById(R.id.userPhoneNumber)
        userName = rootView.findViewById(R.id.HelloUserName)
        userPhoto = rootView.findViewById(R.id.imageUser)


        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userId = fAuth.currentUser!!.uid

        val documentReference:DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{snapshot, e->
            userPhone.text = snapshot!!.getString("PhoneNumber")
            userName.text = "${resources.getString(R.string.HelloToUser)} ${snapshot!!.getString("Name")}"
            userEmail.text = snapshot!!.getString("Email")
        }

        return rootView
    }


}