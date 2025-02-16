package org.hkurh.doky

import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import javax.crypto.spec.SecretKeySpec

@SpringBootApplication
class IndexerServiceApplication {

    companion object {
        private const val SECRET_KEY = "dokySecretKey-hanna.kurhuzenkava-project"
        val SECRET_KEY_SPEC = SecretKeySpec(SECRET_KEY.toByteArray(), SignatureAlgorithm.HS256.jcaName)
    }
}

fun main(args: Array<String>) {
    runApplication<IndexerServiceApplication>(*args)
}
