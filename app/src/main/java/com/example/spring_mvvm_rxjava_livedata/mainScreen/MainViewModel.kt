package com.example.spring_mvvm_rxjava_livedata.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.spring_mvvm_rxjava_livedata.model.MainRepository
import com.example.spring_mvvm_rxjava_livedata.model.Student
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val mainRepository: MainRepository):ViewModel() {








    private lateinit var netDisposable : Disposable
    private val errorData = MutableLiveData<String>()

    init {

        mainRepository
            .refreshData()
            .subscribeOn(Schedulers.io())
            .subscribe( object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    netDisposable = d
                }

                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    errorData.postValue( e.message ?: "unknown error!" )
                }

            } )

    }

    fun getAllData() : LiveData<List<Student>> {
        return mainRepository.getAllStudent()
    }
    fun getErrorData() :LiveData<String> {
        return errorData
    }
    fun removeStudent(studentName :String) : Completable {
        return mainRepository.removeStudent(studentName)
    }

    override fun onCleared() {
        netDisposable.dispose()
        super.onCleared()
    }

}