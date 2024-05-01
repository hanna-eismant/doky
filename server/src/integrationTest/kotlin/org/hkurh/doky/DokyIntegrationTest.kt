package org.hkurh.doky

import org.junit.jupiter.api.Tag
import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode

@ActiveProfiles("test")
@Tag("integration")
@Suite
@SuiteDisplayName("Integration Tests")
@IncludeTags("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = ["classpath:sql/create_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["classpath:sql/cleanup_base_test_data.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DokyIntegrationTest
