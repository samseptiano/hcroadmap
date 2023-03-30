package com.samseptiano.hcroadmap.Room.UserLogin
import androidx.room.*

@Dao
interface UserloginDao {

    @Insert
    fun insert(userLoginRoom: UserLoginRoom)

    @Insert
    fun insertModule(moduleRoom: ModuleRoom)

    @Update
    fun update(userLoginRoom: UserLoginRoom)

    @Delete
    fun delete(userLoginRoom: UserLoginRoom)

    @Query("DELETE FROM userLogin ")
    fun deleteAll() : Unit

    @Query("DELETE FROM module ")
    fun deleteAllModule() : Unit

    @Query("SELECT * FROM userLogin ")
    fun getAll() : List<UserLoginRoom>

    @Query("SELECT * FROM module ")
    fun getAllModule() : List<ModuleRoom>


    @Query("SELECT token FROM userLogin ")
    fun getToken() : String

    @Query("SELECT emp_compgrpid FROM userLogin ")
    fun getCompGrpId() : String



    @Query("SELECT emp_nik FROM userLogin ")
    fun getNIK() : String

    @Query("SELECT username FROM userLogin ")
    fun getusername() : String

    @Query("SELECT * FROM userLogin WHERE id = :id")
    fun getById(id: Int) : List<UserLoginRoom>
}