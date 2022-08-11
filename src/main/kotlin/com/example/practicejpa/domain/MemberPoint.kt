package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import org.hibernate.Hibernate
import java.math.BigDecimal
import javax.persistence.*

@Entity
data class MemberPoint(
    @Column(precision = 20, scale = 6)
    var point: BigDecimal = BigDecimal.valueOf(0), // MemberPoint 처음 생성시 초기값 0
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    @JoinColumn(name = "member_point_id")
    val memberPointHistories: MutableList<MemberPointHistory> = mutableListOf()
): BaseEntity() {

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as MemberPoint

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
