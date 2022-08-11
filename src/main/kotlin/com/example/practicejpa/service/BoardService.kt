package com.example.practicejpa.service

import com.example.practicejpa.domain.Board
import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.repository.Member2Repository
import com.example.practicejpa.repository.MemberPoint2Repository
import com.example.practicejpa.repository.MemberRepository
import com.example.practicejpa.service.dto.BoardListItemResponse
import com.example.practicejpa.service.dto.BoardRequest
import com.example.practicejpa.service.dto.BoardResponse
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import com.example.practicejpa.util.UserBoardAuthException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.math.abs

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val memberRepository: MemberRepository,
    private val member2Repository: Member2Repository,
    private val memberPoint2Repository: MemberPoint2Repository

) {
    // 글 작성 완료 후 작성한 글로 페이지를 이동하게 되면 BoardResponse 리턴해야함
    // 요구사항 추가 ) 게시글 작성시 게시글을 작성한 user point 를 100 추가해준다.
    @Transactional
    fun createBoard(request: BoardRequest): BoardResponse {
        println("request user id : ${request.id}")
        val board = boardRepository.save(request.toEntity()) // 게시글 DB에 추가
        // val memberPoint = memberRepository.findById(request.id).get().memberPoint
        // memberPoint.updatePoint(POINT_TYPE.CREATE_BOARD) // 포인트 추가
        // val point = memberPoint2Repository.findByMember2Id(request.id)
        val point = memberPoint2Repository.findByMemberId(request.id)
        // point.updatePoint(POINT_TYPE.CREATE_BOARD)
        point.addBoardPoint()
        return board.toResponse()
    }

    // 게시글 상세 조회
    @Transactional
    fun readBoard(id: Long): BoardResponse {
        val boardResponse = boardRepository.findById(id).get()
        boardResponse.increaseCount() // 조회수 증가
        return boardResponse.toResponse()
    }
    // 게시글 리스트 조회 with 페이징, 검색타입, 검색어, 정렬
    fun readBoards(page: Int?, query: String?,
                   searchType: SEARCHTYPE?, orderType: ORDERTYPE?): List<BoardResponse> {
        val pageable = when(page) {
            null, 0 -> PageRequest.of(0, 3)
            else -> PageRequest.of(abs(page).minus(1), 3)
        }
        println("query: $query , searchType : $searchType orderType : $orderType")
        val boardList = boardRepository.findBoardWithPaging2(pageable, query, searchType, orderType).map {
            it.toResponse()
        }
        return boardList
    }
    // 게시글 리스트 조회 with 페이징
    fun readBoards2(page: Int?): List<BoardResponse> {
        val pageable = when (page) {
            null, 0 -> PageRequest.of(0, 3)
            else -> PageRequest.of(abs(page).minus(1), 3)
        }
        return boardRepository.findBoardWithPaging(pageable).map { it.toResponse() }
    }

    // 게시글 리스트 조회 with 페이징, 검색타입, 검색어, 정렬 (정렬기준추가)
    fun readBoards3(
        page: Int?, query: String?,
        searchType: SEARCHTYPE?, orderType: ORDERTYPE?
    ): List<BoardListItemResponse> {
        val pageable = when (page) {
            null, 0 -> PageRequest.of(0, 3)
            else -> PageRequest.of(abs(page).minus(1), 3)
        }
        println("query: $query , searchType : $searchType orderType : $orderType")
        return boardRepository.findBoardAndCommentCountWithPaging(pageable, query, searchType, orderType)
    }

    fun readBoard4(
        page: Int?,
        startDate: LocalDateTime,
        finishDate: LocalDateTime
    ): List<BoardListItemResponse> {
        val pageable = when(page) {
            null, 0 -> PageRequest.of(0, 3)
            else -> PageRequest.of(abs(page).minus(1), 3)
        }
        return boardRepository.findMonthlyBoard(pageable, startDate, finishDate)
    }

    // 게시글 수정
    @Transactional
    fun updateBoard(boardId: Long, boardRequest: BoardRequest): BoardResponse {
        val board = boardRepository.findById(boardId).get()
        return board.update(boardRequest).toResponse()
    }

    /**
     *  delete vs deleteById
     *  1. deleteById를 하면 id로 select 를 하고 delete 문을 실행한다. id 에 대한 null 체크를 하지 않아도
     *     JPA 가 null 체크를 도와준다
     *  2. delete를 하면 entity를 파라미터로 넘겨줘야하고 엔티티 null 체크 후 entityManager 로 null 체크 후 삭제를 진행
     *  두가지 방식 중 findById + delete 조합을 많이 사용한다
     */

    // 게시글 삭제
    @Transactional
    fun deleteBoard(boardId: Long, memberId: Long) {
        val board = boardRepository.findById(boardId).get()
        println(board.member.id)
        println(memberId)
        if (board.member.id != memberId)
            throw UserBoardAuthException("게시글 삭제 권한이 없습니다")
        // val member = memberRepository.findById(memberId).get().memberPoint
        val point = memberPoint2Repository.findByMemberId(memberId)
        // point.updatePoint(POINT_TYPE.DELETE_BOARD)
        point.substractBoardPoint()
        // member.updatePoint(POINT_TYPE.DELETE_BOARD) // 업데이트 포인트
        boardRepository.delete(board)
    }

    // 게시글 전체 조회
    fun BoardRequest.toEntity(): Board {
        return Board(
            title,
            content,
            0, // 게시글 작성시 조회수는 0
            member2Repository.findById(id).get()
        )
    }
}