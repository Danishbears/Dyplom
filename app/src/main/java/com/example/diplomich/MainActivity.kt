package com.example.diplomich

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.diplomich.ViewModel.Ordered
import com.example.diplomich.adapter.NotificationAdapter
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.badge_text.view.*
import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener




class MainActivity : AppCompatActivity() {

    private lateinit var notificationBadges: View
    private var count:Int = 1
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fr: FirebaseFirestore
    private lateinit var mAdapter: NotificationAdapter
    private lateinit var mUploads:MutableList<Ordered>
    private lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView2)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.catalogFragment,R.id.cartFragment,R.id.favoritesFragment,R.id.moreFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        initializeMet()
        checkOrders()
    }

    private fun checkOrders() :Int{
        val docRef = fr.collection("Maded").document(uid).collection("ToUser")
        var counter:Int = 0
       /* docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Ordered>()
            for (eachIndex in city.indices) {
                Log.d("CcheckPacek",eachIndex.toString())
               /* if(city[eachIndex].isChecked == 1){
                    mUploads.add(city[eachIndex])*/
                if (eachIndex != null) {
                    mUploads.add(city[eachIndex])
                }
           // }
            }
            counter = mUploads.size
            getItemCount(counter)

        }*/

         fr.collection("Maded").document(uid).collection("ToUser").whereEqualTo("isChecked",1)
             .get().addOnSuccessListener {
                 counter = it.size()
                 getItemCount(counter)
             }


        return counter
    }

    private fun getItemCount(counter: Int) {
        count = counter
        updateBadgeCount(count)
    }

    private fun initializeMet() {
        fAuth = FirebaseAuth.getInstance()
        fr = FirebaseFirestore.getInstance()
        uid = fAuth.currentUser!!.uid
        mUploads = ArrayList()
    }

    private fun updateBadgeCount(count: Int = 0){
       // Toast.makeText(this,"${count}",Toast.LENGTH_SHORT).show()
        val itemView = bottomNavigationView.getChildAt(2) as? BottomNavigationItemView

        notificationBadges = LayoutInflater.from(this)
            .inflate(R.layout.badge_text,itemView,true)
        notificationBadges?.notification_badge?.text = count.toString()

        bottomNavigationView.addView(notificationBadges)
    }

}