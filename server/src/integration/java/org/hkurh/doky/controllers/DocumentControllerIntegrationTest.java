package org.hkurh.doky.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Documents endpoint integration test")
class DocumentControllerIntegrationTest extends AbstractIntegrationTest {

    private static final String DOCUMENT_NAME_PROPERTY = "name";
    private static final String DOCUMENT_DESCRIPTION_PROPERTY = "description";
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

    @Test
    @DisplayName("Create new document")
    void shouldCreateNewDocument_whenRequestIsCorrect() throws JSONException {
        var documentBody = new JSONObject();
        documentBody.put(DOCUMENT_NAME_PROPERTY, NEW_DOCUMENT_NAME);
        documentBody.put(DOCUMENT_DESCRIPTION_PROPERTY, NEW_DOCUMENT_DESCRIPTION);

        var requestEntity = new HttpEntity<>(documentBody.toString(), httpHeaders);
        var responseEntity = restTemplate.exchange(documentsBaseEndpoint, HttpMethod.POST, requestEntity, Object.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
