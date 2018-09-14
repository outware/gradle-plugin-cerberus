package com.outware.omproject.cerberus.data

import com.outware.omproject.cerberus.CerberusPlugin

class GitLogProvider {
    fun getLogLines(): List<String> {
        val lastSuccessfulCommit = CerberusPlugin.properties?.lastSuccessfulCommit ?: "HEAD"
        val prettyFormat = CerberusPlugin.properties?.gitLogPrettyFormat ?: "%s"

        return ProcessBuilder("git", "log", "--no-merges", "--pretty=format:$prettyFormat", "$lastSuccessfulCommit...HEAD")
                .start().inputStream.bufferedReader().readLines()
    }
}