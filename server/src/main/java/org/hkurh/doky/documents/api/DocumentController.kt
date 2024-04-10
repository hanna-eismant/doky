package org.hkurh.doky.documents.api

import org.hkurh.doky.documents.DocumentFacade
import org.hkurh.doky.security.DokyAuthority
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.IOException
import java.net.MalformedURLException

@RestController
@RequestMapping("/documents")
@Secured(DokyAuthority.Role.ROLE_USER)
class DocumentController(private val documentFacade: DocumentFacade) : DocumentApi {

    @PostMapping("/{id}/upload")
    override fun uploadFile(@RequestBody file: MultipartFile, @PathVariable id: String): ResponseEntity<*> {
        documentFacade.saveFile(file, id)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping("/{id}/download")
    @Throws(IOException::class)
    override fun downloadFile(@PathVariable id: String): ResponseEntity<*> {
        return try {
            val file = documentFacade.getFile(id)
            if (file == null) {
                LOG.debug("No attached file for document [$id]")
                return ResponseEntity.notFound().build<Any>()
            }
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
    override fun create(@RequestBody document: DocumentRequest): ResponseEntity<*> {
        val createdDocument = documentFacade.createDocument(document.name, document.description)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").build(createdDocument!!.id)
        return ResponseEntity.created(resourceLocation).build<Any>()
    }

    @PutMapping("/{id}")
    override fun update(@PathVariable id: String, @RequestBody document: DocumentRequest): ResponseEntity<*>? {
        documentFacade.update(id, document)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping
    override fun getAll(): ResponseEntity<*> {
        val documents = documentFacade.findAllDocuments()
        return ResponseEntity.ok(documents)
    }

    @GetMapping("/{id}")
    override fun get(@PathVariable id: String): ResponseEntity<*> {
        val document = documentFacade.findDocument(id)
        return if (document != null) {
            ResponseEntity.ok(document)
        } else {
            LOG.debug("No Document with id [$id]")
            ResponseEntity.notFound().build<Any>()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentController::class.java)
    }
}

data class DocumentRequest(var name: String, var description: String)

data class DocumentDto(
    var id: Long,
    var name: String? = null,
    var description: String? = null,
    var createdBy: String,
    var createdDate: String,
    var modifiedBy: String,
    var modifiedDate: String
)
