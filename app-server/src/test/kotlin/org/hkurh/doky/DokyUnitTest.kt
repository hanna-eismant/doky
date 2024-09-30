package org.hkurh.doky

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.suite.api.IncludeTags
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
@Tag("unit")
@Suite
@SuiteDisplayName("Unit Tests")
@IncludeTags("unit")
interface DokyUnitTest
