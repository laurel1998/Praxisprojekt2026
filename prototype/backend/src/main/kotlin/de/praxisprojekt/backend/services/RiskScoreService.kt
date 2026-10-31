package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.RiskScore
import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.models.YoutubeChannel
import de.praxisprojekt.backend.services.YoutubeService
import de.praxisprojekt.backend.services.SentimentClient
import org.springframework.stereotype.Service
import java.util.Date
import kotlin.math.roundToInt

@Service
class RiskScoreService(
    private val sentimentClient: SentimentClient,
    private val youtubeService: YoutubeService
) {
    
    //Hier anpassen, wenn sich max. Punkte je Kombination veraendern!!!
    private val maxPointsPerCategory = mapOf(
        "channelProfile" to 5,
        "commentProfile" to 4,
        "languageProfile" to 3,
        "commentActivity" to 4
    )

    fun calculateRiskScore(
        channel: YoutubeChannel,
        viewCount: Long,
        currentComment: Kommentar,
        comments: List<Kommentar>
    ): RiskScore {
        
        //var score = 0
        
        val publicationRatePoints = publicationRate(channel)
        val reachPoints = reach(viewCount, channel.subscriberCount)
        val descriptionPoints = channelDescription(channel)
        
        val interactionPoints = interactionIntensity(currentComment, comments)
        val linkPoints = links(currentComment)
        
        val ttrPoints = ttr(currentComment)
        val sentimentPoints = sentiment(currentComment, comments)
        
        val responseSpeedPoints = responseSpeed(currentComment)
        val similarityPoints = textSimilarity(currentComment,comments)

        //Debug-Ausgaben
        println("Publication Rate: $publicationRatePoints")
        println("Reach: $reachPoints")
        println("Description: $descriptionPoints")
        println("Interaction Intensity: $interactionPoints")
        println("Links: $linkPoints")
        println("TTR: $ttrPoints")
        println("Sentiment: $sentimentPoints")
        println("Antwortgeschwindigkeit: $responseSpeedPoints")
        println("wiederholungsgrad: $similarityPoints")
        
        val channelProfile = publicationRatePoints + reachPoints + descriptionPoints
        val commentProfile = interactionPoints + linkPoints
        val languageProfile = ttrPoints + sentimentPoints
        val commentActivity = responseSpeedPoints + similarityPoints
        
        val rawPoints = channelProfile + commentProfile + languageProfile + commentActivity
        
        val combinationBonus = calculateCombinationBonus(
            channelProfile,
            commentProfile,
            languageProfile,
            commentActivity
        )
        
        val categoryBonus = calculateCategoryBonus(
        channelProfile,
        commentProfile,
        languageProfile,
        commentActivity
        )
        
        val totalPoints = rawPoints + combinationBonus + categoryBonus
        val score = (totalPoints / 37.0 * 100).roundToInt()
        // Hier anpassen, wenn sich max. Punkteanzahl = 37 veraendert!!!
        // max. Rohpunkt = 16
        // max. Kombinationspunkte = 12
        // max. Kategoriepunkte = 9

        //Debug-Ausgaben
        println("Raw Points: $rawPoints")
        println("Combination Bonus: $combinationBonus")
        println("Category Bonus: $categoryBonus")
        println("Total Points: $totalPoints / 37")
        println("Risk Score: $score %")
        
        return RiskScore(
            score = score
        )
    }
    
    private fun calculateCombinationBonus(
        channelProfile: Int,
        commentProfile: Int,
        languageProfile: Int,
        commentActivity: Int
    ): Int {
        
        val categoryPoints = listOf(
        channelProfile,
        commentProfile,
        languageProfile,
        commentActivity
        )
        
        val maxPoints = maxPointsPerCategory.values.toList()

        // zip() verbindet zwei Listen anhand ihrer Position zu Paaren z.B. [(4, 5), (2, 4), (2, 3), (3, 4)]
        // sumOf() geht diese Paare nacheinander durch und addiert sie
        
        return categoryPoints.zip(maxPoints).sumOf { (points, max) ->
            val percentage = points.toDouble() / max

            when {
                percentage < 0.25 -> 0
                percentage < 0.50 -> 1
                percentage < 0.75 -> 2
                else -> 3
            }
        }
    }
    
    private fun calculateCategoryBonus(
        channelProfile: Int,
        commentProfile: Int,
        languageProfile: Int,
        commentActivity: Int
    ): Int {
        
        val maxPoints = maxPointsPerCategory.values.toList()

        val categoryPoints = listOf(
            channelProfile,
            commentProfile,
            languageProfile,
            commentActivity
        )

        // count() zählt, wie viele Elemente eine bestimmte Bedingung erfüllen
        // -> (Bedingung: mehr als 75% der Kombi-Punkte)
        
        val maxBonusCount = categoryPoints.zip(maxPoints).count { (points, max) -> 
            points.toDouble() / max >= 0.75
        }

        return when (maxBonusCount) {
            0, 1 -> 0
            2 -> 3
            3 -> 6
            4 -> 9
            else -> 0
        }
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
            .take(10) // Anzahl Vergleichskommentare stark begrenzt für schnellere Laufzeit 

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