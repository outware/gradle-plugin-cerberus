package com.outware.omproject.cerberus

import com.outware.omproject.cerberus.tasks.MakeReleaseNotesTask
import com.outware.omproject.cerberus.tasks.UpdateTicketTask
import org.gradle.api.Plugin
import org.gradle.api.Project

private const val PROPERTY_EXTENSION_NAME = "cerberus"

private const val RELEASE_NOTES_TASK_NAME = "cerberus_makeReleaseNotes"
private const val UPDATE_TICKET_TASK_NAME = "cerberus_updateTicket"

open class CerberusPlugin : Plugin<Project> {
    companion object {
        var properties: CerberusPluginExtension? = null
    }

    override fun apply(project: Project) {
        properties = project.extensions.create(PROPERTY_EXTENSION_NAME, CerberusPluginExtension::class.java)

        project.tasks.create(RELEASE_NOTES_TASK_NAME, MakeReleaseNotesTask::class.java)
        project.tasks.create(UPDATE_TICKET_TASK_NAME, UpdateTicketTask::class.java)

        project.afterEvaluate(this::afterEvaluate)
    }

    /**
     * The `afterEvaluate` callback on Project is called after the project has settled and,
     * importantly, after the various configuration blocks have been resolved.
     * We use this as we can't access properties such as `shouldUpdateTicketAfter` in the `apply` block
     */
    private fun afterEvaluate(project: Project) {
        properties?.shouldUpdateTicketAfter?.let {
            if (it.isNotEmpty()) {
                with(project.tasks.getByName(UPDATE_TICKET_TASK_NAME) as UpdateTicketTask) {
                    setShouldRunAfter(it)
                }
            }
        }
    }
}