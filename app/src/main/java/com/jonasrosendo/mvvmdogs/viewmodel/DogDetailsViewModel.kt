package com.jonasrosendo.mvvmdogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jonasrosendo.mvvmdogs.model.Dog
import com.jonasrosendo.mvvmdogs.model.DogDatabase
import kotlinx.coroutines.launch

class DogDetailsViewModel(application: Application) : BaseViewModel(application) {

    var dogLiveData = MutableLiveData<Dog>()

    fun fetch(dogUuid: Int){
        launch {
            dogLiveData.value = DogDatabase(getApplication()).dogDao().getDogByUuid(dogUuid)
        }
    }
}