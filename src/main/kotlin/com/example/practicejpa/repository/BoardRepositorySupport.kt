package com.example.practicejpa.repository

import com.example.practicejpa.domain.Board
import com.example.practicejpa.service.dto.BoardListItemResponse
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.time.LocalDateTime

interface BoardRepositorySupport {
    fun findBoardWithPaging(pageable: Pageable): List<Board>

    fun findBoardWithPaging2(page: Pageable, query: String?,
                             searchType: SEARCHTYPE?, order: ORDERTYPE?): List<Board>

    // 댓글수추가
    fun findBoardAndCommentCountWithPaging(
        pageable: Pageable, query: String?,
        searchType: SEARCHTYPE?, order: ORDERTYPE?
    ): List<BoardListItemResponse>
    fun findMonthlyBoard(page: Pageable, startDate: LocalDateTime, finishDateTime: LocalDateTime): List<BoardListItemResponse>

}