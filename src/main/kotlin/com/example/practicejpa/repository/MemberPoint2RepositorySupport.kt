package com.example.practicejpa.repository

import com.example.practicejpa.service.dto.PointRankingResponse
import com.querydsl.core.Tuple

interface MemberPoint2RepositorySupport {
    // fun findTopPercentPoint(count: Long): List<MemberPoint2>
    fun findTop5PercentPoint(count: Long): List<Tuple>

    // Propagation 을 이용한 데이터 가져오기
    fun findTopRankPoint(count: Long): List<PointRankingResponse>
}