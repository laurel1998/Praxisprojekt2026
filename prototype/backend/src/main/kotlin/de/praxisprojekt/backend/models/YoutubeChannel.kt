package de.praxisprojekt.backend.models

import java.util.Date

data class YoutubeChannel(
    val publishedAt: Date,
    val videoCount: Int,
    val subscriberCount: Long,
    val description: String
)
