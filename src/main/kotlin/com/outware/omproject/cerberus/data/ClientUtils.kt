package com.outware.omproject.cerberus.data

import com.outware.omproject.cerberus.CerberusPlugin

fun makeJiraEndpoint(endpoint: String): String {
    val domain = CerberusPlugin.properties?.jiraDomain ?: throw IllegalArgumentException()

    val trimmedDomain = if (domain.endsWith('/')) {
        domain.trimEnd('/')
    } else {
        domain
    }

    val formattedEndpoint = if (endpoint.startsWith('/')) {
        endpoint
    } else {
        "/$endpoint"
    }

    return trimmedDomain + formattedEndpoint
}