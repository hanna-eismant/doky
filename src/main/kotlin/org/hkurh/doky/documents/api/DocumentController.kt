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

import datadog.trace.api.Trace
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.hkurh.doky.documents.DocumentFacade
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException

/**
 * Controller class for handling document-related operations.
 */
@RestController
@RequestMapping("/api/documents")
@PreAuthorize("hasRole('ROLE_USER')")
class DocumentController(private val documentFacade: DocumentFacade) : DocumentApi {

    private final val log = KotlinLogging.logger {}

    @PostMapping("/{id}/upload")
    @Trace(operationName = "doky.documents.upload")
    override fun uploadFile(@RequestBody file: MultipartFile, @PathVariable id: Long): ResponseEntity<*> {
        documentFacade.saveFile(id, file)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping("/{id}/download/token")
    override fun getDownloadToken(@PathVariable id: Long): ResponseEntity<DownloadTokenResponse>? {
        val token = documentFacade.generateDownloadToken(id)
        return ResponseEntity.ok(DownloadTokenResponse(token))
    }

    @PostMapping("/{id}/download")
    @Trace(operationName = "doky.documents.download")
    @Throws(IOException::class)
    override fun downloadFile(
        @PathVariable id: Long,
        @RequestBody downloadFileRequest: DownloadFileRequest
    ): ResponseEntity<*> {
        return try {
            val file = documentFacade.getFile(id, downloadFileRequest.token)
            val header = "attachment; filename=\"${file.filename}\""
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, header)
                .body(file)
        } catch (e: MalformedURLException) {
            ResponseEntity.internalServerError().build<Any>()
        }
    }

    @PostMapping
    @Trace(operationName = "doky.documents.create")
    override fun create(@RequestBody @Valid document: DocumentRequest): ResponseEntity<*> {
        val createdDocument = documentFacade.createDocument(document.name, document.description)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").build(createdDocument!!.id)
        return ResponseEntity.created(resourceLocation)
            .header("Access-Control-Expose-Headers", "Location")
            .build<Any>()
    }

    @PutMapping("/{id}")
    @Trace(operationName = "doky.documents.update")
    override fun update(@PathVariable id: Long, @RequestBody @Valid document: DocumentRequest): ResponseEntity<*>? {
        documentFacade.update(id, document)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping("/{id}")
    @Trace(operationName = "doky.documents.get.single")
    override fun get(@PathVariable id: Long): ResponseEntity<*> {
        val document = documentFacade.findDocument(id)
        return if (document != null) {
            ResponseEntity.ok(document)
        } else {
            log.debug { "No Document with id [$id]" }
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping
    @Trace(operationName = "doky.documents.get.all")
    override fun getAll(): ResponseEntity<*> {
        val documents = documentFacade.findAllDocuments()
        log.debug { "Get [${documents.size}] documents" }
        return ResponseEntity.ok(documents)
    }

    @PostMapping("/search")
    @Trace(operationName = "doky.documents.search")
    override fun search(@RequestBody documentSearchRequest: DocumentSearchRequest): ResponseEntity<*>? {
        val query = documentSearchRequest.query?.trim().takeUnless { it.isNullOrEmpty() } ?: "*"
        val page = documentSearchRequest.page ?: Page(number = 0, size = 10)
        val sort = documentSearchRequest.sort ?: Sort(property = "id", direction = SortDirection.ASC)

        val searchResponse = documentFacade.search(query, page, sort)
        return ResponseEntity.ok(searchResponse)
    }
}
