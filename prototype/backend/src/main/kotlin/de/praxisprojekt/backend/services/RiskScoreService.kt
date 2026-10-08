package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.RiskScore
import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.models.YoutubeChannel
import org.springframework.stereotype.Service
import java.util.Date

@Service
class RiskScoreService {

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
        
        //Debug-Ausgabe
        println("Publication Rate: ${publicationRate(channel)}")
        println("Reach: ${reach(viewCount, channel.subscriberCount)}")
        println("Description: ${channelDescription(channel)}")
        println("Interaction Intensity: ${interactionIntensity(currentComment, comments)}")
        println("Links: ${links(currentComment)}")
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
        
        //Debug-Ausgabe
        println("Current comment likes: ${currentComment.likeCount}")
        println("Comparison comments: ${comparisonComments.size}")
        println("Average likes: $averageLikes")
        println("Like ratio: $likeRatio")
        println("Current comment ID: ${currentComment.id}")
        println("Comparison contains current comment: ${
            comparisonComments.any { it.id == currentComment.id }
        }")

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
}