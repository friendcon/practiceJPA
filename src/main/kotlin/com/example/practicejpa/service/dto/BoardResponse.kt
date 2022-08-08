package com.example.practicejpa.service.dto

import java.time.LocalDateTime

data class BoardResponse(
    val title: String,
    val content: String,
    val id: Long,
    val count: Long,
    val registerDate: LocalDateTime,
    val commentList: List<CommentResponse>?
)
