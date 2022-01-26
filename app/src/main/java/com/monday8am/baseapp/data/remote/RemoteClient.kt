package com.monday8am.baseapp.data.remote

import com.monday8am.baseapp.domain.model.User
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Path

interface RemoteClient {

    @GET("mojo/team.json")
    suspend fun getUsers(): List<User>

    @HTTP(method = "DELETE", path = "v2/profile/favorites/{itemId}")
    suspend fun removeFavorite(@Path("itemId") id: String)

    //endregion
}
