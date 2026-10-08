package de.praxisprojekt.backend.controllers

import de.praxisprojekt.backend.models.Kommentar
import de.praxisprojekt.backend.services.YoutubeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin

@CrossOrigin(origins = ["https://www.youtube.com"])
@RestController
@RequestMapping("/api/youtube")
class YouTubeController(
    private val youtubeService: YoutubeService
) {

    //Kontextinformationen
    @GetMapping("/comments")
    fun getComments(
        @RequestParam videoId: String,
    ): List<Kommentar> {
        return youtubeService.getComments(videoId)
    }
    
    //Zielkommentar
    @GetMapping("/comment")
    fun getComment(
        @RequestParam videoId: String,
        @RequestParam commentId: String
    ): Kommentar {
        return youtubeService.getComment(videoId, commentId)
    }
}