package com.example.spring_mvvm_rxjava_livedata.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spring_mvvm_rxjava_livedata.model.Student

@Dao
interface StudentDao {

    @Query("SELECT * FROM student")
    fun getAllData(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(students: List<Student>)

    @Query("DELETE FROM student WHERE name = :studentName")
    fun remove(studentName: String)


}