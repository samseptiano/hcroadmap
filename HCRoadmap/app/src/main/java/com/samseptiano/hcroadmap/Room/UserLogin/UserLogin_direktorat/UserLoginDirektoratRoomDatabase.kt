package com.samseptiano.hcroadmap.Room.UserLogin.UserLogin_direktorat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database annotation to specify the entities and set version
@Database(entities = [UserLoginDirektoratRoom::class], version = 1, exportSchema = false)
abstract class UserLoginDirektoratRoomDatabase : RoomDatabase() {


    companion object {
        @Volatile
        private var INSTANCE: UserLoginDirektoratRoomDatabase? = null

        val DB_NAME:String = "userlogin_db"

        fun getDatabase(context: Context): UserLoginDirektoratRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserLoginDirektoratRoomDatabase::class.java,
                    DB_NAME
                )
                    .allowMainThreadQueries() //allows Room to executing task in main thread
                    .fallbackToDestructiveMigration() //allows Room to recreate database if no migrations found
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }

    abstract fun getUserListDirektoratDao() : UserloginDirektoratDao
}