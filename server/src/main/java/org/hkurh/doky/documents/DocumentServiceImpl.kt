package org.hkurh.doky.documents

import org.hkurh.doky.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.lang.NonNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class DocumentServiceImpl(private val documentEntityRepository: DocumentEntityRepository, private val userService: UserService) : DocumentService {
    override fun create(@NonNull name: String?, description: String?): DocumentEntity? {
        val document = DocumentEntity()
        document.name = name
        document.description = description
        val currentUser = userService.currentUser
        document.creator = currentUser
        LOG.debug(String.format("Created new Document [%s] by User [%s]", document.id, currentUser!!.id))
        return documentEntityRepository.save(document)
    }

    override fun find(@NonNull id: String?): Optional<DocumentEntity?>? {
        val documentId = id!!.toLong()
        val currentUser = userService.currentUser
        return documentEntityRepository.findByIdAndCreatorId(documentId, currentUser!!.id)
    }

    @NonNull
    override fun find(): List<DocumentEntity?>? {
        val currentUser = userService.currentUser
        return documentEntityRepository.findByCreatorId(currentUser!!.id)?.let { ArrayList(it) }
    }

    override fun save(@NonNull document: DocumentEntity?) {
        documentEntityRepository.save(document!!)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(DocumentServiceImpl::class.java)
    }
}
