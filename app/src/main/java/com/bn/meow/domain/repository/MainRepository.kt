package com.bn.meow.domain.repository

import com.bn.meow.data.models.CatsResponseItem
import com.bn.meow.data.network.Resource

interface MainRepository {
    suspend fun getCats(limit:Int): Resource<ArrayList<CatsResponseItem>>
}