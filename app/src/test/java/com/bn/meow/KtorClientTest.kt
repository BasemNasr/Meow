package com.bn.meow

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import io.kotest.matchers.shouldBe


class KtorClientTest {

    @Test
    fun `ktor client and serialization work correctly`(): Unit = runBlocking {
        val response: HttpResponse = mockHttpClient.get("https://api.thecatapi.com/v1/images/search")
        response.status shouldBe HttpStatusCode.OK
    }
}