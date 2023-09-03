package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_food")
data class Food(

    @PrimaryKey(autoGenerate = true)
    val id : Int ? = null,

    val txtName : String,
    val txtPrice : String,
    val txtDistance : String,
    val txtCity : String,

//    @ColumnInfo(name = "url")  نام دادن
    val urlImage : String,
    val numberRating : Int,
    val rating : Float

)
