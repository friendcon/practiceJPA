package com.example.practicejpa.service.dto

import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val content: String,
    val createdDate: LocalDateTime
)
