package com.outware.omproject.cerberus.exceptions

class JsonDeserialisationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
