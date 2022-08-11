package com.example.practicejpa.controller

import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.service.BoardService
import com.example.practicejpa.service.dto.BoardListItemResponse
import com.example.practicejpa.service.dto.BoardRequest
import com.example.practicejpa.service.dto.BoardResponse
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import com.example.practicejpa.util.UserBoardAuthException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Tag(name = "게시판")
@RestController
@RequestMapping("/board")
class BoardController(
    private val boardService: BoardService,
    private val boardRepository: BoardRepository
) {
    // 조회가 아니라 1+N 문제 발생 X
    // 게시글 생성
    @Operation(summary = "게시글 작성")
    @PostMapping("/create")
    fun createBoard(@Valid @RequestBody request: BoardRequest): ResponseEntity<BoardResponse> {
        return ResponseEntity.ok(boardService.createBoard(request))
    }


    // 게시글 상세 조회
    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/read/{id}")
    fun readOneBoard(@PathVariable id: Long): ResponseEntity<BoardResponse> {
        return ResponseEntity.ok(boardService.readBoard(id))
    }

    @Operation(summary = "게시글 전체 목록 조회 1")
    @GetMapping("/list")
    fun readBoardList(
        @RequestParam(required = false) page: Int?,
        // size 는 서버에서 고정시킬 수 있기 때문에 client 에게 내려주지 않아도 될듯?
        @RequestParam(required = false) query: String?,
        // where 절에 붙어야 할 것들
        @RequestParam(required = false) searchType: SEARCHTYPE?,
        @RequestParam(required = false) orderType: ORDERTYPE?,
    ): ResponseEntity<List<BoardResponse>> {
        return ResponseEntity.ok(boardService.readBoards(page, query, searchType, orderType))
    }

    @Operation(summary = "게시글 전체 목록 조회 2")
    @GetMapping("/list2")
    fun readList(
        @RequestParam(required = false) page: Int?
    ): ResponseEntity<List<BoardResponse>> {
        return ResponseEntity.ok(boardService.readBoards2(page))
    }

    @Operation(summary = "게시글 전체 목록 조회 3 + 댓글이 많은 순으로 정렬")
    @GetMapping("/list3")
    fun readList3(
        @RequestParam(required = false) page: Int?,
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) searchType: SEARCHTYPE?,
        @RequestParam(required = false) orderType: ORDERTYPE?,
    ): ResponseEntity<List<BoardListItemResponse>> {
        return ResponseEntity.ok(boardService.readBoards3(page, query, searchType, orderType))
    }



    @Operation(summary = "게시글 수정")
    @PutMapping("/update/{id}")
    fun modifyBoard(@PathVariable id: Long, @Valid @RequestBody request: BoardRequest): ResponseEntity<BoardResponse> {
        if( boardRepository.findById(id).get().member.id != request.id )
            throw UserBoardAuthException("게시글 수정 권한이 없습니다")
        return ResponseEntity.ok(boardService.updateBoard(id, request))
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/delete/{id}")
    fun deleteBoard(@PathVariable id: Long, @RequestParam userId: Long): ResponseEntity<Void> {
        boardService.deleteBoard(id, userId)
        return ResponseEntity(HttpStatus.OK)
    }
}