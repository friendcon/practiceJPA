package com.example.practicejpa.repository

import com.example.practicejpa.domain.MemberPoint2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MemberPoint2Repository: JpaRepository<MemberPoint2, Long>, MemberPoint2RepositorySupport {
    fun findByMemberId(id: Long): MemberPoint2

    /**
     * JPQL 을 이용하여 포인트 가장 많은 멤버순으로 조회
     * JPQL 은 limit 을 사용할 수 없어서,, 데이터가 많으면 사용할 수 없음
     * [해결방법1] native query 를 사용하면 limit 을 사용할 수 있다
     * [해결방법2]
     */
    @Query(
        "select point from MemberPoint2 as point join fetch Board as board " +
                "on point.member.id = board.member.id " +
                "group by board.member.id, point.member.id, point.id " +
                "order by point.point desc , count(board.id) desc "
    )
    fun findTopPoint(): List<MemberPoint2>
}
