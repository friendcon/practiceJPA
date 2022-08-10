package com.example.practicejpa.service

import com.example.practicejpa.domain.Member
import com.example.practicejpa.domain.MemberPoint
import com.example.practicejpa.repository.MemberRepository
import com.example.practicejpa.service.dto.MemberRequest
import com.example.practicejpa.service.dto.MemberResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MemberService(
    @Autowired private val memberRepository: MemberRepository
) {
    fun createMember(request: MemberRequest): MemberResponse {
        return memberRepository.save(request.toEntity()).toResponse()
    }

    fun MemberRequest.toEntity(): Member {
        return Member(
            pw,
            email,
            MemberPoint()
        )
    }
}