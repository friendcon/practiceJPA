package com.example.practicejpa.repository

import com.example.practicejpa.domain.Member2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Member2Repository: JpaRepository<Member2, Long>{
    // 멤버와 멤버 Point 같이 조회
    @Query(value = "select mem " +
            "from Member2 mem join fetch mem.memberPoint2")
    fun findUserInfo(@Param("id") id: Long): List<Member2>

    /**
     * JPQL 작성하는 대신 Repository의 count 함수를 사용하자!!!!!!
     */
    @Query(value = "select count(mem) from Member2 mem")
    fun funGetMemberCount(): Long

}