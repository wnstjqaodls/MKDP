package com.mkdp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MkdpApplication

fun main(args: Array<String>) {
    runApplication<MkdpApplication>(*args)
}
