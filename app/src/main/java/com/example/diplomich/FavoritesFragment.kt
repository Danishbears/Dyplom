package com.example.diplomich

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomich.ViewModel.Products
import com.example.diplomich.adapter.MyAdapter
import com.example.diplomich.adapter.OrdersAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class FavoritesFragment : Fragment() {
    private lateinit var fr: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_favorites, container, false)
        return root
    }

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