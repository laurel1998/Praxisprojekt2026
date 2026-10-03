package de.praxisprojekt.backend.models

import java.util.Date

data class Kommentar(
    val id: String,
    val videoId: String,
    val authorChannelId: String,
    val text: String,
    val likeCount: Int,
    val publishedAt: Date,
    val totalReplyCount: Int,
    val testScore: Int = 0
)