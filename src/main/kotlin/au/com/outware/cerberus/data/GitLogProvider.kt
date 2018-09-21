package au.com.outware.cerberus.data

import au.com.outware.cerberus.CerberusPlugin
import au.com.outware.cerberus.exceptions.GitInvocationException
import au.com.outware.cerberus.exceptions.ProcessOutputException

private const val GIT_HEAD_LABEL = "HEAD"

class GitLogProvider {
    fun getLogLines(): List<String> {
        val lastSuccessfulCommit = CerberusPlugin.properties?.lastSuccessfulCommit ?: GIT_HEAD_LABEL
        val prettyFormat = CerberusPlugin.properties?.gitLogPrettyFormat ?: throw IllegalArgumentException()

        lateinit var process: Process
        try {
            process = ProcessBuilder("git", "log", "--no-merges", "--pretty=format:$prettyFormat", "$lastSuccessfulCommit...HEAD").start()
        } catch (t: Throwable) {
            throw GitInvocationException("Failed to invoke Git on the system")
        }

        try {
            return process.inputStream.bufferedReader().readLines()
        } catch (t: Throwable) {
            throw ProcessOutputException("Failed to receive standard output from Git invocation")
        }
    }
}