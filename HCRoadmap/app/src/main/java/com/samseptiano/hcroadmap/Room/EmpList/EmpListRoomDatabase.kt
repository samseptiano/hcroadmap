package com.samseptiano.hcroadmap.Room.EmpList

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Database annotation to specify the entities and set version
@Database(entities = [EmpListRoom::class], version = 1, exportSchema = false)
abstract class EmpListRoomDatabase : RoomDatabase() {


    companion object {
        @Volatile
        private var INSTANCE: EmpListRoomDatabase? = null

        val DB_NAME:String = "emplist_db"

        fun getDatabase(context: Context): EmpListRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EmpListRoomDatabase::class.java,
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

    abstract fun getEmpListDao() : EmpListDao
}