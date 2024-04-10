package org.hkurh.doky.errorhandling

data class ErrorResponse(var error: Error)
data class Error(var message: String)

data class ValidationErrorResponse(var error: Error, var fields: MutableList<Field> = ArrayList())
data class Field(var field: String, var messages: List<String> = ArrayList())
