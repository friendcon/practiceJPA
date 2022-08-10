package com.example.practicejpa.domain

import com.example.practicejpa.service.dto.MemberResponse
import com.example.practicejpa.util.BaseEntity
import com.example.practicejpa.util.BaseTimeEntity
import com.example.practicejpa.util.POINT_CHANGE_TYPE
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class Member(
    val pw: String,
    val email: String,
    @OneToOne(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    val memberPoint: MemberPoint // member 회원가입시 point 생성
    /*@OneToOne(mappedBy = "member") // 연관관계 주인은 MemberPoint (memberPoint 가 key를 가지고있다)
    val memberPoint: MemberPoint*/
    /*@OneToOne(mappedBy = "member")
    val memberPoint: MemberPoint*/
): BaseEntity() {

    fun toResponse(): MemberResponse {
        return MemberResponse(
            id?: throw NullPointerException("id가 null 입니다"),
            pw,
            email,
            memberPoint.id?: throw NullPointerException("id가 null 입니다"),
            memberPoint.point
        )
    }
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Member

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}