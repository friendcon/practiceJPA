package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import com.example.practicejpa.util.POINT_CHANGE_TYPE
import com.example.practicejpa.util.POINT_TYPE
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
    // 포인트 업데이트 후 히스토리 업데이트
    fun updatePoint(pointType: POINT_TYPE) {
        when(pointType) {
            POINT_TYPE.CREATE_BOARD -> {
                point += BigDecimal.valueOf(100)
                val history = MemberPointHistory(
                    BigDecimal(100),
                    POINT_CHANGE_TYPE.ADD
                )
                memberPointHistories.add(history)
            }
            POINT_TYPE.DELETE_BOARD -> {
                point -= BigDecimal.valueOf(50)
                val history = MemberPointHistory(
                    BigDecimal(50),
                    POINT_CHANGE_TYPE.SUBSTRACT
                )
                memberPointHistories.add(history)
            }
        }
    }

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
