package com.example.practicejpa

import com.example.practicejpa.domain.Member2
import com.example.practicejpa.domain.MemberPoint2
import com.example.practicejpa.repository.Member2Repository
import com.example.practicejpa.repository.MemberPoint2Repository
import com.example.practicejpa.service.MemberService
import com.example.practicejpa.service.dto.MemberRequest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("local")
class MemberTests(
    @Autowired private val memberService: MemberService,
    @Autowired private val member2Repository: Member2Repository,
    @Autowired private val memberPoint2Repository : MemberPoint2Repository
) {

    @Test
    @DisplayName("")
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
}