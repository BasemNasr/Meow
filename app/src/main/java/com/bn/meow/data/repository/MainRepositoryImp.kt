package com.bn.meow.data.repository

import com.bn.meow.data.models.CatsResponseItem
import com.bn.meow.data.network.Resource
import com.bn.meow.data.network.Urls
import com.bn.meow.domain.repository.MainRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MainRepositoryImp(private val httpClient: HttpClient) : MainRepository {
    override suspend fun getCats(limit: Int): Resource<ArrayList<CatsResponseItem>> {
        val response = httpClient.get(Urls.GET_CATS) {
            headers.append(
                "x-api-key",
                Urls.API_KEY
            )
            parameter("limit", "30")

        }.body<ArrayList<CatsResponseItem>>()

        return try {
            Resource.Success(
                response
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
}