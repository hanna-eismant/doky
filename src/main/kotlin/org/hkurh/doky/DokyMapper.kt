/*
 * This file is part of the Doky Project.
 *
 * Copyright (C) 2022-2026
 *  - Hanna Kurhuzenkava (hanna.kurhuzenkava@outlook.com)
 *  - Anton Kurhuzenkau (kurguzenkov@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * Contact Information:
 *  - Project Homepage: https://github.com/hanna-eismant/doky
 */

package org.hkurh.doky

import org.apache.commons.lang3.StringUtils
import org.hkurh.doky.documents.api.DocumentResponse
import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.search.DocumentIndexData
import org.hkurh.doky.users.api.UserDto
import org.hkurh.doky.users.db.UserEntity
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*

fun UserEntity.toDto(): UserDto {
    val entity = this
    return UserDto().apply {
        id = entity.id
        email = entity.uid
        name = entity.name
        roles = entity.authorities.map { it.authority.name }.toMutableSet()
    }
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

fun DocumentEntity.toIndexData(): DocumentIndexData {
    val entity = this
    return DocumentIndexData(
        objectID = entity.id.toString(),
        name = entity.name,
        description = entity.description,
        fileName = entity.fileName,
        createdDateTs = entity.createdDate?.toInstant()?.toEpochMilli(),
        createdDateIso = entity.createdDate?.toInstant()?.toString(),
        modifiedDateTs = entity.modifiedDate?.toInstant()?.toEpochMilli(),
        modifiedDateIso = entity.modifiedDate?.toInstant()?.toString(),
        createdBy = entity.createdBy?.id.toString(),
        modifiedBy = entity.modifiedBy?.id.toString(),
        allowedUsers = mutableListOf()
    )
}

val formatter: DateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
private fun dateToString(source: Date?): String {
    return if (source != null) {
        formatter.print(source.time)
    } else {
        StringUtils.EMPTY
    }
}

private fun userToString(source: UserEntity?): String {
    return if (source != null) {
        "${source.name} <${source.uid}>"
    } else {
        StringUtils.EMPTY
    }
}
