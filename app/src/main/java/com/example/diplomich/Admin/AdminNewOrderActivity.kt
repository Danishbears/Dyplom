package com.example.diplomich.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diplomich.R

class AdminNewOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_new_order)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
}