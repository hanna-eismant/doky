package org.hkurh.doky

import org.hkurh.doky.email.EmailService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode

@ActiveProfiles("test")
@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = ["classpath:sql/create_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["classpath:sql/cleanup_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class EmailSpec() {

    @Autowired
    lateinit var emailService: EmailService

    @Test
    fun sendRegistrationEmail() {
        val user = UserEntity().apply {
            uid = "hkurh.test.01@yopmail.com"
            name = "Hanna Kurh"
        }
        emailService.sendRegistrationConfirmationEmail(user)
    }
}
