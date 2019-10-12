package com.jonasrosendo.mvvmdogs.model.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jonasrosendo.mvvmdogs.model.model.Dog

@Database(entities = [Dog::class], version = 1, exportSchema = true)
abstract class DogDatabase : RoomDatabase(){

    abstract fun dogDao() : DogRoomDao

    companion object {
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
            ?: synchronized(LOCK) {
            instance
                ?: buildDatabase(
                    context
                ).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "dogdatabase"
        ).build()
    }
}