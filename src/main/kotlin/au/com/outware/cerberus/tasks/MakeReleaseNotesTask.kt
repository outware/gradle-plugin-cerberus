package au.com.outware.cerberus.tasks

import au.com.outware.cerberus.CerberusPlugin
import au.com.outware.cerberus.util.getJiraTickets
import au.com.outware.cerberus.util.getJiraUrlFromTicket
import au.com.outware.cerberus.util.getPassthroughChangesFromCommitHistory
import au.com.outware.cerberus.util.makeReleaseNotes

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
