package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.Kommentar
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import tools.jackson.databind.ObjectMapper
import java.text.SimpleDateFormat

@Service
class YouTubeService {

    private val restClient = RestClient.create()
    private val objectMapper = ObjectMapper()

    fun getComments(videoId: String): List<Kommentar> {

        val apiKey = System.getenv("YOUTUBE_API_KEY")
            ?: throw IllegalStateException("YOUTUBE_API_KEY ist nicht gesetzt")

        val response = restClient.get()
            .uri("https://www.googleapis.com/youtube/v3/commentThreads") {
                it.queryParam("part", "snippet")
                    .queryParam("videoId", videoId)
                    .queryParam("key", apiKey)
                    .queryParam("maxResults", 10)
                    .build()
            }
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("Keine Antwort von der YouTube API")

        val items = objectMapper.readTree(response)["items"]
        val comments = mutableListOf<Kommentar>()

        for (item in items) {
            val snippet = item["snippet"]
            val comment = snippet["topLevelComment"]
            val commentSnippet = comment["snippet"]

            comments.add(
                Kommentar(
                    id = comment["id"].asText(),
                    videoId = videoId,
                    authorChannelId = commentSnippet["authorChannelId"]["value"].asText(),
                    text = commentSnippet["textOriginal"].asText(),
                    likeCount = commentSnippet["likeCount"].asInt(),
                    publishedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .parse(commentSnippet["publishedAt"].asText()),
                    totalReplyCount = snippet["totalReplyCount"].asInt()
                )
            )
        }
        return comments
    }
}