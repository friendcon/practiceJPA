package com.example.practicejpa.repository

import com.example.practicejpa.domain.Board
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import org.springframework.data.domain.Pageable

interface BoardRepositorySupport {
    fun findBoardWithPaging(pageable: Pageable): List<Board>

    fun findBoardWithPaging2(page: Pageable, query: String?,
                             searchType: SEARCHTYPE?, order: ORDERTYPE?): List<Board>
}