package com.jonasrosendo.mvvmdogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jonasrosendo.mvvmdogs.model.model.Dog
import com.jonasrosendo.mvvmdogs.model.data.local.DogDatabase
import kotlinx.coroutines.launch

class DogDetailsViewModel(application: Application) : BaseViewModel(application) {

    var dogLiveData = MutableLiveData<Dog>()

    fun fetch(dogUuid: Int){
        launch {
            dogLiveData.value = DogDatabase(
                getApplication()
            ).dogDao().getDogByUuid(dogUuid)
        }
    }
}