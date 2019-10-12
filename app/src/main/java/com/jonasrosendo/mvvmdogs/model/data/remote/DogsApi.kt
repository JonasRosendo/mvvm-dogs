package com.jonasrosendo.mvvmdogs.model.data.remote

import com.jonasrosendo.mvvmdogs.model.model.Dog
import io.reactivex.Single
import retrofit2.http.GET

interface DogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<Dog>>
}