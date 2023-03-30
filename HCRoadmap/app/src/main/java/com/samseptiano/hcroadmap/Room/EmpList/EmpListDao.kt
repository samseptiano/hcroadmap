package com.samseptiano.hcroadmap.Room.EmpList

import androidx.room.*

@Dao
interface EmpListDao {

    @Insert
    fun insert(empListRoom : EmpListRoom)

    @Update
    fun update(empListRoom: EmpListRoom)

    @Delete
    fun delete(empListRoom: EmpListRoom)

    @Query("SELECT * FROM empList ")
    fun getAll() : List<EmpListRoom>

    @Query("SELECT * FROM empList WHERE id = :id")
    fun getById(id: Int) : List<EmpListRoom>
}