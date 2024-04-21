package com.example.socialspot

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.Images.Media
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.socialspot.DataClass.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class UploadImageActivity : AppCompatActivity() {

    private lateinit var edtDescription: EditText
    private lateinit var previewImage: ImageView
    private lateinit var btnselectImage: Button
    private lateinit var btnuploadImage: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload_image)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        edtDescription = findViewById(R.id.edt_description)
        previewImage = findViewById(R.id.Image_preview)
        btnselectImage = findViewById(R.id.btn_Select_Image)
        btnuploadImage = findViewById(R.id.btn_Upload_Image)
        progressBar = findViewById(R.id.progress_Bar)

        btnselectImage.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,101)

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK){
            val uri = data?.data
            previewImage.setImageURI(uri)

            btnuploadImage.setOnClickListener {
                //image code to upload image
                //to get image link
                progressBar.visibility = android.view.View.VISIBLE
                val Description = edtDescription.text.toString()

                val fileName = UUID.randomUUID().toString()+".jpg"
                val storageRef = FirebaseStorage.getInstance().reference.child("personPhoto/$fileName")
                storageRef.putFile(uri!!).addOnSuccessListener {
                    val result = it.metadata?.reference?.downloadUrl
                    result?.addOnSuccessListener {
                        Log.i("abcd",it.toString())

                        uploadImage(
                            Description,
                            it.toString()
                        )
                        // Remove ProgressBar on UI thread after the upload is complete
                        runOnUiThread {
                            progressBar.visibility = android.view.View.GONE
                        }

                    }
                }
            }

        }

    }

    private fun uploadImage(Decription:String, link: String){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        Log.d("UploadPhotoActivity", "User ID: $userId") // Log the userId

        userId?.let {
            Firebase.database.getReference("photos").push().setValue(
                Image(
                    userId = it,
                    description = Decription,
                    personPhoto = link
                )
            ).addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Product is successfully uploaded", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener { exception ->
                progressBar.visibility = View.GONE
                Log.e("UploadPhotoActivity", "Error uploading photo: $exception")
                Toast.makeText(this, "Failed to upload product", Toast.LENGTH_SHORT).show()
            }
        }
    }





}