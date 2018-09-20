package com.outware.omproject.cerberus.exceptions

class ProcessOutputException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
