package org.hkurh.doky.documents.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

@Tag(name = "Documents")
@SecurityRequirement(name = "Bearer Token")
interface DocumentApi {
    @Operation(summary = "Upload file to document entry")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "File is uploaded and attached to document"),
            ApiResponse(responseCode = "404", description = "Document with provided id does not exist"))
    fun uploadFile(@RequestBody file: MultipartFile, @PathVariable id: String): ResponseEntity<*>?

    @Operation(summary = "Download file attached to document entry")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "File is sent to client and can be downloaded",
                    content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)],
                    headers = [Header(name = "attachment; filename=...")]),
            ApiResponse(responseCode = "404", description = "No document with requested id, or no attached file for document"))
    @Throws(IOException::class)
    fun downloadFile(@PathVariable id: String): ResponseEntity<*>?

    @Operation(summary = "Create document with metadata")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Document is created"))
    fun create(@RequestBody document: DocumentRequest): ResponseEntity<*>?

    @Operation(summary = "Update document with metadata")
    @ApiResponses(ApiResponse(responseCode = "200", description = "Document is updated"))
    fun update(@PathVariable id: String, document: DocumentRequest): ResponseEntity<*>?

    @ApiResponses(
            ApiResponse(responseCode = "200", description = "List if documents is retrieved successfully",
                    content = [Content(array = ArraySchema(schema = Schema(implementation = DocumentResponse::class)))]))
    @Operation(summary = "Get all documents that was created by current customer")
    fun getAll(): ResponseEntity<*>?

    @Operation(summary = "Get metadata for document")
    @ApiResponses(
            ApiResponse(responseCode = "200", description = "Document information is retrieved successfully",
                    content = [Content(schema = Schema(implementation = DocumentResponse::class))]),
            ApiResponse(responseCode = "404", description = "No document with provided id"))
    operator fun get(@PathVariable id: String): ResponseEntity<*>?
}
