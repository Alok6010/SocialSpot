package com.example.socialspot.DataClass

data class User(val userName : String = "",
                val userEmail : String = "",
                val userProfileImage : String = "",
                val listOfuserPhotos : List<String> = listOf(),
                val uid : String = "",)
