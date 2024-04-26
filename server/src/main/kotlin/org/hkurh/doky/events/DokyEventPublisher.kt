package org.hkurh.doky.events

import org.hkurh.doky.users.db.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class DokyEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) {

    fun publishUserRegistrationEvent(user: UserEntity) {
        LOG.debug("Publishing user registration event for user [${user.id}]")
        val event = UserRegistrationEvent(this, user)
        applicationEventPublisher.publishEvent(event)
    }


    companion object {
        private val LOG = LoggerFactory.getLogger(DokyEventPublisher::class.java)
    }
}
