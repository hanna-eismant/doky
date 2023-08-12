package org.hkurh.doky.documents

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
    override fun uploadFile(@RequestBody file: MultipartFile?, @PathVariable id: String?): ResponseEntity<*> {
        documentFacade.saveFile(file, id)
        return ResponseEntity.ok<Any>(null)
    }

    @GetMapping("/{id}/download")
    @Throws(IOException::class)
    override fun downloadFile(@PathVariable id: String?): ResponseEntity<*> {
        return try {
            val file = documentFacade.getFile(id)
            if (file == null) {
                LOG.debug(String.format("No attached file for document [%s]", id))
                return ResponseEntity.noContent().build<Any>()
            }
            val header = "attachment; filename=\"" +
                    file.filename +
                    "\""
            ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, header)
                    .body(file)
        } catch (e: MalformedURLException) {
            ResponseEntity.internalServerError().build<Any>()
        }
    }

    @PostMapping
    override fun create(@RequestBody document: DocumentRequest?): ResponseEntity<*> {
        val createdDocument = documentFacade.createDocument(document!!.name, document.description)
        val resourceLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").build(createdDocument!!.id)
        return ResponseEntity.created(resourceLocation).build<Any>()
    }

    @get:GetMapping
    override val all: ResponseEntity<*>
        get() {
            val documents = documentFacade.findAllDocuments()
            return if (documents!!.isEmpty()) {
                LOG.debug("No Documents for current user")
                ResponseEntity.noContent().build<Any>()
            } else {
                ResponseEntity.ok(documents)
            }
        }

    @GetMapping("/{id}")
    override fun get(@PathVariable id: String?): ResponseEntity<*> {
        val document = documentFacade.findDocument(id)
        return if (document != null) {
            ResponseEntity.ok(document)
        } else {
            LOG.debug(String.format("No Document with id [%s]", id))
            ResponseEntity.noContent().build<Any>()
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentController::class.java)
    }
}
