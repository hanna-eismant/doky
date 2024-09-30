package org.hkurh.doky.users

import org.hkurh.doky.DokyUnitTest
import org.hkurh.doky.kafka.EmailType
import org.hkurh.doky.kafka.KafkaEmailNotificationProducerService
import org.hkurh.doky.security.UserAuthority
import org.hkurh.doky.users.db.AuthorityEntity
import org.hkurh.doky.users.db.AuthorityEntityRepository
import org.hkurh.doky.users.db.UserEntity
import org.hkurh.doky.users.db.UserEntityRepository
import org.hkurh.doky.users.impl.DefaultUserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
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
import java.util.*
import java.util.function.Function

@DisplayName("DefaultUserService unit test")
class DefaultUserServiceTest : DokyUnitTest {
    private val userUid = "user@mail.com"
    private val userName = "user"
    private val userPassword = "password"

    private var userEntityRepository: MockUserEntityRepository = mock()
    private var authorityEntityRepository: AuthorityEntityRepository = mock()
    private val kafkaEmailNotificationProducerService: KafkaEmailNotificationProducerService = mock()
    private var userService =
        DefaultUserService(userEntityRepository, authorityEntityRepository, kafkaEmailNotificationProducerService)

    @Test
    @DisplayName("Should publish user registration event when user is successfully registered")
    fun shouldPublishEvent_whenUserSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository.save(any())).thenReturn(userEntity)

        // when
        assertDoesNotThrow { userService.create(userUid, userPassword) }

        // then
        verify(kafkaEmailNotificationProducerService).sendNotification(userEntity.id, EmailType.REGISTRATION)
    }

    @Test
    @DisplayName("Should not publish user registration event when user is not successfully registered")
    fun shouldNotPublishEvent_whenUserNotSuccessfullyRegistered() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository.save(any())).thenThrow(RuntimeException())

        // when
        assertThrows<RuntimeException> { userService.create(userUid, userPassword) }

        // then
        verify(kafkaEmailNotificationProducerService, never()).sendNotification(userEntity.id, EmailType.REGISTRATION)
    }

    @Test
    @DisplayName("Should set name for user from UID when register")
    fun shouldSetUserNameFromUid_whenRegister() {
        // given
        val userEntity = createUserEntity()
        whenever(userEntityRepository.save(any())).thenReturn(userEntity)

        // when
        userService.create(userUid, userPassword)

        // then
        verify(userEntityRepository).save(argThat<UserEntity> { name == userName })
    }

    @Test
    @DisplayName("Should assign USER authority when register")
    fun shouldAssignUserAuthority_whenRegister() {
        // given
        val userEntity = createUserEntity()
        val authority = createAuthorityEntity()
        whenever(userEntityRepository.save(any())).thenReturn(userEntity)
        whenever(authorityEntityRepository.findByAuthority(UserAuthority.ROLE_USER)).thenReturn(authority)

        // when
        userService.create(userUid, userPassword)

        // then
        verify(userEntityRepository).save(argThat<UserEntity> {
            authorities.find { a -> a.authority == UserAuthority.ROLE_USER } != null
        })
    }

    private fun createUserEntity(): UserEntity {
        return UserEntity().apply {
            id = 14
            uid = userUid
            name = userName
            password = userPassword
        }
    }

    private fun createAuthorityEntity(): AuthorityEntity {
        return AuthorityEntity().apply {
            authority = UserAuthority.ROLE_USER
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
