package com.monday8am.baseapp.domain.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserLocation(
    val id: String = UUID.randomUUID().toString(),
    val longitude: Double,
    val latitude: Double,
    val imageUrl: String? = null,
    val takenAt: Long = System.currentTimeMillis()
)

@Serializable
data class PhotosSearchResponse(val photos: PhotoContainer?, val stat: String?)

@Serializable
data class PhotoContainer(
    val page: Int?,
    val pages: Int?,
    val perpage: Int?,
    val total: Int?,
    val photo: List<FlickrPhoto>?
)

@Serializable
data class FlickrPhoto(
    val id: String?,
    val owner: String?,
    val secret: String?,
    val server: String?,
    val farm: Int?,
    val title: String?,
    val ispublic: Int?,
    val isfriend: Int?,
    val isfamily: Int?,
    val url_c: String?,
    val height_c: String?,
    val width_c: String?
)
