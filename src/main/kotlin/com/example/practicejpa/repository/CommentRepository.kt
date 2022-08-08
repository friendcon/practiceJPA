package com.example.practicejpa.repository

import com.example.practicejpa.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository: JpaRepository<Comment, Long> {
}