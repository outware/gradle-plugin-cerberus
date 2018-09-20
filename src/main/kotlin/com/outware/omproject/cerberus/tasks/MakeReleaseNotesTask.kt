package com.outware.omproject.cerberus.tasks

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.util.getPassthroughChangesFromCommitHistory
import com.outware.omproject.cerberus.util.getJiraTickets
import com.outware.omproject.cerberus.util.getJiraUrlFromTicket
import com.outware.omproject.cerberus.util.makeReleaseNotes

open class MakeReleaseNotesTask : NonEssentialTask() {

    override fun run() {
        val tickets = getJiraTickets()

        val changes = tickets.map {
            with(it) {
                "[$key](${getJiraUrlFromTicket(key)}) - ${fields.summary}"
            }
        }.toMutableList()

        changes.addAll(getPassthroughChangesFromCommitHistory())

        val releaseNotes = makeReleaseNotes(changes, CerberusPlugin.properties?.buildUrl)

        CerberusPlugin.properties?.releaseNotes = releaseNotes
    }
}
