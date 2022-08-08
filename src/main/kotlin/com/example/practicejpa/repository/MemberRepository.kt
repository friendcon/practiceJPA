package com.example.practicejpa.repository

import com.example.practicejpa.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {
}