package com.monday8am.baseapp.data.remote

import com.monday8am.baseapp.domain.model.User
import retrofit2.http.GET

interface RemoteClient {

    @GET("mojo/team.json")
    suspend fun getUsers(): List<User>
}
