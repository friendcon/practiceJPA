package com.example.practicejpa.controller

import com.example.practicejpa.domain.Board
import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.repository.MemberRepository
import com.example.practicejpa.service.BoardService
import com.example.practicejpa.service.dto.BoardRequest
import com.example.practicejpa.service.dto.BoardRequest2
import com.example.practicejpa.service.dto.BoardResponse
import com.example.practicejpa.util.UserBoardAuthException
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/board")
class BoardController(
    private val boardService: BoardService,
    private val boardRepository: BoardRepository
) {
    // 게시글 생성
    @PostMapping("/create")
    fun createBoard(@Valid @RequestBody request: BoardRequest): ResponseEntity<BoardResponse> {
        return ResponseEntity.ok(boardService.createBoard(request))
    }

    // 게시글 상세 조회
    @GetMapping("/read/{id}")
    fun readOneBoard(@PathVariable id: Long): ResponseEntity<BoardResponse> {
        return ResponseEntity.ok(boardService.readBoard(id))
    }

    @PutMapping("/update/{id}")
    fun modifyBoard(@PathVariable id: Long, @Valid @RequestBody request: BoardRequest): ResponseEntity<BoardResponse> {
        if( boardRepository.findById(id).get().member.id != request.id )
            throw UserBoardAuthException("게시글 수정 권한이 없습니다")
        return ResponseEntity.ok(boardService.updateBoard(id, request))
    }

    @DeleteMapping("/delete/{id}")
    fun deleteBoard(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<Void> {
        boardService.deleteBoard(id, userId)
        return ResponseEntity(HttpStatus.OK)
    }
}