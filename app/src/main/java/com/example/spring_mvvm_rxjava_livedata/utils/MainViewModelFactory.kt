package com.example.spring_mvvm_rxjava_livedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spring_mvvm_rxjava_livedata.addStudent.AddStudentViewModel
import com.example.spring_mvvm_rxjava_livedata.mainScreen.MainViewModel
import com.example.spring_mvvm_rxjava_livedata.model.MainRepository

class MainViewModelFactory (private val mainRepository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mainRepository) as T
    }

}

class AddStudentViewModelFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddStudentViewModel(mainRepository) as T
    }

}