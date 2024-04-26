package org.hkurh.doky.users

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.email.EmailService
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.hkurh.doky.users.impl.DefaultUserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.repository.query.FluentQuery
import org.springframework.mail.MailSendException
import java.util.*
import java.util.function.Function

@DisplayName("DefaultUserService unit test")
class DefaultUserServiceTest : DokyUnitTest {
    private val userUid = "user@mail.com"
    private val userName = "user"
    private val userPassword = "password"

    private var userEntityRepository: MockUserEntityRepository = mock()
    private val emailService: EmailService = mock()
    private var userService = DefaultUserService(userEntityRepository, emailService)

    @Test
    @DisplayName("Should send registration email when user is successfully registered")
    fun shouldSendEmail_whenUserSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        Mockito.`when`(userEntityRepository.save(any())).thenReturn(userEntity)

        // when
        assertDoesNotThrow { userService.create(userUid, userPassword) }

        // then
        verify(emailService).sendRegistrationConfirmationEmail(userEntity)
    }

    @Test
    @DisplayName("Should not send registration email when user is not successfully registered")
    fun shouldNotSendEmail_whenUserNotSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        Mockito.`when`(userEntityRepository.save(any())).thenThrow(RuntimeException())

        // when
        assertThrows<RuntimeException> { userService.create(userUid, userPassword) }

        // then
        verify(emailService, never()).sendRegistrationConfirmationEmail(userEntity)
    }

    @Test
    @DisplayName("Should not throw exception when sending email is not successfully")
    fun shouldNotThrowException_whenEmailSendNotSuccessfully() {
        // given
        val userEntity = createUserEntity()
        Mockito.`when`(userEntityRepository.save(any())).thenReturn(userEntity)
        whenever(emailService.sendRegistrationConfirmationEmail(userEntity)).thenThrow(MailSendException(""))

        // when - then
        assertDoesNotThrow { userService.create(userUid, userPassword) }
    }

    @Test
    @DisplayName("Should set name for user from UID when register")
    fun shouldSetUserNameFromUid_whenRegister() {
        // given
        val userEntity = createUserEntity()
        Mockito.`when`(userEntityRepository.save(any())).thenReturn(userEntity)

        // when
        userService.create(userUid, userPassword)

        // then
        verify(userEntityRepository).save(argThat<UserEntity> { name == userName })
    }

    private fun createUserEntity(): UserEntity {
        return UserEntity().apply {
            id = 1
            uid = userUid
            name = userName
            password = userPassword
        }
    }

    /**
     * The `MockUserEntityRepository` class implements the `UserEntityRepository` interface and provides mock implementations for its methods.
     */
    class MockUserEntityRepository : UserEntityRepository {
        override fun findByUid(uid: String): UserEntity? {
            TODO("Not yet implemented")
        }

        override fun existsByUid(uid: String): Boolean {
            TODO("Not yet implemented")
        }

        override fun <S : UserEntity?> save(entity: S & Any): S & Any {
            TODO("Not yet implemented")
        }

        override fun <S : UserEntity?> saveAll(entities: MutableIterable<S>): MutableIterable<S> {
            TODO("Not yet implemented")
        }

        override fun findAll(): MutableIterable<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<UserEntity?>): MutableList<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<UserEntity?>, pageable: Pageable): Page<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAll(spec: Specification<UserEntity?>, sort: Sort): MutableList<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun findAllById(ids: MutableIterable<Long?>): MutableIterable<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun count(): Long {
            TODO("Not yet implemented")
        }

        override fun count(spec: Specification<UserEntity?>): Long {
            TODO("Not yet implemented")
        }

        override fun delete(entity: UserEntity) {
            TODO("Not yet implemented")
        }

        override fun delete(spec: Specification<UserEntity?>): Long {
            TODO("Not yet implemented")
        }

        override fun deleteAllById(ids: MutableIterable<Long?>) {
            TODO("Not yet implemented")
        }

        override fun deleteAll(entities: MutableIterable<UserEntity?>) {
            TODO("Not yet implemented")
        }

        override fun deleteAll() {
            TODO("Not yet implemented")
        }

        override fun findOne(spec: Specification<UserEntity?>): Optional<UserEntity?> {
            TODO("Not yet implemented")
        }

        override fun exists(spec: Specification<UserEntity?>): Boolean {
            TODO("Not yet implemented")
        }

        override fun <S : UserEntity?, R : Any?> findBy(
            spec: Specification<UserEntity?>,
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

        override fun findById(id: Long): Optional<UserEntity?> {
            TODO("Not yet implemented")
        }

    }
}
