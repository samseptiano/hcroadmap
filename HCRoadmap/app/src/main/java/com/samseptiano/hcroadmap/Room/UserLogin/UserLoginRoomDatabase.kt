package com.samseptiano.hcroadmap.Room.UserLogin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database annotation to specify the entities and set version
@Database(entities = [UserLoginRoom::class,ModuleRoom::class], version = 1, exportSchema = false)
abstract class UserLoginRoomDatabase : RoomDatabase() {


    companion object {
        @Volatile
        private var INSTANCE: UserLoginRoomDatabase? = null

        val DB_NAME:String = "userlogin_db"

        fun getDatabase(context: Context): UserLoginRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserLoginRoomDatabase::class.java,
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

    abstract fun getUserLoginDao() : UserloginDao
}