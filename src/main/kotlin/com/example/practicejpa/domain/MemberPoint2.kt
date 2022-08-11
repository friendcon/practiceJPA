package com.example.practicejpa.domain

import com.example.practicejpa.util.BaseEntity
import com.example.practicejpa.util.POINT_CHANGE_TYPE
import com.example.practicejpa.util.POINT_TYPE
import org.hibernate.Hibernate
import java.math.BigDecimal
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class MemberPoint2(
    @Column(precision = 20, scale = 6)
    var point: BigDecimal = BigDecimal.valueOf(0), // MemberPoint 처음 생성시 초기값 0
    @OneToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "member2_id")
    val member: Member2,
    @OneToMany(cascade = [CascadeType.PERSIST], orphanRemoval = true)
    @JoinColumn(name = "member_point_2_id")
    val memberPointHistories: MutableList<MemberPointHistory> = mutableListOf()
): BaseEntity() {
    /**
     * ADD_POINT : 게시글 작성시 포인트 추가
     * SUBSTRACT_POINT : 게시글 삭제시 포인트 차감
     * REWARD_POINT : 리워드 포인트는 변동사항이 있으므로
     */
    companion object {
        private val ADD_POINT = BigDecimal.valueOf(100)
        private val SUBSTRACT_POINT = BigDecimal.valueOf(100)
    }

    fun addRewardPoint(pointValue: BigDecimal) {
        point += pointValue
        updateHistory(MemberPointHistory(pointValue, POINT_CHANGE_TYPE.ADD, POINT_TYPE.MONTHLY_REWARD))
    }
    fun substractBoardPoint() {
        point -= SUBSTRACT_POINT
        updateHistory(MemberPointHistory(SUBSTRACT_POINT, POINT_CHANGE_TYPE.SUBSTRACT, POINT_TYPE.DELETE_BOARD))
    }

    fun addBoardPoint() {
        point += ADD_POINT
        updateHistory(MemberPointHistory(ADD_POINT, POINT_CHANGE_TYPE.ADD, POINT_TYPE.CREATE_BOARD))
    }

    private fun updateHistory(history: MemberPointHistory) {
        memberPointHistories.add(history)
    }

    override fun toString(): String {
        return "MemberPoint2(point=$point)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as MemberPoint2

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
