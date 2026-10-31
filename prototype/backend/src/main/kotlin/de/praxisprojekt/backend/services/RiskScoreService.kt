package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.RiskScore
import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.models.YoutubeChannel
import de.praxisprojekt.backend.services.YoutubeService
import de.praxisprojekt.backend.services.SentimentClient
import org.springframework.stereotype.Service
import java.util.Date

@Service
class RiskScoreService(
    private val sentimentClient: SentimentClient,
    private val youtubeService: YoutubeService
) {

    fun calculateRiskScore(
        channel: YoutubeChannel,
        viewCount: Long,
        currentComment: Kommentar,
        comments: List<Kommentar>
    ): RiskScore {
        var score = 0
        
        score += publicationRate(channel)
        score += reach(viewCount, channel.subscriberCount)
        score += channelDescription(channel)
        score += interactionIntensity(currentComment, comments)
        score += links(currentComment)
        score += ttr(currentComment)
        val sentimentPoints = sentiment(currentComment, comments)
        score += sentimentPoints
        score += responseSpeed(currentComment)
        score += textSimilarity(currentComment,comments)

        //Debug-Ausgabe
        println("Publication Rate: ${publicationRate(channel)}")
        println("Reach: ${reach(viewCount, channel.subscriberCount)}")
        println("Description: ${channelDescription(channel)}")
        println("Interaction Intensity: ${interactionIntensity(currentComment, comments)}")
        println("Links: ${links(currentComment)}")
        println("TTR: ${ttr(currentComment)}")
        println("Sentiment: $sentimentPoints")
        println("Antwortgeschwindigkeit: ${responseSpeed(currentComment)}")
        println("wiederholungsgrad: ${textSimilarity(currentComment,comments)}")
        println("Risk Score: $score")
        
        return RiskScore(
            score = score
        )
    }
    
    private fun publicationRate(channel: YoutubeChannel): Int {
        val now = Date()
        val channelAgeMillis = now.time - channel.publishedAt.time
        
        //Wochenanzahl zwischen Kanalerstellung und jetzt:
        val channelAgeWeeks = channelAgeMillis / (1000.0 * 60 * 60 * 24 * 7)

        val videosPerWeek = channel.videoCount / channelAgeWeeks

        return when {
            videosPerWeek <= 1 -> 0
            videosPerWeek <= 4 -> 1
            else -> 2
        }
    }
    
    private fun reach(viewCount: Long, subscriberCount: Long): Int {
        return when {
            viewCount < 1000 -> 0
            viewCount < 10000 && subscriberCount > 61 -> 0
            viewCount < 10000 && subscriberCount <= 61 -> 1
            viewCount >= 10000 && subscriberCount > 61 -> 1
            else -> 2
        }
    }
    
    private fun channelDescription(channel: YoutubeChannel): Int {
        return if (channel.description.isBlank()) {
            1
        } else {
            0
        }
    }
    
    private fun interactionIntensity(
        currentComment: Kommentar,
        comments: List<Kommentar>
    ): Int {

        //Zu bewertenden Kommentar aus der Vergleichsmenge entfernen
        val comparisonComments = comments.filter {
            it.id != currentComment.id
        }

        val averageLikes = comparisonComments
            .map { it.likeCount }
            .average()

        if (comparisonComments.isEmpty() || averageLikes <= 0) {
            return 0
        }

        val likeRatio = currentComment.likeCount / averageLikes

        return when {
            likeRatio < 3 -> 0
            likeRatio <= 10 -> 1
            else -> 2
        }
    }
    
    private fun links(comment: Kommentar): Int {

        //URLs mit http://, https:// oder www.
        val urlPattern = Regex("""(?:https?://|www\.)\S+""")

        val linkCount = urlPattern.findAll(comment.text).count()

        return when {
            linkCount == 0 -> 0
            linkCount == 1 -> 1
            else -> 2
        }
    }
    
    private fun ttr(comment: Kommentar): Int {
        
        // Regulärer Ausdruck: Folge aus Buchstaben und/oder Zahlen
        val words = Regex("""[\p{L}\p{N}]+""")
            .findAll(comment.text.lowercase())  // for case insensitivity
            .map { it.value }
            .toList()

        if (words.size < 10) {
            return 0
        }

        val ttr = words.toSet().size.toDouble() / words.size

        return when {
            ttr > 0.80 -> 0
            ttr >= 0.60 -> 1
            else -> 2
        }
    }
    
    private fun sentiment(
        currentComment: Kommentar,
        comments: List<Kommentar>
    ): Int {

        val currentSentiment = sentimentClient.analyze(currentComment.text).sentiment

        val referenceComments = comments
            .filter {
                it.id != currentComment.id && it.text.isNotBlank()
            }
            .take(20)

        if (referenceComments.isEmpty()) {
            println("Sentiment: Keine Referenzkommentare vorhanden.")
            return 0
        }

        val referenceSentiments = referenceComments.map {
            sentimentClient.analyze(it.text).sentiment
        }

        val differentSentiments = referenceSentiments.count {
            it != currentSentiment
        }

        val rarity = differentSentiments.toDouble() / referenceSentiments.size

        return if (rarity < 0.3) 1 else 0
    }
    
    /*
    "Sentiment"-Laufzeit mit unterschiedlichen Referenzmengen:

    Reference Comments: 10
    Sentiment Rarity: 0.6
    Sentiment Laufzeit: 3309,78 ms
    Sentiment: 0
    Risk Score: 6


    Reference Comments: 25
    Sentiment Rarity: 0.6
    Sentiment Laufzeit: 4755,37 ms
    Sentiment: 0
    Risk Score: 3


    Reference Comments: 50
    Sentiment Rarity: 0.6
    Sentiment Laufzeit: 5841,15 ms
    Sentiment: 0
    Risk Score: 3
    */
    
    private fun responseSpeed(
        currentComment: Kommentar
    ): Int {
        val parentId = currentComment.domParentId ?: return 0

        val parentComment = youtubeService.getComment(
            currentComment.videoId,
            parentId,
            null
        )

        val responseTimeMinutes =
            (currentComment.publishedAt.time - parentComment.publishedAt.time) / 60000.0

        println("Response time: $responseTimeMinutes minutes")

        return when {
            responseTimeMinutes < 10 -> 2
            responseTimeMinutes <= 30 -> 1
            else -> 0
        }
    }
    
    private fun textSimilarity(
        currentComment: Kommentar,
        comments: List<Kommentar>
    ): Int {
        val hasDuplicate = comments.any { comment ->
            comment.id != currentComment.id &&
            comment.text.trim() == currentComment.text.trim()
        }
        return if (hasDuplicate) 2 else 0
    }
}