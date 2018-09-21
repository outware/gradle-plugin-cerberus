package au.com.outware.cerberus.exceptions

class JsonSerialisationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
