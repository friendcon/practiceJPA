package com.example.practicejpa.domain

import com.example.practicejpa.service.dto.BoardRequest
import com.example.practicejpa.service.dto.BoardResponse
import com.example.practicejpa.util.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class Board(
    var title: String,
    var content: String,
    var count: Long = 0,
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    @JoinColumn(name = "board_id")
    var comments: MutableSet<Comment> = mutableSetOf()
): BaseEntity() {

    // 조회수 증가 : 게시글 수정, 게시글 조회시 호출
    fun increaseCount(): Board {
        count++
        return this
    }

    // 댓글 추가
    fun addComment(comment: Comment): Board {
        comments.add(comment)
        return this
    }

    fun update(boardRequest: BoardRequest): Board {
        title = boardRequest.title
        content = boardRequest.content
        count++
        return this
    }

    fun toResponse(): BoardResponse {
        return BoardResponse(
            title,
            content,
            member.id!!,
            count,
            createdDate,
            comments?.map {
                it.toResponse()
            }
        )
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Board

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Board(title='$title', content='$content', count=$count, member=$member)"
    }
}
