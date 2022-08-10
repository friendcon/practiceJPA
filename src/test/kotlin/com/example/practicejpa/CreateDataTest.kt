package com.example.practicejpa

import com.example.practicejpa.domain.Board
import com.example.practicejpa.domain.Comment
import com.example.practicejpa.domain.Member
import com.example.practicejpa.domain.MemberPoint
import com.example.practicejpa.repository.BoardRepository
import com.example.practicejpa.repository.CommentRepository
import com.example.practicejpa.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("local")
class CreateDataTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val commentRepository: CommentRepository
) {
    @Test
    @DisplayName("user 데이터 생성")
    fun CreateDataTest() {
        for(i in 1..300) {
            val member = Member(
                pw = "mem",
                email = "$i@test.com",
                MemberPoint()
            )
            memberRepository.save(member)
        }
    }

    @Test
    @DisplayName("board 데이터 생성")
    fun CreateDataTest2() {
        val boardList = mutableListOf<Board>()
        for(i in 1..300) {
            val board = (Board(
                title = "안녕하세요 가입인사 합니다 mem $i",
                content = "게시글 내용 $i",
                member = memberRepository.findById(i.toLong()).get()
            ))
            boardList.add(board)
        }

        boardRepository.saveAll(boardList)
    }

    @Test
    @Transactional
    @DisplayName("board comment 생성")
    fun CreateCommentTest() {
        val member = memberRepository.findById(4L).get()
        val board = boardRepository.findById(310L).get()
        for(i in 1..10) {
            val comment = Comment(
                member,
                "댓글이여요"
            )
            board.addComment(comment)
        }

        board.comments.forEach {
            println(it.toString())
            Assertions.assertThat(it.content).isEqualTo("댓글이여요")
        }
    }

    @Test
    @Transactional
    @DisplayName("board 에서 comment 조회 ")
    fun ReadBoardCommentTest() {
        boardRepository.findById(309L).get().comments.forEach {
            println(it.toString())
        }
    }
}