package com.example.api

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

@JsonClass(generateAdapter = true)
data class PicoRequest(val prompt: String)

@JsonClass(generateAdapter = true)
data class PicoResponse(val status: String, val text: String? = null)

interface PicoApiService {
    @POST
    suspend fun generateContent(
        @Url url: String,
        @Body request: PicoRequest
    ): PicoResponse
}
