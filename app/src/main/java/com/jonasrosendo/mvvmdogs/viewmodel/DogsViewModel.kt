package com.jonasrosendo.mvvmdogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.jonasrosendo.mvvmdogs.model.Dog
import com.jonasrosendo.mvvmdogs.model.DogDatabase
import com.jonasrosendo.mvvmdogs.model.DogsApiService
import com.jonasrosendo.mvvmdogs.util.NotificationsHelper
import com.jonasrosendo.mvvmdogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class DogsViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    private val dogsApiService = DogsApiService() //api instance
    private val disposable = CompositeDisposable() //allows us to observe the observable that api sent us


    val dogs = MutableLiveData<List<Dog>>()
    val hasError = MutableLiveData<Boolean>() //if any error returns
    val isLoading = MutableLiveData<Boolean>() //if isLoading

    fun refresh(){
        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        }else{
            fetchFromApi()// retrieve data from API
        }
    }

    fun refreshBypassCache(){
        fetchFromApi()
    }

    private fun fetchFromDatabase(){
        isLoading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchFromApi(){
        isLoading.value = true
        disposable.add(
            dogsApiService.getDogs()
                .subscribeOn(Schedulers.newThread())//process happens on a background thread
                .observeOn(AndroidSchedulers.mainThread())//send the result to main thread
                .subscribeWith(object : DisposableSingleObserver<List<Dog>>() {
                    override fun onSuccess(dogsList: List<Dog>) {
                        storeDogsLocally(dogsList)
                        NotificationsHelper(getApplication()).createNotification()
                        Toast.makeText(getApplication(), "Dogs retrieved from web", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        hasError.value = true
                        isLoading.value = false
                        e.printStackTrace()
                    }
                })//observer that we want to observe
        )
    }

    private fun dogsRetrieved(dogsList : List<Dog>){
        dogs.value = dogsList
        hasError.value = false
        isLoading.value = false
    }

    private fun storeDogsLocally(list: List<Dog>){
        launch {
            val dogDao = DogDatabase(getApplication()).dogDao()
            dogDao.deleteAllDogs()
            val result = dogDao.insertAll(*list.toTypedArray())

            list.forEachIndexed { index, dog ->
                dog.uuid = result[index].toInt()
            }
            dogsRetrieved(list)
        }

        prefHelper.saveUpdateTime(System.nanoTime())

    }
    //clear data after view is destroyed
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}