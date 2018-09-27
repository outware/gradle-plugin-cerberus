package au.com.outware.cerberus.exceptions

class GitInvocationException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
