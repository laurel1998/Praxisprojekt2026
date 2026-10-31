package de.praxisprojekt.backend.services

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.http.client.JdkClientHttpRequestFactory
import java.net.http.HttpClient

@Component
class SentimentClient {

    private val restClient = RestClient.builder()
    .baseUrl("http://localhost:8000")
    .requestFactory(
        JdkClientHttpRequestFactory(
            HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build()
        )
    )
    .build()
        
    data class SentimentResult(
        val sentiment: String,
        val confidence: Double
    )

    fun analyze(text: String): SentimentResult {
        return restClient.post()
            .uri("/sentiment")
            .contentType(MediaType.APPLICATION_JSON)
            .body(mapOf("text" to text))
            .retrieve()
            .body(SentimentResult::class.java)
            ?: error("Sentiment-Dienst hat keine Antwort zurückgegeben.")
    }
    
}
