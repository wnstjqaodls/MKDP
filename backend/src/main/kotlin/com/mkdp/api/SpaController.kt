package com.mkdp.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/** Vue Router(history 모드) 새로고침 시 정적 index.html로 위임한다. API/actuator 경로는 제외. */
@Controller
class SpaController {
    @GetMapping(
        "/{path:^(?!api|actuator)[^.]*$}",
        "/{path:^(?!api|actuator)[^.]*$}/**",
    )
    fun forward(): String = "forward:/index.html"
}
