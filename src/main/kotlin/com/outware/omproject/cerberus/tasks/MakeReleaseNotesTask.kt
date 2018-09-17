package com.outware.omproject.cerberus.tasks

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.util.fetchNoteworthyChangesFromCommitHistory
import com.outware.omproject.cerberus.util.getBuildTickets
import com.outware.omproject.cerberus.util.getJiraUrlFromTicket
import com.outware.omproject.cerberus.util.makeReleaseNotes

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
}
