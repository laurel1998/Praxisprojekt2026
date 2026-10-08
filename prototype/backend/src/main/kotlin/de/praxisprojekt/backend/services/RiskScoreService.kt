package de.praxisprojekt.backend.services

import de.praxisprojekt.backend.models.RiskScore
import de.praxisprojekt.backend.models.YoutubeChannel
import org.springframework.stereotype.Service
import java.util.Date

@Service
class RiskScoreService {

    fun calculateRiskScore(
        channel: YoutubeChannel,
        viewCount: Long
    ): RiskScore {
        var score = 0
        
        score += publicationRate(channel)
        score += reach(viewCount, channel.subscriberCount)
        score += channelDescription(channel)
        
        //Debug-Ausgabe
        println("Publication Rate: ${publicationRate(channel)}")
        println("Reach: ${reach(viewCount, channel.subscriberCount)}")
        println("Description: ${channelDescription(channel)}")
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
}