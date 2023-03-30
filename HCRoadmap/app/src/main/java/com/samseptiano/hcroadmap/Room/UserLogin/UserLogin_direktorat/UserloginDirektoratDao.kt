package com.samseptiano.hcroadmap.Room.UserLogin.UserLogin_direktorat
import androidx.room.*

@Dao
interface UserloginDirektoratDao {

    @Insert
    fun insert(userLoginDirektoratRoom: UserLoginDirektoratRoom)

    @Update
    fun update(userLoginDirektoratRoom: UserLoginDirektoratRoom)

    @Delete
    fun delete(userLoginDirektoratRoom: UserLoginDirektoratRoom)

    @Query("SELECT * FROM userLoginDirektorat ")
    fun getAll() : List<UserLoginDirektoratRoom>

    @Query("SELECT * FROM userLoginDirektorat WHERE id = :id")
    fun getById(id: Int) : List<UserLoginDirektoratRoom>
}