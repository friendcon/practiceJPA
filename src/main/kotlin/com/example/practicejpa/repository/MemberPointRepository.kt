package com.example.practicejpa.repository

import com.example.practicejpa.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPointRepository: JpaRepository<Member, Long> {
}