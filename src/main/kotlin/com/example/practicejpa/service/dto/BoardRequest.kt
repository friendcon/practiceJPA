package com.example.practicejpa.service.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class BoardRequest(
    @field:NotBlank
    val title: String,
    @field:NotBlank
    val content: String,
    @field:NotNull
    val id: Long
)
