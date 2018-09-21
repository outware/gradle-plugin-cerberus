package au.com.outware.cerberus.exceptions

class ProcessOutputException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
