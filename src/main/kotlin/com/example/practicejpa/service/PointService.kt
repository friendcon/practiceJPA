package com.example.practicejpa.service

import com.example.practicejpa.repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class PointService(
    private val member2Repository: Member2Repository,
    private val memberPoint2Repository: MemberPoint2Repository,
    private val memberPointHistoryRepository: MemberPointHistoryRepository
) {
    companion object {
        // multiple 은 BigDecimal 로 선언할 필요가 없는 것 같지만 일단.. 0.03 으로 선언
        private val RATE = BigDecimal.valueOf(0.03)
        private val FIRST_CLASS_POINT = BigDecimal.valueOf(1000)
        private val SECOND_CLASS_POINT = BigDecimal.valueOf(800)
        private val THIRD_CLASS_POINT = BigDecimal.valueOf(600)
        private val GENERAL_CLASS_POINT = BigDecimal.valueOf(200)
    }

    /** Requirement 1.
     * [요구사항1] 한달에 한번 전체 회원 중 포인트를 많이 가지고있는 상위 5%에게 포인트를 지급한다
     * [요구사항2] 동일 포인트를 가지고 있다면 게시글 수를 비교해서 게시글을 더 많이 작성한 회원이 더 상위 랭킹회원임
     * - 1등 회원에게는 1000 포인트 지급
     * - 2등 회원에게는 800 포인트 지급
     * - 3등 회원에게는 400 포인트 지급
     * - 나머지 회원에게는 100 포인트를 지급
     */
    @Transactional
    fun rewardPoint() {
        /**
         * 1. 전체 회원수를 가지고 온다
         * 2. findTopRankPoint 함수에 1에서 계산한 전체 회원수를 넘겨준다
         *  2.1 전체 회원수의 일정 비율을 구했을 때 소숫점으로 나온다면 [반올림]한 다음 해당 회원 수 만큼 포인트를 보상해준다
         * 3. 리턴받은 PointRankingResponse 에서 pointWallet update 를 실행시킨다
         *  3.1 PointHistory 기록을 위해 Enum 에 REWARD 를 추가해준다
         */
        val memCount = memberPoint2Repository.count() // 전체 회원 수
        println("전체 회원의 수 : $memCount")
        // rewardMemberCount = 포인트 Reward 를 받을 회원의 수
        val rewardMemberCount = BigDecimal.valueOf(memCount).multiply(RATE).setScale(0, RoundingMode.HALF_UP).toLong()
        println("포인트 리워드를 받을 회원의 수 : $rewardMemberCount")
        // reward 받을 회원만큼만 데이터를 요청
        val response = memberPoint2Repository.findTopRankPoint(rewardMemberCount)
        response.forEach {
            println("포인트 요청을 받을 회원목록 $it")
        }

        for(i in response.indices) {
            val point = memberPoint2Repository.findById(response[i].pointId).get()
            when(i) {
                1 -> point.addRewardPoint(FIRST_CLASS_POINT)
                2 -> point.addRewardPoint(SECOND_CLASS_POINT)
                3 -> point.addRewardPoint(THIRD_CLASS_POINT)
                else -> point.addRewardPoint(GENERAL_CLASS_POINT)
            }
        }

        val response2 = memberPointHistoryRepository.findTop10ByOrderByCreatedDateDesc()
        response2.forEach {
            println(it.toString())
        }
    }
}