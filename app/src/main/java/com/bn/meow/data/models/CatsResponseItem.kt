package com.bn.meow.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CatsResponseItem(
    val breeds: List<Breed>?=null,
    val categories: List<Category>?=null,
    val height: Int?=null,
    val id: String?=null,
    val url: String?=null,
    val width: Int?=null,
    val isLike: Boolean?=false,
)