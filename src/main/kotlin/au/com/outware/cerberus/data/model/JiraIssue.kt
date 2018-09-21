package au.com.outware.cerberus.data.model

data class JiraIssue(val id: String,
                     val key: String,
                     val fields: Fields) {
    data class Fields(val summary: String)
}