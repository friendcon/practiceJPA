package com.example.practicejpa.service.dto

import java.time.LocalDateTime

data class BoardResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val memberEmail: String,
    val count: Long,
    val registerDate: LocalDateTime,
    val commentList: List<CommentResponse>?
)
