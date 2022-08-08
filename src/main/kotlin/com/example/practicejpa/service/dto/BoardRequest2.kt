package com.example.practicejpa.service.dto

import javax.validation.constraints.NotBlank

data class BoardRequest2(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
    val id: Long
)
