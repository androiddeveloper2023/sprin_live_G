package com.example.spring_mvvm_rxjava_livedata.model

import androidx.lifecycle.LiveData
import com.example.spring_mvvm_rxjava_livedata.utils.studentToJsonObject
import io.reactivex.Completable

class MainRepository(
    private val apiService: ApiService,
    private val studentDao: StudentDao
)  {
    fun getAllStudent(): LiveData<List<Student>> {
        return studentDao.getAllData()
    }

    // caching
    fun refreshData(): Completable {
        return apiService
            .getAllStudents()
            .doOnSuccess {
                studentDao.insertAll(it)
            }
            .ignoreElement()
    }

    fun insertStudent(student: Student): Completable {
        return apiService
            .insertStudent( studentToJsonObject(student) )
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun updateStudent(student: Student): Completable {
        return apiService
            .updateStudent(student.name, studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun removeStudent(studentName: String): Completable {
        return apiService
            .deleteStudent(studentName)
            .doOnComplete {
                studentDao.remove(studentName)
            }
    }

}