package com.example.practicejpa.service.dto

import com.querydsl.core.annotations.QueryProjection
import java.math.BigDecimal

data class PointRankingResponse @QueryProjection constructor(
    val pointId: Long,
    val memberId: Long,
    val point: BigDecimal,
    val boardCount: Long
)
