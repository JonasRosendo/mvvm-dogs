package com.jonasrosendo.mvvmdogs.model.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "dogs")
data class Dog(
    @ColumnInfo(name = "id")
    @SerializedName(value = "id")
    val dogId: String?,

    @ColumnInfo(name = "name")
    @SerializedName(value = "name")
    val dogName: String?,

    @ColumnInfo(name = "life_span")
    @SerializedName(value = "life_span")
    val lifespan: String?,

    @ColumnInfo(name = "bred_for")
    @SerializedName(value = "bred_for")
    val dogFor: String?,

    @ColumnInfo(name = "breed_group")
    @SerializedName(value = "breed_group")
    val dogGroup: String?,

    @ColumnInfo(name = "temperament")
    @SerializedName(value = "temperament")
    val temperament: String?,

    @ColumnInfo(name = "url")
    @SerializedName(value = "url")
    val imageUrl: String?
){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}

data class DogPalette(var color : Int)