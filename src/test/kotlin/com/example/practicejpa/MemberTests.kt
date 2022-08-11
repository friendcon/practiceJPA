package com.example.practicejpa

import com.example.practicejpa.domain.Member2
import com.example.practicejpa.domain.MemberPoint2
import com.example.practicejpa.repository.Member2Repository
import com.example.practicejpa.repository.MemberPoint2Repository
import com.example.practicejpa.service.MemberService
import com.example.practicejpa.service.dto.MemberRequest
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@SpringBootTest
@ActiveProfiles("local")
class MemberTests(
    @Autowired private val memberService: MemberService,
    @Autowired private val member2Repository: Member2Repository,
    @Autowired private val memberPoint2Repository : MemberPoint2Repository
) {

    @Test
    @DisplayName("user 생성")
    fun createUserTest() {
        for(i in 1..100) {
            val point = MemberPoint2(
                member = Member2(
                    "password$i",
                    "$i@email.com",
                    memberPoint2 = null
                )
            )
            memberPoint2Repository.save(point)
        }
    }
    @Test
    @DisplayName("회원가입 및 포인트 생성")
    fun memberSaveTest() {
        val memberRequest = MemberRequest(
            "email",
            "password"
        )
        memberService.createMember(memberRequest)
    }

    @Test
    @DisplayName("회원테스트2")
    fun MemberTests2() {
        val mem = Member2(
            "pw",
            "email"
        )
        val point2 = MemberPoint2(
            member = mem
        )
        memberPoint2Repository.save(point2)
    }

    @Test
    @Transactional
    @DisplayName("회원테스트3")
    fun getMemberPoint() {
        val response = member2Repository.findById(1L).get().memberPoint2
        println(response.toString())
    }

    @Test
    fun findUserInfo() {
        member2Repository.findUserInfo(1L).forEach {
            println(it.memberPoint2)
        }
    }

    @Test
    @DisplayName("멤버수계산")
    fun countMemberTest() {
        val response = member2Repository.funGetMemberCount()
        println(response)
        Assertions.assertThat(response).isEqualTo(200L)
    }

    @Test
    @DisplayName("JPA Repository 가 기본으로 제공하는 함수 사용해보기")
    fun countMethodTest() {
        val response = member2Repository.count();
        println(response)
    }

    /**
     * plus() : 더하기
     * minus() : 빼기
     * times() : 곱하기
     * div() : 나누기
     * mod() : 나머지를 제외한 몫
     * rem() : 나머지
     */
    @Test
    @DisplayName("퍼센트 계산한 후 올림하기")
    fun countMember() {
        val memCount = 1723L
        val rate = 0.03
        // 반올림 전
        val result1 = BigDecimal.valueOf(memCount).multiply(BigDecimal.valueOf(rate))
        // 반올림 후
        val result2 = BigDecimal.valueOf(memCount).multiply(BigDecimal.valueOf(rate)).setScale(0, RoundingMode.HALF_UP)
        println("반올림 전 결과 $result1")
        println("반올림 후 결과 $result2")


    }

}