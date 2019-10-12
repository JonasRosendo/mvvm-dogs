package com.jonasrosendo.mvvmdogs.model.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jonasrosendo.mvvmdogs.model.model.Dog

@Dao
interface DogRoomDao {

    @Insert
    suspend fun insertAll(vararg dogs: Dog) : List<Long>

    @Query("SELECT * FROM dogs")
    suspend fun getAllDogs() : List<Dog>

    @Query("SELECT * FROM dogs WHERE uuid = :dogUuid")
    suspend fun getDogByUuid(dogUuid: Int) : Dog

    @Query("DELETE FROM dogs")
    suspend fun deleteAllDogs()
}