package com.example.shobisapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shobisapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private lateinit var profile : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        profile = getSharedPreferences("login_session", MODE_PRIVATE)

        //show profile data
        binding?.fullnameTextView?.text = profile.getString("fullname", null)
        binding?.emailTextView?.text = profile.getString("email", null)

        //function log out button
        binding?.buttonLogout?.setOnClickListener {
            profile.edit().clear().commit()

            binding!!.loading.visibility = View.GONE
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            Toast.makeText(this@MainActivity, "Logout success ", Toast.LENGTH_LONG)
                .show()
            finish()
        }
    }
}