package org.hkurh.doky

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IndexerServiceApplication

fun main(args: Array<String>) {
    runApplication<IndexerServiceApplication>(*args)
}
