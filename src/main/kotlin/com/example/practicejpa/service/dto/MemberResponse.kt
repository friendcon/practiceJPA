package com.example.practicejpa.service.dto

import java.math.BigDecimal

data class MemberResponse(
    val id: Long,
    val pw: String,
    val email: String,
    val memberPointId: Long,
    val memberPoint: BigDecimal
)
