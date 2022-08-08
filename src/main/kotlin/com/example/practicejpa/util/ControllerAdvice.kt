package com.example.practicejpa.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.NoSuchElementException
import javax.servlet.http.HttpServletRequest


@RestControllerAdvice
class ControllerAdvice {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return getResponseEntity(HttpStatus.NOT_FOUND, request, e, "유효성 검사를 통과하지 못했습니다")
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun noSuchElementException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return getResponseEntity(HttpStatus.NOT_FOUND, request, e, e.message)
    }

    @ExceptionHandler(UserBoardAuthException::class)
    fun userAuthException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse>{
        return getResponseEntity(HttpStatus.FORBIDDEN, request, e, e.message)
    }

    private fun getResponseEntity(
        httpStatus: HttpStatus,
        request: HttpServletRequest,
        e: Exception,
        message: String? = null
    ): ResponseEntity<ErrorResponse> {
        return ErrorResponse(
            LocalDateTime.now(),
            httpStatus.value(),
            message ?: e.localizedMessage ?: "message not specified",
            request.requestURI
        ).let {
            logger.warn("[FROM ${request.remoteAddr}] $it")
            ResponseEntity.status(httpStatus).body(it)
        }
    }
}