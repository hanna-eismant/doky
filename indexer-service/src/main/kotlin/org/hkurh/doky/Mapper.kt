package org.hkurh.doky

import org.hkurh.doky.documents.db.DocumentEntity
import org.hkurh.doky.search.DocumentIndexData
import java.time.ZoneOffset

fun DocumentEntity.toIndexData(): DocumentIndexData {
    val entity = this
    return DocumentIndexData(
        id = entity.id.toString(),
        name = entity.name,
        description = entity.description,
        fileName = entity.fileName,
        createdDate = entity.createdDate?.toInstant()?.atOffset(ZoneOffset.UTC),
        modifiedDate = entity.modifiedDate?.toInstant()?.atOffset(ZoneOffset.UTC),
        createdBy = entity.createdBy?.id.toString(),
        modifiedBy = entity.modifiedBy?.id.toString()
    )
}
