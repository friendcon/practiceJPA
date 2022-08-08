package com.example.practicejpa.service

import com.example.practicejpa.domain.Board
import com.example.practicejpa.domain.Member
import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.repository.MemberRepository
import com.example.practicejpa.service.dto.BoardRequest
import com.example.practicejpa.service.dto.BoardRequest2
import com.example.practicejpa.service.dto.BoardResponse
import com.example.practicejpa.util.UserBoardAuthException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val memberRepository: MemberRepository
) {
    // 글 작성 완료 후 작성한 글로 페이지를 이동하게 되면 BoardResponse 리턴해야함
    fun createBoard(request: BoardRequest): BoardResponse {
        val board = boardRepository.save(request.toEntity())
        return board.toResponse()
    }

    // 게시글 상세 조회
    @Transactional
    fun readBoard(id: Long): BoardResponse {
        val boardResponse = boardRepository.findById(id).get()
        boardResponse.increaseCount() // 조회수 증가
        return boardResponse.toResponse()
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
        return boardRepository.delete(board)
    }

    // 게시글 전체 조회
    fun BoardRequest.toEntity(): Board {
        return Board(
            title,
            content,
            0, // 게시글 작성시 조회수는 0
            memberRepository.findById(id).get()
        )
    }
}