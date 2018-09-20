package com.outware.omproject.cerberus.util

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.data.GitLogProvider

fun getTicketsFromCommitHistory(logProvider: GitLogProvider = GitLogProvider()): List<String> =
        getCommitsPostExclusion(logProvider)
                .fold(mutableListOf(), ::ticketExtractionFolder)
                .distinct()

fun getPassthroughChangesFromCommitHistory(logProvider: GitLogProvider = GitLogProvider()): List<String> =
        getCommitsPostExclusion(logProvider).filter(::commitInclusionFilter)

private fun getCommitsPostExclusion(logProvider: GitLogProvider): List<String> =
        logProvider.getLogLines().filter(::commitExclusionFilter)

private fun commitExclusionFilter(input: String): Boolean {
    CerberusPlugin.properties?.commitIgnorePattern?.let {
        return input.matches(it.toRegex()).not()
    }
    return true
}

private fun commitInclusionFilter(input: String): Boolean {
    CerberusPlugin.properties?.commitPassthroughPattern?.let {
        return input.matches(it.toRegex())
    }
    return false
}

private fun ticketExtractionFolder(accumulator: MutableList<String>, input: String): MutableList<String> {
    CerberusPlugin.properties?.ticketExtractionPattern.takeIf { it?.isNotEmpty() ?: false }?.let {
        val ticketMatcher = it.toPattern().matcher(input)

        while (ticketMatcher.find()) {
            accumulator.add(ticketMatcher.group())
        }
    }
    return accumulator
}