package com.monday8am.baseapp.data.remote

import com.monday8am.baseapp.domain.model.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

val flickrUrl = "https://api.flickr.com"

interface FlickrClient {

    @GET("/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_c")
    suspend fun listPhotos(
        @Query("api_key") apiKey: String,
        @Query("lon") longitude: Double,
        @Query("lat") latitude: Double,
        @Query("radius") radius: Float = 0.1f,
        @Query("per_page") maxNumberOfPhotos: Int = 1
    ): PhotosSearchResponse
}
