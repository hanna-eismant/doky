package org.hkurh.doky.search

import org.hkurh.doky.users.UserService
import org.springframework.stereotype.Component

@Component
class DocumentSearchFacade(
    private val userService: UserService ,
    private val documentSearchService: DocumentSearchService,
    private val documentIndexService: DocumentIndexService
) {

    fun search(query: String)  {
        documentIndexService.fullIndex()
        documentIndexService.search(query)
//        val user = userService.getCurrentUser()

    }


}
