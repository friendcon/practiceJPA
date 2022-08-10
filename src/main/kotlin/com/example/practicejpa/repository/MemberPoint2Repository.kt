package com.example.practicejpa.repository

import com.example.practicejpa.domain.MemberPoint2
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPoint2Repository: JpaRepository<MemberPoint2, Long> {
    fun findByMemberId(id: Long): MemberPoint2
}