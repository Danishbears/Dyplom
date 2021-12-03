package com.example.diplomich

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.diplomich.Authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MoreFragment : Fragment(){
    private lateinit var userEmail: TextView
    private lateinit var userPhone: TextView
    private lateinit var userName:TextView
    private lateinit var userPhoto:ImageView
    private lateinit var fAuth:FirebaseAuth
    private lateinit var fStore:FirebaseFirestore
    private lateinit var userId:String
    private lateinit var emailText:TextView
    private lateinit var buttonLogout: Button

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
        emailText = rootView.findViewById(R.id.verificationEmail)

        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        userId = fAuth.currentUser!!.uid

        buttonLogout = rootView.findViewById(R.id.logoutButton)
        buttonLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity().applicationContext,LoginActivity::class.java))
        }



        val user: FirebaseUser = fAuth.currentUser!!
        if(!user.isEmailVerified) {
            emailSent(user)
        }
        val documentReference:DocumentReference = fStore.collection("users").document(userId)
        documentReference.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }
            userPhone.text = snapshot!!.getString("PhoneNumber")
            userName.text = "${resources.getString(R.string.HelloToUser)} ${snapshot!!.getString("Name")}"
            userEmail.text = snapshot!!.getString("Email")
        }
        return rootView
    }

    private fun emailSent(user: FirebaseUser) {
            emailText.text = resources.getString(R.string.NotVerifiedEmail)
            emailText.visibility = View.VISIBLE
            emailText.setOnClickListener {
                user.sendEmailVerification().addOnSuccessListener {
                    Toast.makeText(requireActivity().applicationContext,R.string.VerificationEmail,Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener{
                        Log.d("TAG","On Failure ${it.message}")
                    }
            }
    }

}