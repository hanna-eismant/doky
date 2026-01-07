/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

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
        ApiResponse(responseCode = "404", description = "Document with provided id does not exist")
    )
    fun uploadFile(@RequestBody file: MultipartFile, @PathVariable id: Long): ResponseEntity<*>?

    @Operation(summary = "Get token to authorize file download")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Token is generated and provided"),
        ApiResponse(responseCode = "404", description = "Document with provided id does not exist")
    )
    fun getDownloadToken(@PathVariable id: Long): ResponseEntity<DownloadTokenResponse>?

    @Operation(summary = "Download file attached to document entry")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "File is sent to client and can be downloaded",
            content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)],
            headers = [Header(name = "attachment; filename=...")]
        ),
        ApiResponse(responseCode = "401", description = "Token is invalid or expired"),
        ApiResponse(
            responseCode = "404",
            description = "No document with requested id, or no attached file for document"
        )
    )
    @Throws(IOException::class)
    fun downloadFile(@PathVariable id: Long, @RequestBody downloadFileRequest: DownloadFileRequest): ResponseEntity<*>?

    @Operation(summary = "Create document with metadata")
    @ApiResponses(ApiResponse(responseCode = "201", description = "Document is created"))
    fun create(@RequestBody document: DocumentRequest): ResponseEntity<*>?

    @Operation(summary = "Update document with metadata")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Document is updated"),
        ApiResponse(responseCode = "404", description = "No document with provided id")
    )
    fun update(@PathVariable id: Long, document: DocumentRequest): ResponseEntity<*>?

    @Operation(summary = "Get metadata for document")
    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "Document information is retrieved successfully",
            content = [Content(schema = Schema(implementation = DocumentResponse::class))]
        ),
        ApiResponse(responseCode = "404", description = "No document with provided id")
    )
    operator fun get(@PathVariable id: Long): ResponseEntity<*>?

    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "List if documents is retrieved successfully",
            content = [Content(array = ArraySchema(schema = Schema(implementation = DocumentResponse::class)))]
        )
    )
    @Operation(summary = "Get all documents that was created by current customer")
    fun getAll(): ResponseEntity<*>?

    @ApiResponses(
        ApiResponse(
            responseCode = "200", description = "List of documents is retrieved successfully",
        )
    )
    @Operation(summary = "Search for documents")
    fun search(@RequestBody documentSearchRequest: DocumentSearchRequest): ResponseEntity<*>?
}
