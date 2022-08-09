package com.example.practicejpa.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Configuration
class QueryDslConfig(
    // EntityManager는 고객의 요청이 올 때마다 (thread 생성될 때마다) EntityManager 생성
    @PersistenceContext private val entityManager: EntityManager
) {
    // 매니저마다 jpaQueryFactory 를 생성
    @Bean
    fun jpaQueryFactory(): JPAQueryFactory {
        return JPAQueryFactory(this.entityManager)
    }
}