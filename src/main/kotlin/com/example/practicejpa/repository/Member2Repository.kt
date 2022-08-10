package com.example.practicejpa.repository

import com.example.practicejpa.domain.Member2
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface Member2Repository: JpaRepository<Member2, Long>{
    @Query(value = "select mem " +
            "from Member2 mem join fetch mem.memberPoint2")
    fun findUserInfo(@Param("id") id: Long): List<Member2>
}