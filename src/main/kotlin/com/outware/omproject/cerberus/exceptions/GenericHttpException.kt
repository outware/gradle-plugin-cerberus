package com.outware.omproject.cerberus.exceptions

class GenericHttpException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
