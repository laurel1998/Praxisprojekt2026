package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.models.YoutubeChannel
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import tools.jackson.databind.ObjectMapper
import java.text.SimpleDateFormat
import java.util.Date
import java.time.Instant

@Service
class YoutubeService {

    private val restClient = RestClient.create()
    private val objectMapper = ObjectMapper()
    
    private val apiKey = System.getenv("YOUTUBE_API_KEY")
        ?: throw IllegalStateException("YOUTUBE_API_KEY ist nicht gesetzt")

    private fun getTestScore(commentId: String): Int {
        
        val digits = commentId.filter { it.isDigit() }.toSet()

        return when {
            '7' in digits -> 2
            digits.size > 1 -> 1
            else -> 0
        }
    }
    
    fun getComments(videoId: String): List<Kommentar> {

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
                    totalReplyCount = snippet["totalReplyCount"].asInt(),
                    testScore = getTestScore(comment["id"].asText())
                )
            )
        }
        return comments
    }
    
    fun getComment(videoId: String, commentId: String): Kommentar {

        val response = restClient.get()
            .uri("https://www.googleapis.com/youtube/v3/comments") {
                it.queryParam("part", "snippet")
                    .queryParam("id", commentId)
                    .queryParam("key", apiKey)
                    .build()
            }
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("Keine Antwort von der YouTube API")

        println(response)

        val item = objectMapper.readTree(response)["items"][0]
        val snippet = item["snippet"]

        return Kommentar(
            id = item["id"].asText(),
            videoId = videoId,
            authorChannelId = snippet["authorChannelId"]["value"].asText(),
            text = snippet["textOriginal"].asText(),
            likeCount = snippet["likeCount"].asInt(),
            publishedAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .parse(snippet["publishedAt"].asText()),
            totalReplyCount = 0,
            testScore = getTestScore(item["id"].asText())
        )
    }
    
    fun getChannel(channelId: String): YoutubeChannel {

        val response = restClient.get()
            .uri("https://www.googleapis.com/youtube/v3/channels") {
                it.queryParam("part", "snippet,statistics")
                    .queryParam("id", channelId)
                    .queryParam("key", apiKey)
                    .build()
            }
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("Keine Antwort von der YouTube API")

        val item = objectMapper.readTree(response)["items"][0]

        val publishedAt = Date.from(
            Instant.parse(item["snippet"]["publishedAt"].asText())
        )

        val videoCount = item["statistics"]["videoCount"].asInt()
        
        val subscriberCount = item["statistics"]["subscriberCount"].asLong()
        
        val description = item["snippet"]["description"].asText()

        return YoutubeChannel(
            publishedAt = publishedAt,
            videoCount = videoCount,
            subscriberCount = subscriberCount,
            description = description
        )
    }
    
    fun getVideoViewCount(videoId: String): Long {

        val response = restClient.get()
            .uri("https://www.googleapis.com/youtube/v3/videos") {
                it.queryParam("part", "statistics")
                    .queryParam("id", videoId)
                    .queryParam("key", apiKey)
                    .build()
            }
            .retrieve()
            .body(String::class.java)
            ?: throw IllegalStateException("Keine Antwort von der YouTube API")

        val item = objectMapper.readTree(response)["items"][0]

        return item["statistics"]["viewCount"].asLong()
    }
}