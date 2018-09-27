package au.com.outware.cerberus.tasks

import au.com.outware.cerberus.CerberusPlugin
import au.com.outware.cerberus.data.JiraClient
import au.com.outware.cerberus.data.model.JiraCommentRequest
import au.com.outware.cerberus.exceptions.GenericHttpException
import au.com.outware.cerberus.exceptions.HttpAuthenticationException
import au.com.outware.cerberus.util.getJiraTickets
import au.com.outware.cerberus.util.makeComment

open class UpdateTicketTask : NonEssentialTask() {

    override fun run() {
        val tickets = getJiraTickets()

        val ticketComment = makeComment(CerberusPlugin.properties?.buildNumber,
                CerberusPlugin.properties?.buildUrl,
                CerberusPlugin.properties?.hockeyAppShortVersion,
                CerberusPlugin.properties?.hockeyAppUploadUrl)

        tickets.forEach {
            commentOnJiraTicket(it.key, ticketComment)
        }
    }

    private fun commentOnJiraTicket(ticket: String, message: String) {
        val client = JiraClient("/rest/api/2/issue/$ticket/comment")

        val responseCode = client.post(JiraCommentRequest(message))

        when (responseCode) {
            201 -> println("Successfully commented on $ticket")
            in (400..499) -> {
                throw HttpAuthenticationException("Authentication failed. HTTP response code: $responseCode")
            }
            else -> {
                throw GenericHttpException("Comment on $ticket failed. HTTP response code: $responseCode")
            }
        }
    }
}
