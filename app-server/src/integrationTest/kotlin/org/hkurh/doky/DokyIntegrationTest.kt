package org.hkurh.doky

import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@ActiveProfiles("test")
@Tag("integration")
@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = ["emails-test"])
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = ["classpath:sql/create_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["classpath:sql/cleanup_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DokyIntegrationTest
