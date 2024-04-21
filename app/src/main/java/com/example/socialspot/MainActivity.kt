package com.example.socialspot

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.socialspot.Adapter.ImageAdapter
import com.example.socialspot.DataClass.Image
import com.example.socialspot.HomePage.LoginActivity
import com.example.socialspot.HomePage.SignUpActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var fablike: MaterialButton
    private lateinit var fabdislike: MaterialButton
    private lateinit var fabUpload: MaterialButton
    private lateinit var imageAdapter: ImageAdapter
    var listofImages: MutableList<Image> = mutableListOf()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        auth = FirebaseAuth.getInstance()

        rv = findViewById(R.id.rv)
        fablike = findViewById(R.id.fab_like)
        fabdislike = findViewById(R.id.fab_dislike)
        fabUpload = findViewById(R.id.fab_upload)


        fabUpload.setOnClickListener(){
            //Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,UploadImageActivity::class.java))

        }


        //code to get data from database and upload it
        FirebaseDatabase.getInstance().getReference("photos")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    listofImages.clear()
                    for (dataSnapshot in snapshot.children){
                        val photo= dataSnapshot.getValue(Image::class.java)

                        listofImages.add(photo!!)
                    }


//                     Inside onDataChange after updating listofImages
//                    ImageAdapter.notifyDataSetChanged()


                    //code to show one ss image at a time when we swipe ,completely goes to second activity
                    val snapHelper : SnapHelper = LinearSnapHelper()
                    snapHelper.attachToRecyclerView(rv)

                    imageAdapter = ImageAdapter(this@MainActivity, listofImages)
                    rv.layoutManager = LinearLayoutManager(this@MainActivity)
                    rv.adapter = imageAdapter



                }


                override fun onCancelled(error: DatabaseError) {

                }
            })



    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_search -> {
                Toast.makeText(this, "search clicked", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menu_chat -> {
                Toast.makeText(this, "search clicked", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this,ChatListActivity::class.java))
                return true
            }

            R.id.menu_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                return true
            }

            R.id.menu_setting -> {
                Toast.makeText(this, "clik", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(this, SettingActivity::class.java))
                return true
            }

            R.id.menu_Share -> {
                Toast.makeText(this, "clik", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menu_rateus -> {
                Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menu_logout -> {

                auth.signOut()
                FirebaseAuth.getInstance().signOut()

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }


}