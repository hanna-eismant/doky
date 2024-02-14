package org.hkurh.doky.documents

import org.hkurh.doky.users.UserDto

class DocumentDto {
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var creator: UserDto? = null
    var createdBy: String? = null
    var modifiedBy: String? = null
}
