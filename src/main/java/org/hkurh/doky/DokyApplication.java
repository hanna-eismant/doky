package org.hkurh.doky;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.SignatureAlgorithm;


@SpringBootApplication
public class DokyApplication {

    public static final String SECRET_KEY = "dokySecretKey-hanna.kurhuzenkava-project";
    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public static void main(String[] args) {
        SpringApplication.run(DokyApplication.class, args);
    }


}
