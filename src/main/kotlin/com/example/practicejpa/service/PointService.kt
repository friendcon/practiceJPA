package com.example.practicejpa.service

import com.example.practicejpa.repository.MemberPointRepository
import org.springframework.stereotype.Service

@Service
class PointService(
    private val memberPointRepository: MemberPointRepository
) {

}