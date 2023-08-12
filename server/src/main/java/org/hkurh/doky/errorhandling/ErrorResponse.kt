package org.hkurh.doky.errorhandling

data class ErrorResponse(var error: Error)

data class Error(var message: String)
