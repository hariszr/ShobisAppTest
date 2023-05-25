package com.example.shobisapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shobisapp.databinding.ActivityLoginBinding
import com.example.shobisapp.model.ResponsesLogin
import com.example.shobisapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var binding : ActivityLoginBinding? = null
    private var email : String = ""
    private var password : String = ""
    private lateinit var profile : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //cek session
        profile = getSharedPreferences("login_session", MODE_PRIVATE)
        if (profile.getString("email", null) != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding!!.buttonLogin.setOnClickListener {
            email = binding!!.emailEditText.text.toString()
            password = binding!!.passwordEditText.text.toString()

            when {
                email == "" -> {
                    binding!!.emailEditText.error = "Email is empty"
                }
                password == "" -> {
                    binding!!.passwordEditText.error ="Password is empty"
                }
                else -> {
                    binding!!.loading.visibility = View.VISIBLE

                    getData()
                }
            }
        }
    }

    private fun getData() {
        val api = RetrofitClient().getInstance()
        api.login(email, password).enqueue(object  : Callback<ResponsesLogin> {
            override fun onResponse(
                call: Call<ResponsesLogin>,
                response: Response<ResponsesLogin>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.response == true) {

                        //buat session
                        getSharedPreferences("login_session", MODE_PRIVATE)
                            .edit()
                            .putString("fullname", response.body()?.payload?.fullname)
                            .putString("email", response.body()?.payload?.email)
                            .apply()

                        binding!!.loading.visibility = View.GONE
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        Toast.makeText(this@LoginActivity, "Login success ", Toast.LENGTH_LONG)
                            .show()
                        finish()
                    } else {
                        binding!!.loading.visibility = View.GONE
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, check your email and password again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    binding!!.loading.visibility = View.GONE
                    Toast.makeText(
                        this@LoginActivity,
                        "Login failed, an error occurred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

                override fun onFailure(call: Call<ResponsesLogin>, t: Throwable) {
                    Log.e("Error message", "${t.message}")
                }
        })
    }
}