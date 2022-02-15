package com.example.diplomich.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.diplomich.Admin.AdminAddProductActivity
import com.example.diplomich.Authentication.LoginActivity
import com.example.diplomich.R
import com.example.diplomich.common.Constants
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {
    private lateinit var phoneView: ImageView
    private lateinit var mouseView:ImageView
    private lateinit var keyboardView:ImageView
    private lateinit var monitorView:ImageView
    private lateinit var videoCardView:ImageView
    private lateinit var gamepadView:ImageView
    private lateinit var buttonLog: Button
    private lateinit var buttonCheck:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        buttonLog = findViewById(R.id.buttonAdmLogout)
        buttonLog.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext,LoginActivity::class.java))
            finish()
        }
        phoneView = findViewById(R.id.phone)
        mouseView = findViewById(R.id.mouse)
        keyboardView = findViewById(R.id.keyboard)
        monitorView = findViewById(R.id.monitor)
        videoCardView = findViewById(R.id.videocard)
        gamepadView = findViewById(R.id.gamepad)
        buttonCheck = findViewById(R.id.checkNewItems)

        buttonCheck.setOnClickListener {
            startActivity(Intent(applicationContext,AdminNewOrderActivity::class.java))
        }

        phoneView.setOnClickListener {
            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.PHONE_ID)
            startActivity(intent)
        }
        mouseView.setOnClickListener {
            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.MOUSE_ID)
            startActivity(intent)
        }
        keyboardView.setOnClickListener {
            val intent= Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.KEYBOARD_ID)
            startActivity(intent)
        }
        monitorView.setOnClickListener{
            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.MONITOR_ID)
            startActivity(intent)
        }
        videoCardView.setOnClickListener{
            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.VIDEO_CARD_ID)
            startActivity(intent)
        }
        gamepadView.setOnClickListener {
            val intent = Intent(this, AdminAddProductActivity::class.java)
            intent.putExtra("category",Constants.GAME_CONTROLLER_ID)
            startActivity(intent)
        }
    }
}