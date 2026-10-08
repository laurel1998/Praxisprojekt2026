package de.praxisprojekt.backend.controllers

import de.praxisprojekt.backend.models.RiskScore
import de.praxisprojekt.backend.services.RiskScoreService
import de.praxisprojekt.backend.services.YoutubeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/api/risk-score")
class RiskScoreController(
    private val riskScoreService: RiskScoreService,
    private val youtubeService: YoutubeService
) {

    @GetMapping
    fun getRiskScore(@RequestParam channelId: String,
                     @RequestParam videoId: String
    ): RiskScore {
        
        val channel = youtubeService.getChannel(channelId)
        val viewCount = youtubeService.getVideoViewCount(videoId)
        
        return riskScoreService.calculateRiskScore(channel, viewCount)
    }
}