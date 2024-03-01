package org.hkurh.doky

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.documents.api.DocumentDto
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.users.api.UserDto
import org.hkurh.doky.users.db.UserEntity
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

fun UserEntity.toDto(): UserDto {
    val entity = this
    val dto = UserDto().apply {
        id = entity.id
        uid = entity.uid
        name = entity.name
    }
    return dto
}

fun DocumentEntity.toDto(): DocumentDto {
    val entity = this
    val dto = DocumentDto().apply {
        id = entity.id
        name = entity.name
        description = entity.description
        createdBy = userToString(entity.createdBy)
        createdDate = dateToString(entity.createdDate)
        modifiedBy = userToString(entity.modifiedBy)
        modifiedDate = dateToString(entity.modifiedDate)
    }
    return dto
}

val formatter: DateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
fun dateToString(source: Date?): String {
    return if (source != null) {
        formatter.print(source.time)
    } else {
        StringUtils.EMPTY
    }
}

fun userToString(source: UserEntity?): String {
    return if (source != null) {
        "${source.name} <${source.uid}>"
    } else {
        StringUtils.EMPTY
    }
}
