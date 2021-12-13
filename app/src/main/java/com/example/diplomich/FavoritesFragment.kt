package com.example.diplomich

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Ordered
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.adapter.MyAdapter
import com.example.diplomich.adapter.NotificationAdapter
import com.example.diplomich.adapter.OrdersAdapter
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.badge_text.view.*


class FavoritesFragment : Fragment() {
    private lateinit var fr: FirebaseFirestore
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var fAuth: FirebaseAuth

    private lateinit var mAdapter: NotificationAdapter
    private lateinit var mUploads:MutableList<Ordered>
    private lateinit var uid:String

    private lateinit var notificationBadges: View
    private var count:Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_favorites, container, false)
        fAuth = FirebaseAuth.getInstance()
        fr = FirebaseFirestore.getInstance()
        uid = fAuth.currentUser!!.uid
        mUploads = ArrayList()
        mRecyclerView = root.findViewById(R.id.recycler_notification)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(root.context)
        /*val docRef = fr.collection("Maded")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Ordered>()
            val check:Ordered = Ordered()
            for(eachIndex in city.indices){
                if(eachIndex!=null){
                    mUploads.add(city[eachIndex])
                }
            }
            mAdapter = NotificationAdapter(requireActivity().applicationContext,mUploads)
            mRecyclerView.adapter = mAdapter
            //Log.d("VIBECHEdada", city[0].toString())
        }*/

        val docRef = fr.collection("Maded").document(uid).collection("ToUser")

        docRef.get().addOnSuccessListener { documentSnapshot ->
            val city = documentSnapshot.toObjects<Ordered>()
            for (eachIndex in city.indices) {
                if (eachIndex != null) {
                    mUploads.add(city[eachIndex])
                }
            }
            mAdapter = NotificationAdapter(requireActivity().applicationContext, mUploads)
            mRecyclerView.adapter = mAdapter
        }
        checkFragmentVisibility()
        return root
    }

    private fun checkFragmentVisibility() {
        val test: FavoritesFragment? = activity?.supportFragmentManager
            ?.findFragmentByTag("testID") as? FavoritesFragment
        if (test != null && test.isVisible) {
            Toast.makeText(requireContext().applicationContext, "Check INgo", Toast.LENGTH_SHORT)
                .show()
        } else {
            //Toast.makeText(requireContext().applicationContext, "Didnds", Toast.LENGTH_SHORT).show()
            val docRef = fr.collection("Maded").document(uid).collection("ToUser")
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val city = documentSnapshot.toObjects<Ordered>()
                for (eachIndex in city.indices) {
                    if (eachIndex != null) {
                        val products: Ordered = mUploads[eachIndex]
                        fr.collection("Maded").document(uid).collection("ToUser")
                            .document(products.time.toString())
                            .update("isChecked", 0)
                    }
                }
            }
        }
    }

   /* override fun onHiddenChanged(hidden: Boolean) {
        if(hidden){
           /* val washingtonRef = fr.collection("Maded").document(uid).collection("ToUser")
            washingtonRef
                .update("isChecked", 0)
                .addOnSuccessListener { Log.d("YEah", "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w("Bad", "Error updating document", e) }*/
          /*  val docRef = fr.collection("Maded").document(uid).collection("ToUser")
            docRef.get().addOnSuccessListener { documentSnapshot ->
                val city = documentSnapshot.toObjects<Ordered>()
                for (eachIndex in city.indices) {
                    if (eachIndex != null) {
                        val products: Ordered = mUploads[eachIndex]
                        products.isChecked = 0
                    }
                }
            }*/
        }
        super.onHiddenChanged(hidden)
    }*/

    /*private fun notificationMessage(id: String?, holder: OrdersAdapter.MyViewHolder) {
        holder.db.collection("Orders").document(id!!).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Check", "Listen failed.", e)
                return@addSnapshotListener
            }


            if (snapshot != null && snapshot.exists()) {
                Log.d("Adada", "Current data: ${snapshot.data}")
            } else {
                val builder = NotificationCompat.Builder(root.context,"1231")
                    .setSmallIcon(R.drawable.ic_favorite_24)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true)
                with(NotificationManagerCompat.from(root.context)) {
                    // notificationId is a unique int for each notification that you must define
                    notify(1231, builder.build())
                }
            }
        }
    }*/

}