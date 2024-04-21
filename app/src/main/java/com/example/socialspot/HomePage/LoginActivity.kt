package com.example.socialspot.HomePage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.socialspot.MainActivity
import com.example.socialspot.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {


    private lateinit var texttogoSinUp : TextView
    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnLogin : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }



        texttogoSinUp = findViewById(R.id.go_sign_up)
        btnLogin = findViewById(R.id.btn_login)
        edtEmail =findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)





        texttogoSinUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        clearInputFields()

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            login(email,password)
        }



        if (isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }



    }




    private fun login(email:String, password : String) {

        if(TextUtils.isEmpty(edtEmail.text.toString())){
            edtEmail.setError("please enter Email")
            return
        }else if(TextUtils.isEmpty(edtPassword.text.toString())) {
            edtPassword.setError("please enter Password")
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Set is_logged_in to true
                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

                    // Sign in success, update UI with the signed-in user's information
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "some error occured", Toast.LENGTH_SHORT).show()

                }
            }
    }


    private fun isLoggedIn(): Boolean {
        // Retrieve the login state from SharedPreferences
        val isLoggedIn =  sharedPreferences.getBoolean("is_logged_in", false)
        Log.d("LoginActivity", "Is logged in: $isLoggedIn")
        return isLoggedIn
    }

    private fun clearInputFields() {
        edtEmail.text = null
        edtPassword.text = null

    }


}