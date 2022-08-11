package com.example.practicejpa.repository

import com.example.practicejpa.domain.MemberPoint2
import com.example.practicejpa.domain.QBoard.board
import com.example.practicejpa.domain.QMemberPoint2.memberPoint2
import com.example.practicejpa.service.dto.PointRankingResponse
import com.example.practicejpa.service.dto.QPointRankingResponse
import com.querydsl.core.Tuple
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class MemberPoint2RepositorySupportImpl(
    private val queryFactory: JPAQueryFactory
): QuerydslRepositorySupport(MemberPoint2::class.java), MemberPoint2RepositorySupport{

    /**
     * [방법1Tupl로데이터가져오기]
     * Point 가 가장 많은 순으로 정렬 (동일 포인트 보유시 작성한 게시글 순으로 한번 더 정렬)
     * 데이터를 가져올 때 가장 포인트가 많은 순으로 정렬해서가져온다
     * 튜플로 가져오면 어떤 데이터인지 모르기 때문에 Projection 을 사용해서 데이터를 가져와야한다
     */
    override fun findTop5PercentPoint(count: Long): List<Tuple> {
        return queryFactory.select(
            memberPoint2.id,
            memberPoint2.member.id,
            memberPoint2.point
        ).from(memberPoint2)
            .leftJoin(board).on(memberPoint2.member.id.eq(board.member.id))
            .groupBy(board.member.id, memberPoint2.point, memberPoint2.id)
            .orderBy(OrderSpecifier(Order.DESC, memberPoint2.point), OrderSpecifier(Order.DESC, board.id.count()))
            .limit(count)
            .fetch()
    }

    /**
     * [방법2PointRankingResponse로데이터가져오기]
     * Point가 가장 많은 순으로 정렬, 작성한 게시글 수도 가져오기
     * Projection 을 사용해봅시다..
     *
     */
    override fun findTopRankPoint(count: Long): List<PointRankingResponse> {
        return queryFactory.select(QPointRankingResponse(
            memberPoint2.id,
            memberPoint2.member.id,
            memberPoint2.point,
            board.member.id.count()
        )).from(memberPoint2)
            .leftJoin(board).on(memberPoint2.member.id.eq(board.member.id))
            .groupBy(board.member.id, memberPoint2.point, memberPoint2.id)
            .orderBy(OrderSpecifier(Order.DESC, memberPoint2.point), OrderSpecifier(Order.DESC, board.id.count()))
            .limit(count)
            .fetch()
    }
}