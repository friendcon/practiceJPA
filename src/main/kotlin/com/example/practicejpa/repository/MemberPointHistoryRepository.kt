package com.example.practicejpa.repository

import com.example.practicejpa.domain.MemberPointHistory
import org.springframework.data.jpa.repository.JpaRepository

interface MemberPointHistoryRepository: JpaRepository<MemberPointHistory, Long>{
}