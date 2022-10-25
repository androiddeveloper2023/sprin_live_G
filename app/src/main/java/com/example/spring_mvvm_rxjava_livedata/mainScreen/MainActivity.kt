package com.example.spring_mvvm_rxjava_livedata.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.spring_mvvm_rxjava_livedata.*
import com.example.spring_mvvm_rxjava_livedata.addStudent.AddStudentActivity
import com.example.spring_mvvm_rxjava_livedata.databinding.ActivityMainBinding
import com.example.spring_mvvm_rxjava_livedata.databinding.DialogDeleteItemBinding
import com.example.spring_mvvm_rxjava_livedata.model.MainRepository
import com.example.spring_mvvm_rxjava_livedata.model.MyDatabase
import com.example.spring_mvvm_rxjava_livedata.model.Student
import com.example.spring_mvvm_rxjava_livedata.utils.ApiServiceSingleton
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

const val BASE_URL = "http://192.168.1.5:8080"

class MainActivity : AppCompatActivity() , StudentAdapter.StudentEvent{
    lateinit var binding: ActivityMainBinding

    lateinit var myAdapter:StudentAdapter
    private val compositeDisposable = CompositeDisposable()
    lateinit var mainScreenViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        initRecycler()

        mainScreenViewModel =  ViewModelProvider(
            this,
            MainViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(MainViewModel::class.java)
        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }
        mainScreenViewModel.getAllData().observe(this) {
            refreshRecyclerData(it)
        }

        mainScreenViewModel.getErrorData().observe(this) {
            Log.e("testLog", it)
        }


    }
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }




    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = AlertDialog.Builder(this).create()

        val dialogBinding = DialogDeleteItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()

        dialogBinding.btnNo.setOnClickListener{
            dialog.dismiss()
        }

        dialogBinding.btnDelete.setOnClickListener{
            deleteDataFromServer(student, position)
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun refreshRecyclerData(newData: List<Student>) {
        myAdapter.refreshData(newData)
    }

    private fun deleteDataFromServer(student: Student, position: Int) {

        mainScreenViewModel
            .removeStudent(student.name)
            . subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    Toast.makeText(this@MainActivity, "student removed :)", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {
                    Toast.makeText(this@MainActivity, "error -> " + e.message ?: "null", Toast.LENGTH_SHORT).show()                }

            })

myAdapter.removeItem(student,position)

    }

    private fun initRecycler() {
        val myData = arrayListOf<Student>()
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}