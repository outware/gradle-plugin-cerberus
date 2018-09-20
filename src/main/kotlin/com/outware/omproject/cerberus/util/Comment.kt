package com.outware.omproject.cerberus.util

fun makeComment(buildNumber: String? = null, buildUrl: String? = null,
                versionName: String? = null, hockeyUrl: String? = null): String {

    return arrayOf(makeJenkinsInfo(buildNumber, buildUrl),
            makeHockeyInfo(versionName, hockeyUrl))
            .filterNotNull()
            .joinToString("\n")
}

private fun makeJenkinsInfo(buildNumber: String?, url: String?): String? {
    var result: String? = null
    buildNumber?.let {
        result = if (url.isNullOrBlank()) {
            "Jenkins: Build #$it"
        } else {
            "Jenkins: [Build #$it|$url]"
        }
    }
    return result
}

private fun makeHockeyInfo(versionName: String?, url: String?): String? {
    var result: String? = null
    versionName?.let {
        result = if (url.isNullOrBlank()) {
            "HockeyApp: Version $it"
        } else {
            "HockeyApp: [Version $it|$url]"
        }
    }
    return result
}