package org.hkurh.doky.search

import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component

@Component
class DocumentSearchFacade(
    private val userService: UserService ,
    private val documentSearchService: DocumentSearchService
) {

    fun search(query: String): List<DocumentBean> {
        val user = userService.getCurrentUser()
        return documentSearchService.search()
    }


}
