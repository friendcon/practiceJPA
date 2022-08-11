package com.example.practicejpa

import com.example.practicejpa.repository.MemberPoint2Repository
import com.example.practicejpa.service.PointService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class PointTests(
    @Autowired private val memberPoint2Repository: MemberPoint2Repository,
    @Autowired private val pointService: PointService
) {
    @Test
    @DisplayName("find top point")
    fun findTopPoint() {
        val response = memberPoint2Repository.findTopPoint()
        for(i in 1..20) {
            println(response[i].toString())
        }
    }

    @Test
    @DisplayName("find top point 2")
    fun findTopPoint2() {
        val response = memberPoint2Repository.findTop5PercentPoint(5)
        response.forEach {
            println(it.toString())
        }
    }

    @Test
    @DisplayName("find top point 3")
    fun findTopPoint3() {
        val response = memberPoint2Repository.findTopRankPoint(110)
        response.forEach {
            println(it.toString())
        }
    }

    @Test
    @DisplayName("포인트를 받게 될 회원 목록 조회")
    fun rewardPoint1Test() {
        pointService.rewardPoint()
    }




}