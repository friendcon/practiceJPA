package com.example.practicejpa

import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.service.BoardService
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("local")
class BoardTests(
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val boardService: BoardService
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

    @Test
    @DisplayName("QueryProjection 을 이용한 게시글 목록 읽기")
    fun readBoards3() {
        val boardList = boardRepository.findBoardAndCommentCountWithPaging(
            PageRequest.of(0,30), null, SEARCHTYPE.TITLE, ORDERTYPE.COMMENTCOUNT)

        boardList.forEach {
            println(it.toString())
        }
    }

    @Test
    @DisplayName("해당 달에 올라온 게시글 조회")
    fun readBoard4Test() {
        val boardList = boardService.readBoard4(1, LocalDateTime.of(2022,7,1,0,0),
        LocalDateTime.of(2022, 7, 31, 0, 0))
        boardList.forEach {
            println(it.toString())
        }
    }

    @Test
    @DisplayName("포인트")
    fun readBoard5Test() {

    }
}