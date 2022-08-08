package com.example.practicejpa.domain

import com.example.practicejpa.service.dto.CommentResponse
import com.example.practicejpa.util.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Comment(
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "member_id")
    val member: Member,
    val content: String
): BaseEntity() {

    fun toResponse(): CommentResponse {
        return CommentResponse(
            member.id!!,
            content,
            createdDate
        )
    }
    override fun toString(): String {
        return "Comment(member=$member, content='$content')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Comment

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

}
