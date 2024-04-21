package com.example.socialspot.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialspot.ChatActivity
import com.example.socialspot.DataClass.Image
import com.example.socialspot.R


class ImageAdapter(
    private val context: android.content.Context,
    private val listofImages: MutableList<Image>
):RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: android.view.View):RecyclerView.ViewHolder(itemView){
        val personImg : ImageView = itemView.findViewById(R.id.person_image)
        val description : TextView = itemView.findViewById(R.id.Description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_layout,parent,false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        // Return the number of items in your data set
        return listofImages.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val currentPhoto = listofImages[position]
        holder.description.text = currentPhoto.description
        //image code is left for that we use glide
        Glide.with(context)
            .load(currentPhoto.personPhoto)
            .into(holder.personImg)



        holder.itemView.setOnClickListener(){

            val currentUser = listofImages[position]
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name",currentUser.personPhoto)
            intent.putExtra("uid",currentUser.userId)

            context.startActivity(intent)
        }



    }
}