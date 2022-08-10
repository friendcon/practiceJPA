package com.example.practicejpa.repository

import com.example.practicejpa.domain.Board
import com.example.practicejpa.domain.QBoard.board
import com.example.practicejpa.service.dto.BoardListItemResponse
import com.example.practicejpa.service.dto.QBoardListItemResponse
import com.example.practicejpa.util.ORDERTYPE
import com.example.practicejpa.util.SEARCHTYPE
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import java.time.LocalDateTime
import java.util.Arrays

class BoardRepositorySupportImpl(
    private val jpaQueryFactory: JPAQueryFactory
): QuerydslRepositorySupport(Board::class.java), BoardRepositorySupport {

    override fun findBoardWithPaging(pageable: Pageable): List<Board> {
        return jpaQueryFactory.selectFrom(board)
                // createdDate 로 내림차순 정렬하는 것 보다 인덱스로 설정된 id 내림차순으로 정렬하는게 더 효율적임
                // id 값이 클수록 가장 최근에 생성된 데이터임
            .orderBy(board.createdDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
    // 페이징, 검색어 및 검색타입 지정,
    override fun findBoardWithPaging2(
        page: Pageable,
        query: String?,
        searchType: SEARCHTYPE?,
        order: ORDERTYPE?
    ): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .where(
                when(searchType) {
                    SEARCHTYPE.TITLE -> containTitle(query) // searchType 이 title 일 경우 containTitle(query) 실행
                    SEARCHTYPE.CONTENT -> containContent(query) // searchType 이 content 일 경우 containContent 실행
                    SEARCHTYPE.WRITER -> containWriter(query) // searchType 이 writer 일 경우 containWriter 실행
                    SEARCHTYPE.TITLE_OR_CONTENT -> containTitleOrContent(query) // title_or_content 일 경우 containTitleOrContent 실행
                    else -> null
                }
            )
            /**
             * orderBy 역시 동적으로 체크하고 만약 1차로 정렬한 결과에서 동일한 결과가 나오면 board_id 로 다시 정렬한다
            */
            .orderBy(getOrderSpecifier(order), OrderSpecifier(Order.DESC, board.id))
            //.orderBy(getOrderSpecifier(order), OrderSpecifier(Order.DESC, board.id))
            // client 에서 page 요청이 없으면 0으로 offset 설정하고 page 요청이 있다면 page-1 로 offset 설정
            .offset(page.offset)
            // 한 페이지 size(현재는 3개) 를 백엔드에서 지정. request에는 page 만 요청하면 된다
            .limit(page.pageSize.toLong())
            .fetch()
    }

    /**
     * findBoardAndCommentCountWithPaging : findBoardWithPaging2에 comment 순 정렬 추가
     */
    override fun findBoardAndCommentCountWithPaging(
        page: Pageable,
        query: String?,
        searchType: SEARCHTYPE?,
        order: ORDERTYPE?
    ): List<BoardListItemResponse> {
        return jpaQueryFactory.select(QBoardListItemResponse(
           board.id,
           board.title,
           board.content,
           board.member.email,
           board.count,
           board.createdDate,
           board.comments.size()
        )).from(board)
            .where(
                when(searchType) {
                    SEARCHTYPE.TITLE -> containTitle(query)
                    SEARCHTYPE.CONTENT -> containContent(query)
                    SEARCHTYPE.WRITER -> containWriter(query)
                    SEARCHTYPE.TITLE_OR_CONTENT -> containTitleOrContent(query)
                    else -> null
                }
            )
            .orderBy(getOrderSpecifier(order), OrderSpecifier(Order.DESC, board.id))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    // 정렬기준 : 가장 최근에 올라온 순, 한달 데이터..
    override fun findMonthlyBoard(
        page: Pageable,
        startDateTime: LocalDateTime,
        finishDateTime: LocalDateTime
    ): List<BoardListItemResponse> {
        return jpaQueryFactory.select(QBoardListItemResponse(
            board.id,
            board.title,
            board.content,
            board.member.email,
            board.count,
            board.createdDate,
            board.comments.size()
        )).from(board)
            .where(
                dateTimeCondition(startDateTime, finishDateTime)
            )
            .orderBy(OrderSpecifier(Order.DESC, board.createdDate))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    private fun dateTimeCondition(startDateTime: LocalDateTime, finishDateTime: LocalDateTime): BooleanExpression? {
        return board.createdDate.goe(startDateTime).and(board.createdDate.loe(finishDateTime))
    }

    /**
     * 제목에 query 가 포함되었는지 확인
     * like() vs contains() : keyword vs %keyword%
     */
    private fun containTitle(query: String?): BooleanExpression? {
        return when(StringUtils.isNullOrEmpty(query)) {
            true -> null
            false -> board.title.containsIgnoreCase(query)
        }
    }

    /**
     * 내용에 query 가 포함되었는지 확인
    */
    private fun containContent(query: String?): BooleanExpression? {
        return when(StringUtils.isNullOrEmpty(query)) {
            true -> null
            false -> board.content.containsIgnoreCase(query)
        }
    }

    /**
     * 작성자 이름에 query 가 포함되어있는지 확인
    */
    private fun containWriter(query: String?): BooleanExpression? {
        return when(StringUtils.isNullOrEmpty(query)) {
            true -> null
            false -> board.member.email.containsIgnoreCase(query)
        }
    }

    /**
     *  제목 또는 내용에 query 가 포함되어 있는지 확인
     */
    private fun containTitleOrContent(query: String?): BooleanExpression? {
        return when(StringUtils.isNullOrEmpty(query)) {
            true -> null
            false -> {
                board.title.containsIgnoreCase(query).or(board.content.containsIgnoreCase(query))
            }
        }
    }

    /**
     * 게시글을 조회수 순으로 조회
     */
    private fun getOrderSpecifier(orderType: ORDERTYPE?): OrderSpecifier<*> {
        return when(orderType) {
            ORDERTYPE.COUNT -> OrderSpecifier(Order.DESC, board.count) // 조회수가 높은 순으로 조회
            ORDERTYPE.ID_ASC -> OrderSpecifier(Order.ASC, board.id) // 가장 오래된 게시글 순으로 조회
            ORDERTYPE.ID_DESC -> OrderSpecifier(Order.DESC, board.id) // 가장 최근 게시글 순으로 조회
            ORDERTYPE.COMMENTCOUNT -> OrderSpecifier(Order.DESC, board.comments.size()) // 댓글이 많은 순으로 조회 -> 아직 구현 못함
            null -> OrderSpecifier(Order.DESC, board.id)  // request 에 ORDER 타입이 없는 경우 id 내림차순으로 정렬
        }
    }

    /*private fun getOrderSpecifier(orderType: ORDERTYPE?): OrderSpecifier<*> {
        return when(orderType) {
            ORDERTYPE.COUNT -> OrderSpecifier(Order.DESC, board.count) // 조회수가 높은 순으로 조회
            ORDERTYPE.ID_ASC -> OrderSpecifier(Order.ASC, board.id) // 가장 오래된 게시글 순으로 조회
            ORDERTYPE.ID_DESC -> OrderSpecifier(Order.DESC, board.id) // 가장 최근 게시글 순으로 조회
            ORDERTYPE.COMMENTCOUNT -> OrderSpecifier(Order.DESC, board.comments.size()).,
                                    OrderSpecifier(Order.DESC, board.id) // 댓글이 많은 순으로 조회 -> 아직 구현 못함
            null -> OrderSpecifier(Order.DESC, board.id)  // request 에 ORDER 타입이 없는 경우 id 내림차순으로 정렬
        }
    }*/
}