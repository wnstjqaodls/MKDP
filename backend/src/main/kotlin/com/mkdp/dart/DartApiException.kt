package com.mkdp.dart

/**
 * DART 응답의 `status` 코드를 도메인 예외로 변환한다.
 *
 * v1(레거시)은 DART 응답을 파싱하지 않고 raw String을 그대로 클라이언트에 돌려줬다 —
 * 인증키가 만료되든 한도를 초과하든 호출부는 알 방법이 없었다.
 */
sealed class DartApiException(message: String) : RuntimeException(message) {
    class InvalidKey(message: String) : DartApiException(message)

    class NoData(message: String) : DartApiException(message)

    class RateLimited(message: String) : DartApiException(message)

    class Unknown(val dartStatus: String, message: String) : DartApiException(message)
}
