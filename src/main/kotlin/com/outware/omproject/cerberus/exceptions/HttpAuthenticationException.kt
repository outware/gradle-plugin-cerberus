package com.outware.omproject.cerberus.exceptions

class HttpAuthenticationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
