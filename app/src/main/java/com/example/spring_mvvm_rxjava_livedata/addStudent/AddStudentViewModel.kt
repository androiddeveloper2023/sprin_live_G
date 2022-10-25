package com.example.spring_mvvm_rxjava_livedata.addStudent

import androidx.lifecycle.ViewModel
import com.example.spring_mvvm_rxjava_livedata.model.MainRepository
import com.example.spring_mvvm_rxjava_livedata.model.Student
import io.reactivex.Completable

class AddStudentViewModel(private val mainRepository: MainRepository) : ViewModel() {

        fun insertNewStudent(student: Student): Completable {
            return mainRepository.insertStudent(student)
        }

        fun updateStudent(student: Student): Completable {
            return mainRepository.updateStudent(student)
        }



}