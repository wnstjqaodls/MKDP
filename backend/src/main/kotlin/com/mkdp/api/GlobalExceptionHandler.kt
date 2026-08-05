package com.mkdp.api

import com.mkdp.dart.DartApiException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NoSuchElementException::class)
    fun notFound(ex: NoSuchElementException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to (ex.message ?: "찾을 수 없습니다.")))

    @ExceptionHandler(
        IllegalArgumentException::class,
        ConstraintViolationException::class,
        MethodArgumentTypeMismatchException::class,
        MethodArgumentNotValidException::class,
    )
    fun badRequest(ex: Exception) =
        ResponseEntity.badRequest().body(mapOf("error" to (ex.message ?: "요청을 확인해주세요.")))

    @ExceptionHandler(DartApiException.InvalidKey::class)
    fun dartKeyError(ex: DartApiException.InvalidKey): ResponseEntity<Any> {
        log.error("DART 인증키 오류: {}", ex.message)
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(mapOf("error" to "외부 데이터 제공처 인증에 실패했습니다."))
    }

    @ExceptionHandler(DartApiException.RateLimited::class)
    fun dartRateLimited(ex: DartApiException.RateLimited): ResponseEntity<Any> {
        log.warn("DART 호출 한도 초과: {}", ex.message)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
            .body(mapOf("error" to "외부 데이터 제공처 호출 한도를 초과했습니다. 잠시 후 다시 시도해주세요."))
    }

    @ExceptionHandler(DartApiException::class)
    fun dartError(ex: DartApiException): ResponseEntity<Any> {
        log.warn("DART API 오류: {}", ex.message)
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(mapOf("error" to "외부 데이터 조회 중 오류가 발생했습니다."))
    }

    @ExceptionHandler(Exception::class)
    fun unexpected(ex: Exception): ResponseEntity<Any> {
        log.error("예상치 못한 오류", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to "서버 오류가 발생했습니다."))
    }
}
