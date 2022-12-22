package org.hkurh.doky.controllers;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hkurh.doky.AbstractIntegrationTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Documents endpoint integration test")
class DocumentControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String DOCUMENT_ID_PROPERTY = "id";
    private static final String DOCUMENT_NAME_PROPERTY = "name";
    private static final String DOCUMENT_DESCRIPTION_PROPERTY = "description";
    private static final String EXISTED_DOCUMENT_NAME_FIRST = "Test";
    private static final String EXISTED_DOCUMENT_NAME_SECOND = "Test2";
    private static final String NEW_DOCUMENT_NAME = "Apples";
    private static final String NEW_DOCUMENT_DESCRIPTION = "Do you like apples?";
    private String documentsBaseEndpoint;
    private String documentsUploadEndpoint;
    private String documentsDownloadEndpoint;

    @BeforeEach
    void setup() throws JSONException {
        documentsBaseEndpoint = BASE_HOST + port + "/documents";
        documentsUploadEndpoint = documentsBaseEndpoint + "/{id}/upload";
        documentsDownloadEndpoint = documentsBaseEndpoint + "/{id}/download";

        var token = login();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(AUTHORIZATION_HEADER, token);
    }

    private Long getDocumentId() {
        var existedDocumentQuery = "select d.id from document d where d.name = ?";
        var args = new Object[]{EXISTED_DOCUMENT_NAME_FIRST};
        var argTypes = new int[]{Types.VARCHAR};
        var id = jdbcTemplate.queryForObject(existedDocumentQuery, args, argTypes, Long.class);
        return id;
    }

    @Test
    @DisplayName("Should create new document")
    void shouldCreateNewDocument_whenRequestIsCorrect() throws JSONException {
        var documentBody = new JSONObject();
        documentBody.put(DOCUMENT_NAME_PROPERTY, NEW_DOCUMENT_NAME);
        documentBody.put(DOCUMENT_DESCRIPTION_PROPERTY, NEW_DOCUMENT_DESCRIPTION);

        var request = new HttpEntity<>(documentBody.toString(), httpHeaders);
        var response = restTemplate.exchange(documentsBaseEndpoint, HttpMethod.POST, request, Object.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getHeaders().containsKey(LOCATION_HEADER), "Response should contain Location header");
        assertTrue(StringUtils.isNotBlank(response.getHeaders().get(LOCATION_HEADER).get(0)), "Header Location should not be empty");
    }

    @Test
    @DisplayName("Should return document by id when exists")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/DocumentControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void shouldReturnDocument_whenExists() {
        var documentId = getDocumentId();
        var resourceEndpoint = documentsBaseEndpoint + "/" + documentId;

        var request = new HttpEntity<>(null, httpHeaders);
        var response = restTemplate.exchange(resourceEndpoint, HttpMethod.GET, request, LinkedHashMap.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var responseBody = response.getBody();
        assertNotNull(responseBody, "Response should contain body");
        assertTrue(responseBody.containsKey(DOCUMENT_ID_PROPERTY), "Response body should contain document id");
        var actualId = responseBody.get(DOCUMENT_ID_PROPERTY);
        assertEquals(documentId, NumberUtils.createLong(actualId.toString()), "Incorrect document id");
        assertTrue(responseBody.containsKey(DOCUMENT_DESCRIPTION_PROPERTY), "Response body should contain document name");
        assertEquals(EXISTED_DOCUMENT_NAME_FIRST, responseBody.get(DOCUMENT_NAME_PROPERTY), "Incorrect document name");
    }

    @Test
    @DisplayName("Should get all existing documents")
    @SqlGroup({
            @Sql(scripts = "classpath:sql/DocumentControllerIntegrationTest/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void shouldReturnAllDocuments() {
        var request = new HttpEntity<>(null, httpHeaders);
        var response = restTemplate.exchange(documentsBaseEndpoint, HttpMethod.GET, request, ArrayList.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        var responseBody = response.getBody();
        assertNotNull(responseBody, "Response should contain body");
        assertTrue(responseBody.stream().anyMatch(getDocumentNameMatcher(EXISTED_DOCUMENT_NAME_FIRST)), "Response should contain 'Test' document");
        assertTrue(responseBody.stream().anyMatch(getDocumentNameMatcher(EXISTED_DOCUMENT_NAME_SECOND)), "Response should contain 'Test2' document");
    }

    private static Predicate getDocumentNameMatcher(String expectedName) {
        return item -> {
            var asMap = (LinkedHashMap) item;
            if (!asMap.containsKey(DOCUMENT_NAME_PROPERTY)) {
                return false;
            } else {
                var actualName = asMap.get(DOCUMENT_NAME_PROPERTY);
                return expectedName.equals(actualName);
            }
        };
    }

    @Test
    @DisplayName("Should get 'No Content' when no existing documents")
    void shouldReturnNoContent_whenNoDocuments() {

    }
}
