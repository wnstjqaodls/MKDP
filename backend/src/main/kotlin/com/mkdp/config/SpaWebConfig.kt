package com.mkdp.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

/**
 * Vue Router(history 모드) 새로고침 시 정적 index.html로 위임한다.
 *
 * 이전에는 별도 컨트롤러에 경로 정규식으로 API/정적 자원을 걸러냈으나, 그 정규식이
 * 두 번째 세그먼트 이하는 검사하지 않아 assets 하위의 확장자 있는 파일까지 걸려버렸다.
 * 그 결과 JS와 CSS가 index.html(text 타입)로 대체되어 module script MIME 타입 오류로
 * 화면이 완전히 비어 보이는 버그가 있었다. 리소스 핸들러 체인에서 실제 파일이 있으면
 * 그대로 서빙하고 없으면 index.html로 대체하도록 판단하면 이런 문제가 없다. REST
 * 컨트롤러와 actuator 엔드포인트는 자체 핸들러 매핑이 이 리소스 핸들러보다 항상 먼저
 * 매칭되므로 영향받지 않는다.
 */
@Configuration
class SpaWebConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(
                object : PathResourceResolver() {
                    override fun getResource(resourcePath: String, location: Resource): Resource? {
                        val requested = location.createRelative(resourcePath)
                        return if (requested.exists() && requested.isReadable) {
                            requested
                        } else {
                            ClassPathResource("/static/index.html")
                        }
                    }
                },
            )
    }
}
