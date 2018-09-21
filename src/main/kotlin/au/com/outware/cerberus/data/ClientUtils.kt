package au.com.outware.cerberus.data

import au.com.outware.cerberus.CerberusPlugin

fun makeJiraRequestAddress(endpoint: String): String {
    val domain = CerberusPlugin.properties?.jiraDomain ?: throw IllegalArgumentException()

    val trimmedDomain = domain.trimEnd('/')

    val formattedEndpoint = if (endpoint.startsWith('/')) {
        endpoint
    } else {
        "/$endpoint"
    }

    return trimmedDomain + formattedEndpoint
}