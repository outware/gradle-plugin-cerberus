package com.outware.omproject.cerberus.util

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.data.GitLogProvider

fun extractJiraTicketsFromCommitHistory(logProvider: GitLogProvider = GitLogProvider()): List<String> {
    val subjectLines = logProvider.getLogLines()

    val extractedTickets = subjectLines.filter(::commitExclusionFilter)
            .fold(mutableListOf(), ::ticketExtractionFolder)

    return extractedTickets.distinct()
}

fun fetchNoteworthyChangesFromCommitHistory(logProvider: GitLogProvider = GitLogProvider()): List<String> {
    val subjectLines = logProvider.getLogLines()

    return subjectLines.filter(::commitInclusionFilter)
}

private fun commitExclusionFilter(input: String): Boolean {
    CerberusPlugin.properties?.commitExclusionRegex?.let {
        return input.matches(it.toRegex()).not()
    }
    return true
}

private fun commitInclusionFilter(input: String): Boolean {
    CerberusPlugin.properties?.commitInclusionRegex?.let {
        return input.matches(it.toRegex())
    }
    return false
}

private fun ticketExtractionFolder(accumulator: MutableList<String>, input: String): MutableList<String> {
    CerberusPlugin.properties?.ticketRegex.takeIf { it?.isNotEmpty() ?: false }?.let {
        val ticketMatcher = it.toPattern().matcher(input)

        while (ticketMatcher.find()) {
            accumulator.add(ticketMatcher.group())
        }
    }
    return accumulator
}