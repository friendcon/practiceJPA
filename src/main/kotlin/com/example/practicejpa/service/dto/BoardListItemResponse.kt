package com.example.practicejpa.service.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class BoardListItemResponse @QueryProjection constructor(
    val boardId: Long,
    val title: String,
    val conetent: String,
    val memberEmail: String,
    val view: Long,
    val registerDate: LocalDateTime,
    val commentCount: Int
)
