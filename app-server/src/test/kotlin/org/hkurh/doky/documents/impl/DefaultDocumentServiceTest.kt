package org.hkurh.doky.documents.impl

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.documents.db.DocumentEntityRepository
import org.hkurh.doky.users.UserService
import org.hkurh.doky.users.db.UserEntity
import org.junit.jupiter.api.DisplayName
import org.mockito.kotlin.mock
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.query.FluentQuery
import java.util.*
import java.util.function.Function

@DisplayName("DefaultDocumentService unit test")
class DefaultDocumentServiceTest : DokyUnitTest {

    private val documentEntityRepository: MockDocumentEntityRepository = mock()
    private val userService: UserService = mock()

    private val documentService: DefaultDocumentService =
        DefaultDocumentService(documentEntityRepository, userService)

    private fun createUserEntity(): UserEntity {
        return UserEntity().apply {
            id = 1
            name = "user"
        }
    }

    private fun createDocumentEntity(documentName: String): DocumentEntity {
        return DocumentEntity().apply {
            name = documentName
        }
    }

    /**
     * Mock implementation of the [DocumentEntityRepository] interface.
     */
    class MockDocumentEntityRepository : DocumentEntityRepository {
        override fun findByCreatorId(documentId: Long): List<DocumentEntity> {
            TODO("Not yet implemented")
        }

        override fun findByIdAndCreatorId(documentId: Long, creatorId: Long): DocumentEntity? {
            TODO("Not yet implemented")
        }

        override fun findLatestModified(modifiedDate: Date): List<DocumentEntity> {
            TODO("Not yet implemented")
        }

        override fun findByListIdAndUserId(documentIdList: List<Long>, userId: Long): List<DocumentEntity> {
            TODO("Not yet implemented")
        }

        override fun <S : DocumentEntity?> save(entity: S & Any): S & Any {
            TODO("Not yet implemented")
        }

        override fun <S : DocumentEntity?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
            TODO("Not yet implemented")
        }

        override fun findAll(): MutableIterable<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<DocumentEntity?>): MutableList<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<DocumentEntity?>, pageable: Pageable): Page<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<DocumentEntity?>, sort: Sort): MutableList<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAllById(ids: MutableIterable<Long?>): MutableIterable<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun count(): Long {
            TODO("Not yet implemented")
        }

        override fun count(spec: Specification<DocumentEntity?>): Long {
            TODO("Not yet implemented")
        }

        override fun delete(entity: DocumentEntity) {
            TODO("Not yet implemented")
        }

        override fun delete(spec: Specification<DocumentEntity?>): Long {
            TODO("Not yet implemented")
        }

        override fun deleteAllById(ids: MutableIterable<Long?>) {
            TODO("Not yet implemented")
        }

        override fun deleteAll(entities: MutableIterable<DocumentEntity?>) {
            TODO("Not yet implemented")
        }

        override fun deleteAll() {
            TODO("Not yet implemented")
        }

        override fun findOne(spec: Specification<DocumentEntity?>): Optional<DocumentEntity?> {
            TODO("Not yet implemented")
        }

        override fun exists(spec: Specification<DocumentEntity?>): Boolean {
            TODO("Not yet implemented")
        }

        override fun <S : DocumentEntity?, R : Any?> findBy(
            spec: Specification<DocumentEntity?>,
            queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
        ): R & Any {
            TODO("Not yet implemented")
        }

        override fun deleteById(id: Long) {
            TODO("Not yet implemented")
        }

        override fun existsById(id: Long): Boolean {
            TODO("Not yet implemented")
        }

        override fun findById(id: Long): Optional<DocumentEntity?> {
            TODO("Not yet implemented")
        }

    }
}
