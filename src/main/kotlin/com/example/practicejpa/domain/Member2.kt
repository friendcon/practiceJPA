package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
data class Member2(
    val pw: String,
    val email: String,
    @OneToOne(mappedBy = "member")
    val memberPoint2: MemberPoint2? = null
): BaseEntity() {
    override fun toString(): String {
        return "Member2(pw='$pw', email='$email')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Member2

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
