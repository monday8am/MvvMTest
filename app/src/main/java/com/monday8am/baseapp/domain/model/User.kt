package com.monday8am.baseapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val position: String,
    val platform: String,
    val pic: String,
)
