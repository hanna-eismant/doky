Doky – Project‑specific development guidelines

Audience: Advanced developers familiar with Kotlin, Spring Boot, Gradle, and modern frontend testing. This document captures only repo‑specific knowledge verified during this session.

1. Build and configuration (backend)
- Toolchain: Java 17, Kotlin + Spring Boot, Gradle Kotlin DSL.
  - The project enforces Java 17 via Gradle toolchain (build.gradle.kts -> java.toolchain).
  - Kotlin compiler enables strict nullity interop: -Xjsr305=strict.
- Build commands:
  - Clean and compile: ./gradlew clean build
  - Skip tests (CI fast build): ./gradlew build -x test
  - Run app locally with profile: ./gradlew bootRun --args='--spring.profiles.active=local'
    - The local profile adjusts logging and SPA routing, but you must still provide DB and other infra if the code path uses them.
- Configuration files:
  - src/main/resources/application.properties – defaults for prod‑like runtime (includes email, Kafka, Azure Search, etc.). Contains placeholders <CHANGE_ME> that must be externalized for real deployments.
  - src/main/resources/application-local.properties – local overrides for development (e.g., logging, SPA routing, Kafka topic name).
  - src/main/resources/application-test.properties – test profile values. Notable points:
    - DB is configured for MySQL on localhost:3309, DB=doky-test, user/pass=doky-test.
    - Embedded Kafka is used via spring.embedded.kafka.brokers (from spring-kafka-test), protocol PLAINTEXT.
    - SMTP is pointed to localhost:2525 for GreenMail.
    - Azure Search endpoint mocked via https://localhost:8443.
- External services for local/dev:
  - MySQL (used by app/test profile). Example to bring up a disposable instance:
    docker run --name doky-mysql -p 3309:3306 -e MYSQL_DATABASE=doky-test -e MYSQL_USER=doky-test -e MYSQL_PASSWORD=doky-test -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0
  - SMTP (test profile): GreenMail is handled by tests; the app itself may require smtp if you hit email paths.
  - Kafka: For unit tests, a mock/embedded broker is used. For running the app, set spring.kafka.* and doky.kafka.* appropriately or disable consumers where applicable.
  - Azure App Config: README suggests using Azure App Configuration; keep secrets out of repo and provide via env/KeyVault/AppConfig.
- TLS for WireMock in integration tests:
  - build.gradle.kts configures javax.net.ssl.trustStore to wiremock-cert/wiremock-truststore.jks for the integrationTest suite.

2. Testing (backend)
- JUnit 5 is used; Mockito (kotlin), Spring Boot test starters, Testcontainers libraries are present. Gradle test suites are configured:
  - Unit tests (default): ./gradlew test
    - Uses JUnit Platform, logs PASSED/SKIPPED/FAILED. No external services required if tests remain unit-level.
  - Integration tests: ./gradlew integrationTest
    - Declared as TestSuiteType.INTEGRATION_TEST in build.gradle.kts.
    - Suite sets trust store for WireMock TLS. Dependencies include Testcontainers (core/junit/wiremock), RestAssured, Spring Kafka test, GreenMail.
    - Prerequisites: Docker daemon should be running for tests that leverage Testcontainers. If any tests bind to application-test.properties DB, ensure the MySQL container above is running on 3309.
  - API (functional) tests: ./gradlew apiTest
    - Declared as TestSuiteType.FUNCTIONAL_TEST, uses RestAssured, Spring Boot starters. These tests may boot the app or hit endpoints depending on implementation.
- Running a single test or class:
  - Default unit tests: ./gradlew test --tests 'org.hkurh.doky.SomeTestClass'
  - For other suites: ./gradlew integrationTest --tests 'org.hkurh.doky.IntegrationSpec'
- Profiles during tests:
  - Many Spring tests implicitly use the test profile. application-test.properties configures MySQL, SMTP, embedded Kafka, and Azure Search. Ensure environmental expectations are met before running integration/api suites.
- Mail and Kafka during tests:
  - spring.kafka.* points to embedded brokers in test profile. GreenMail (on port 2525) is instantiated by tests that need it; you generally don’t need an external SMTP.

3. Frontend E2E (Cypress)
- Cypress project is under doky-front with config doky-front/cypress.config.js.
  - It pings the production site (baseUrl=https://doky.azurewebsites.net) before tests and expects a 401 from backend user endpoint via Azure APIM. Internet access is required.
- Running E2E as configured for the cloud project:
  - From repo root: ./e2e.sh
    - This will cd doky-front and run npx cypress run --record with the configured project key.
  - Alternatively: cd doky-front && npx cypress run
- Local E2E against a local backend/frontend:
  - You can override the baseUrl without changing the file by setting CYPRESS_baseUrl or passing --config baseUrl=http://localhost:3000.
  - If you run against local backend, adapt the pre-run ping expectations in cypress.config.js or disable the before:run ping.

4. Demonstrated example: adding and running a simple test
- Example test we used (removed afterwards as per task requirement):
  - File: src/test/kotlin/org/hkurh/doky/SampleSanityTest.kt
  - Contents:
    - A minimal JUnit 5 test asserting 1 + 1 == 2.
- How we executed it:
  - Ran the test directly via the test runner and verified it passed.
  - You can reproduce with: ./gradlew test --tests 'org.hkurh.doky.SampleSanityTest'
- Cleanup: Remove the sample file once you’ve verified your environment; keep the test tree focused on real coverage.

5. Additional development notes
- Code style/conventions:
  - Kotlin with strict nullability interop (-Xjsr305=strict). Prefer Kotlin idioms, data classes for DTOs, and constructor injection for services.
  - Logging configured in application.properties and -local.properties; DEBUG for org.hkurh.doky in local/test. Avoid noisy logs in production.
- API documentation:
  - OpenAPI starter is included; swagger-ui at /swagger-ui.html and api-docs at /api-docs when the app runs.
  - The repo includes open-api.json at the root; keep it in sync when API changes.
- Security/JWT:
  - JWT secret key and Spring basic user credentials are defined in application.properties for dev. Do not commit real secrets; prefer env vars or Azure App Config/KeyVault bindings.
- Kafka and email:
  - application.properties enables Kafka email consumer autostart. In local dev where Kafka isn’t present, disable consumers or set spring.kafka properties to a dev broker to avoid startup failures.
- Test data and migrations:
  - Flyway migrations use locations based on profile (e.g., migration/mysql for tests, migration/sqlserver for default). Ensure the target DB matches the chosen migrations.

6. Quick recipes
- Run unit tests only (fast):
  ./gradlew clean test
- Run integration tests (Docker required if Testcontainers are used by tests):
  ./gradlew integrationTest
- Run functional API tests:
  ./gradlew apiTest
- Start local MySQL for test profile compatibility:
  docker run --name doky-mysql -p 3309:3306 -e MYSQL_DATABASE=doky-test -e MYSQL_USER=doky-test -e MYSQL_PASSWORD=doky-test -e MYSQL_ROOT_PASSWORD=root -d mysql:8.0
- Run the app locally:
  ./gradlew bootRun --args='--spring.profiles.active=local'

If you encounter suite-specific failures, check:
- Whether Docker is running (for integration/api tests using Testcontainers).
- Whether MySQL is available on 3309 if a test binds to application-test.properties.
- WireMock truststore path (auto-set in build.gradle.kts for integrationTest).
- Cypress baseUrl and network reachability for E2E.

7. Frontend (doky-front)
- Tech stack: React 18 + Webpack 5 + MUI 6, ESLint. NPM is used (package-lock.json present).
- Install dependencies:
  cd doky-front && npm ci
  - If you don’t use a clean CI cache, npm install also works.
- Running the dev server (Webpack Dev Server, port 10010):
  - Default (auto backend): npm start
  - Explicit modes (selects API BASE_URL via Webpack alias config):
    - Local backend (http://localhost:8080): npm run start:local
    - Auto (cloud via APIM): npm run start:auto
    - Remote (cloud via APIM): npm run start:remote
  - Notes:
    - historyApiFallback is enabled; SPA routes work on refresh.
    - The app alias resolves config to src/api/config.{local|auto|remote}.js based on --env be-env.
- Building production bundle:
  - Local/default: npm run build (outputs to doky-front/dist)
  - Remote config: npm run build:remote
  - Serve the built bundle locally: npm run serve (serves dist/ via the serve package)
- Linting:
  - Run linter: npm run lint
  - Auto-fix: npm run lint:fix
- Backend integration during local dev:
  - Start backend on port 8080 (./gradlew bootRun --args='--spring.profiles.active=local').
  - Start frontend in local mode (npm run start:local) which sets BASE_URL to http://localhost:8080.
  - Frontend dev server runs on http://localhost:10010.
- Cypress E2E with frontend:
  - Cloud-configured E2E: ./e2e.sh (uses doky-front/cypress.config.js, pings prod and expects 401 on users/current).
  - To run against a local frontend/backend, override base URL: cd doky-front && npx cypress run --config baseUrl=http://localhost:10010
    - Or set env var CYPRESS_baseUrl=http://localhost:10010.
    - If you test locally, consider disabling or adjusting the before:run pings in cypress.config.js.
- Common pitfalls:
  - If API calls fail locally, ensure backend is on 8080 and you used start:local so BASE_URL resolves to localhost.
  - If SPA deep links 404 in non-dev servers, ensure your server is configured to serve index.html for unknown paths (dev server already uses historyApiFallback).
