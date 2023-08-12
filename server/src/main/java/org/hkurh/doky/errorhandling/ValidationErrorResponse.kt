package org.hkurh.doky.errorhandling

data class ValidationErrorResponse(var error: Error, var fields: MutableList<Field> = ArrayList())

data class Field(var field: String, var message: String)
