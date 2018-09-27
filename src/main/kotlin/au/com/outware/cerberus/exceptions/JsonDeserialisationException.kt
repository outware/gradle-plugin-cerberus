package au.com.outware.cerberus.exceptions

class JsonDeserialisationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
