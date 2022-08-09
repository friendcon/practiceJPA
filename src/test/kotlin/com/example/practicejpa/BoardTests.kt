package com.example.practicejpa

import com.example.practicejpa.repository.BoardRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class BoardTests(
    @Autowired private val boardRepository: BoardRepository
) {

    @Test
    @DisplayName("게시글 읽기 ")
    fun readBoardsWithPaging() {
        // 1+N 문제 발생
        val boardList = boardRepository.findByOrderByCreatedDateDesc(PageRequest.of(0, 10))
        boardList.forEach {
            println("${it.id} ${it.createdDate}: ${it.toString()}")
        }
    }

    @Test
    @DisplayName("게시글 읽기 (querydsl 사용)")
    fun readBoardsWithPagingUseQueryDsl() {
        val result = boardRepository.findBoardWithPaging(PageRequest.of(0, 20))
        result.forEach {
            println(it.toString())
        }
    }
}