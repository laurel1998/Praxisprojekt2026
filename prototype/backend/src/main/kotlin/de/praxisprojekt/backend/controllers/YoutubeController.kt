package de.praxisprojekt.backend.controllers

import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.services.YouTubeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/youtube")
class YouTubeController(
    private val youtubeService: YouTubeService
) {

    @GetMapping("/comments")
    fun getComments(
        @RequestParam videoId: String
    ): List<Kommentar> {
        return youtubeService.getComments(videoId)
    }
}