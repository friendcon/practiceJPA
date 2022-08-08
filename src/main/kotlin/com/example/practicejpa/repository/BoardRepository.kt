package com.example.practicejpa.repository

import com.example.practicejpa.domain.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {
}