package com.outware.omproject.cerberus.exceptions

class JsonSerialisationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
