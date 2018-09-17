package com.outware.omproject.cerberus.tasks

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.util.fetchNoteworthyChangesFromCommitHistory
import com.outware.omproject.cerberus.util.getBuildTickets
import com.outware.omproject.cerberus.util.getJiraUrlFromTicket

open class MakeReleaseNotesTask : NonEssentialTask() {

    override fun run() {
        val tickets = getBuildTickets()

        val changes = tickets.map {
            with(it) {
                "[$key](${getJiraUrlFromTicket(key)}) - ${fields.summary}"
            }
        }.toMutableList()

        changes.addAll(fetchNoteworthyChangesFromCommitHistory())

        val releaseNotes = makeReleaseNotes(changes, CerberusPlugin.properties?.buildUrl)

        CerberusPlugin.properties?.releaseNotes = releaseNotes
    }

    private fun makeReleaseNotes(changes: List<String>, buildUrl: String?): String =
            StringBuilder().apply {
                append("### Changelog\n")

                if (changes.isEmpty()) {
                    append("*No changes found.*\n")
                } else {
                    changes.forEach {
                        append("- $it\n")
                    }
                }

                buildUrl?.let {
                    append("\nBuilt by [Jenkins]($it)")
                }
            }.toString()
}
