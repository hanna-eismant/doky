package org.hkurh.doky

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.users.api.UserDto
import org.hkurh.doky.users.db.UserEntity
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

fun UserEntity.toDto(): UserDto {
    val entity = this
    return UserDto(
        id = entity.id,
        uid = entity.uid,
        name = entity.name
    )
}

fun DocumentEntity.toDto(): DocumentResponse {
    val entity = this
    return DocumentResponse().apply {
        id = entity.id
        name = entity.name
        description = entity.description
        fileName = entity.fileName
        createdBy = userToString(entity.createdBy)
        createdDate = dateToString(entity.createdDate)
        modifiedBy = userToString(entity.modifiedBy)
        modifiedDate = dateToString(entity.modifiedDate)
    }
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
