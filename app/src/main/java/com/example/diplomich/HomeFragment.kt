package com.example.diplomich

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.diplomich.Authentication.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var  buttonLogOut:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val rootView:View= inflater.inflate(R.layout.fragment_home, container, false)
        buttonLogOut = rootView.findViewById(R.id.buttonPacaton) as Button
        buttonLogOut.setOnClickListener(this)
        return rootView
    }
    override fun onClick(p0: View?) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireActivity().applicationContext,LoginActivity::class.java))
        requireActivity().finish()
    }
}
