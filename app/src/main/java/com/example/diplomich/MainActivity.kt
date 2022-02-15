package com.example.diplomich

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.badge_text.view.*

import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okio.Timeout
import android.app.Activity
import android.content.res.Configuration
import com.google.android.material.internal.ContextUtils.getActivity
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent
import android.content.res.Resources
import android.os.Handler
import android.util.DisplayMetrics
import android.content.SharedPreferences
import com.example.diplomich.Admin.AdminActivity
import kotlinx.android.synthetic.main.fragment_catalog.*
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    private lateinit var notificationBadges: View
    private var count:Int = 1
    private lateinit var fAuth: FirebaseAuth
    private lateinit var fr: FirebaseFirestore
    private lateinit var mAdapter: NotificationAdapter
    private lateinit var uid:String
    private lateinit var sharedPref:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragmentContainerView2)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.catalogFragment,R.id.cartFragment,R.id.favoritesFragment,R.id.moreFragment))
        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        fAuth = FirebaseAuth.getInstance()
        fr = FirebaseFirestore.getInstance()
        uid = fAuth.currentUser!!.uid

        /*val activity: Activity? = getActivity()
        if (activity != null) {

            // etc ...
        }*/
      //  loadLocale()
        checkStateActivity()
        languageCheck()
        GlobalScope.launch {
            checkOrders()
        }

        loadUserConfiguration()

       // nightCheck()
       //languageCheck()
    }


    private fun languageCheck(){
        val documentReference: DocumentReference = fr.collection("users").document(uid)
        documentReference.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }

            if(snapshot?.getString("lang") == "en"){

                setLocale(snapshot.getString("lang").toString())
            }
            else if(snapshot?.getString("lang") == "pl"){

                setLocale(snapshot.getString("lang").toString())
            }
        }
    }

    override fun onStart() {

       /* val sp = getSharedPreferences(
            MainActivity.counti.toString(),
            MODE_PRIVATE
        )
        // проверяем, первый ли раз открывается программа
        // проверяем, первый ли раз открывается программа
        val int = sp.getInt("hasFirst",count)
        if(int==0){
            count.dec()
            Log.d("CheckCounti",counti.toString())
            val refresh = Intent(this, MainActivity::class.java)
            finish()
            startActivity(refresh)
        }
        if(int>0){

        }*/
        super.onStart()
    }

    @SuppressLint("CommitPrefEdits")
    private fun setLocale(lang: String) {
        val locale: Locale = Locale(lang)
        val res:Resources = resources
        val dm:DisplayMetrics = res.displayMetrics
        val conf:Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf,dm)
        checkStateActivity()
    }

    private fun checkStateActivity() {
        val sp = getSharedPreferences(
            MainActivity.counti.toString(),
            MODE_PRIVATE
        )
        val int = sp.getInt("hasFirst",count)
        if(int==0){
            counti.inc()
            Log.d("CheckCounti",counti.toString())
            val refresh = Intent(this, MainActivity::class.java)
            finish()
            startActivity(refresh)
        }
    }

    private fun loadLocale(){
        val editor = this?.getSharedPreferences("Settings", MODE_PRIVATE)
        val language: String? = editor?.getString("My Lang","")
        setLocale(language.toString())
    }

    private fun checkOrders() :Int{
        val docRef = fr.collection("Maded").document(uid).collection("ToUser")
        var counter:Int = 0
        checkSize()
        fr.collection("Maded").document(uid).collection("ToUser").addSnapshotListener{snapshot,e->
        if (e != null) {
            Log.w("ListenerCollection", "Listen failed.", e)
            return@addSnapshotListener
        }
        if (snapshot != null) {
            Log.d("CheckNotNull","${snapshot.documentChanges}")
            checkSize()
        } else {
            Log.d("CheckNull", "Current data: null")
        }
        }

        return counter
    }

    private fun checkSize(){
        var counter:Int = 0
        fr.collection("Maded").document(uid).collection("ToUser").whereEqualTo("isChecked",1)
            .get().addOnSuccessListener {
                counter = it.size()
                getItemCount(counter)
            }
    }

    private fun getItemCount(counter: Int) {
        count = counter
        updateBadgeCount(count)
    }


    private fun updateBadgeCount(count: Int = 0){
       // Toast.makeText(this,"${count}",Toast.LENGTH_SHORT).show()

        val itemView = bottomNavigationView.getChildAt(2) as? BottomNavigationItemView

        notificationBadges = LayoutInflater.from(this)
            .inflate(R.layout.badge_text,itemView,true)
        notificationBadges?.notification_badge?.text = count.toString()

        bottomNavigationView.addView(notificationBadges)
    }

    private fun loadUserConfiguration() {
        GlobalScope.launch {
            checkTypeOfUser()
            delay(500)
        }
    }

    private fun checkTypeOfUser(){
        val doc = returnUserFromDatabase()
        doc.addSnapshotListener{snapshot, e->
            if (e != null) {
                Log.d("TAG", "Failed to read data from Firestore ${e.message}")
                return@addSnapshotListener
            }

            if(snapshot?.getString("isAdmin") == "1"){
                loadAdminScreen()
            }
        }

    }


    private fun loadAdminScreen() {
        val adminIntent = Intent(this,AdminActivity::class.java)
        startActivity(adminIntent)
        finish()
    }

    private fun returnUserFromDatabase(): DocumentReference = fr.collection("users").document(uid)




    companion object{
    private val counti:Int = 1
}

}