package au.com.outware.cerberus.exceptions

class JiraClientException(override val message: String?) : Throwable(message) {
    constructor() : this(null)
}
