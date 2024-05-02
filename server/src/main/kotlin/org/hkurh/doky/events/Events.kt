package org.hkurh.doky.events

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.users.db.UserEntity
import org.springframework.context.ApplicationEvent

class UserRegistrationEvent(source: Any, var user: UserEntity) : ApplicationEvent(source)

class DocumentUpdatedEvent(source: Any, var document: DocumentEntity) : ApplicationEvent(source)
