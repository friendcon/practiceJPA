package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import com.example.practicejpa.util.POINT_CHANGE_TYPE
import com.example.practicejpa.util.POINT_TYPE
import org.hibernate.Hibernate
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
data class MemberPointHistory(
    @Column(precision = 20, scale = 6)
    val changePoint: BigDecimal,
    @Enumerated(EnumType.STRING)
    val pointChangeType: POINT_CHANGE_TYPE,
    @Enumerated(EnumType.STRING)
    val pointType: POINT_TYPE
): BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as MemberPointHistory

        return id != null && id == other.id
    }
    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String {
        return "MemberPointHistory(changePoint=$changePoint, pointChangeType=$pointChangeType, pointType=$pointType)"
    }
}