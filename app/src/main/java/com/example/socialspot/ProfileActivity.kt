package com.example.socialspot

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileimage: CircleImageView
    private lateinit var userName : TextView
    private lateinit var aboutYourself: EditText
    private lateinit var userBio : EditText
    private lateinit var sharedPreferences:SharedPreferences


    //user photo
    private lateinit var rv: RecyclerView
    //private lateinit var userphotosAdapter: UserPhotosAdapter
    //val listOfuserPhotos: MutableList<UserPhotos> = mutableListOf()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        userBio = findViewById(R.id.user_bio)
        aboutYourself = findViewById(R.id.about_yourself)
        userName = findViewById(R.id.user_name)


        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val bio = snapshot.child("bio").getValue(String::class.java) ?: ""
                    val aboutYourselfText = snapshot.child("aboutYourself").getValue(String::class.java) ?: ""

                    val username = snapshot.child("userName").getValue(String::class.java) ?: ""
                    // Set the fetched username to the TextView
                    userName.text = username

                    // Set fetched data to the corresponding views
                    userBio.setText(bio)
                    aboutYourself.setText(aboutYourselfText)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Load previously saved bio data
        userBio.setText(sharedPreferences.getString("bio", ""))
        aboutYourself.setText(sharedPreferences.getString("aboutYourself",""))


        val addimage: Button = findViewById(R.id.add_Image)
        init()
        //inita()

        profileimage = findViewById(R.id.profile_image)
        rv = findViewById(R.id.rv)


        addimage.setOnClickListener {
            // Handle button click here
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this,UploadUserPhotosActivity::class.java))

        }


        profileimage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,100)
        }


        aboutYourself = findViewById(R.id.about_yourself)

        aboutYourself.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // Get the text entered in the EditText
                val inputText = s.toString()

                // Count the number of words
                val words = inputText.trim().split("\\s+".toRegex())
                val wordCount = words.size

                // Enforce the word limit (e.g., 50 words)
                val wordLimit = 50
                if (wordCount > wordLimit) {
                    // If the word count exceeds the limit, trim the excess words
                    val trimmedText = inputText.split("\\s+".toRegex(), wordLimit)
                        .joinToString(" ")

                    // Update the EditText with trimmed text
                    aboutYourself.setText(trimmedText)
                    aboutYourself.setSelection(trimmedText.length)

                    // Show a toast message to inform the user about the word limit
                    Toast.makeText(
                        this@ProfileActivity,
                        "Maximum $wordLimit words allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        })

        userBio = findViewById(R.id.user_bio)
        userBio.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Check if the touch event occurred on the drawable end (downward arrow icon)
                if (event.rawX >= userBio.right - userBio.compoundDrawables[2].bounds.width()) {
                    // Show the options here
                    showOptions()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }



//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        userId?.let { uid ->
//            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)
//            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val username = snapshot.child("userName").getValue(String::class.java) ?: ""
//                    // Set the fetched username to the TextView
//                    userName.text = username
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    // Handle error
//                }
//            })
//        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100 && resultCode == RESULT_OK){
            profileimage.setImageURI(data?.data)
            uploadProfileImage(data?.data)
        }
    }

    private fun uploadProfileImage(uri: Uri?){


        val profileImageName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().getReference().child("profileImages/$profileImageName")

        storageRef.putFile(uri!!).addOnSuccessListener {
        val result  = it.metadata?.reference?.downloadUrl
            result?.addOnSuccessListener {
                FirebaseDatabase.getInstance().reference.child("users").child(Firebase.auth.uid.toString())
                    .child("userProfileImage").setValue(it.toString())
            }
        }
    }

    private fun init(){

        profileimage = findViewById(R.id.profile_image)
        userName = findViewById(R.id.user_name)

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val userId = user.uid
            FirebaseDatabase.getInstance().reference.child("users").child(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val link = snapshot.child("userProfileImage").value.toString()
                        val username = snapshot.child("userName").value.toString()
                        if (!link.isNullOrBlank()){
                            Glide.with(this@ProfileActivity)
                                .load(link)
                                .into(profileimage)
                        } else {
                            profileimage.setImageResource(R.drawable.ic_launcher_background)
                        }
                        userName.text = username
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })
        }
    }


//    private fun inita(){
//        val userPhotosRef = FirebaseDatabase.getInstance().reference.child("users")
//            .child(Firebase.auth.uid.toString()).child("listOfUserPhotos")
//
//        userPhotosRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val userPhotos = snapshot.getValue<List<String>>() ?: emptyList()
//
//                // Load user photos into the RecyclerView
//                listOfuserPhotos.clear()
//                for (photoUrl in userPhotos) {
//                    listOfuserPhotos.add(UserPhotos(personPhoto = photoUrl))
//                }
//
//                // Setup RecyclerView adapter and layout manager
//                userphotosAdapter = UserPhotosAdapter(listOfuserPhotos, this@ProfileActivity)
//                rv.layoutManager = GridLayoutManager(this@ProfileActivity,1)
//                rv.adapter = userphotosAdapter
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle error
//            }
//        })
//    }



    private fun showOptions() {
        val popupMenu = PopupMenu(this, userBio)
        popupMenu.menuInflater.inflate(R.menu.bio_option_menu, popupMenu.menu)

        // Set menu item click listener
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.option_1 -> {
                    // Handle selection of "Ankit" option
                    userBio.setText("Available")
                    true
                }
                R.id.option_2 -> {
                    // Handle selection of "Alok" option
                    userBio.setText("Busy")
                    true
                }
                R.id.option_3 -> {
                    // Handle selection of "Aditya" option
                    userBio.setText("Sleeping")
                    true
                }
                R.id.option_4 -> {
                    // Handle selection of "Vikas" option
                    userBio.setText("Only MiLo")
                    true
                }
                R.id.option_5 -> {
                    // Handle selection of "Ankit" option
                    userBio.setText("Studying")
                    true
                }
                R.id.option_6 -> {
                    // Handle selection of "Alok" option
                    userBio.setText("No Calls Only MiLo")
                    true
                }
                R.id.option_7 -> {
                    // Handle selection of "Aditya" option
                    userBio.setText("Travelling")
                    true
                }
                R.id.option_8 -> {
                    // Handle selection of "Vikas" option
                    userBio.setText("Chill Life")
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()
    }


    override fun onPause() {
        super.onPause()

        // Save the bio data when the activity is paused
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {

            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
            userRef.child("bio").setValue(userBio.text.toString())
            userRef.child("aboutYourself").setValue(aboutYourself.text.toString())
//            //val editor = sharedPreferences.edit()
//            editor.putString("bio", userBio.text.toString())
//            editor.putString("aboutYourself", aboutYourself.text.toString())
////        editor.apply()
        }
    }








}

