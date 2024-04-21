package com.example.socialspot.HomePage

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.socialspot.DataClass.User
import com.example.socialspot.MainActivity
import com.example.socialspot.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {


    private lateinit var textTogoLogin: TextView
    private lateinit var edtUsername: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var edtConfirmPass: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }




        textTogoLogin = findViewById(R.id.go_login_page)
        btnSignUp = findViewById(R.id.btn_signup)
        edtUsername = findViewById(R.id.edt_username)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtConfirmPass = findViewById(R.id.edt_confirm_pass)

        auth = Firebase.auth

        textTogoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


        btnSignUp.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmpass = edtConfirmPass.text.toString()
            val username = edtUsername.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpass) || TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmpass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password == confirmpass) {
                SignUp(username, email, password)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }


            // After successful sign-up, create a user path in Firebase
            val userId = auth.currentUser?.uid
            userId?.let { uid ->
                val userRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)
                val user = User(username, email, "", listOf(), uid)
                userRef.setValue(user)

                userRef.child("userName").setValue(username)

            }
        }



    }




    fun SignUp( username:String,  email:String, password : String) {

        if (TextUtils.isEmpty(edtEmail.text.toString())) {
            edtEmail.setError("please enter Email")
            return
        } else if (TextUtils.isEmpty(edtPassword.text.toString())) {
            edtPassword.setError("please enter Password")
            return
        }



        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                    // Sign in success, update UI with the signed-in user's information
                    // After successful sign-up, create a user path in Firebase
                    val userId = auth.currentUser?.uid
                    userId?.let { uid ->
                        val userRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)
                        val user = User(username, email, "", listOf(), uid)
                        userRef.setValue(user)

                        userRef.child("userName").setValue(username)

                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Sign up failed. Please try again later", Toast.LENGTH_SHORT).show()


                }
        }


    }
}