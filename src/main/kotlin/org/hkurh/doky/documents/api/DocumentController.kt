/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2005
 *  - Hanna Kurhuzenkava (hanna.kuehuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see [Hyperlink removed
 * for security reasons]().
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
import org.springframework.web.bind.annotation.*
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

    @PostMapping("/{id}/upload")
    @Trace(operationName = "document.upload")
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
    @Trace(operationName = "document.download")
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
    @Trace(operationName = "document.create")
    override fun create(@RequestBody @Valid document: DocumentRequest): ResponseEntity<*> {
        val createdDocument = documentFacade.createDocument(document.name, document.description)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").build(createdDocument!!.id)
        return ResponseEntity.created(resourceLocation).build<Any>()
    }

    @PutMapping("/{id}")
    @Trace(operationName = "document.update")
    override fun update(@PathVariable id: Long, @RequestBody @Valid document: DocumentRequest): ResponseEntity<*>? {
        documentFacade.update(id, document)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping("/{id}")
    @Trace(operationName = "document.get.single")
    override fun get(@PathVariable id: Long): ResponseEntity<*> {
        val document = documentFacade.findDocument(id)
        return if (document != null) {
            ResponseEntity.ok(document)
        } else {
            LOG.debug { "No Document with id [$id]" }
            ResponseEntity.notFound().build<Any>()
        }
    }

    @GetMapping
    @Trace(operationName = "document.get.all")
    override fun getAll(): ResponseEntity<*> {
        val documents = documentFacade.findAllDocuments()
        return ResponseEntity.ok(documents)
    }

    companion object {
        private val LOG = KotlinLogging.logger {}
    }
}
