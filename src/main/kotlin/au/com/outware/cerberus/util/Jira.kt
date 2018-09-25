package au.com.outware.cerberus.util

import au.com.outware.cerberus.data.JiraClient
import au.com.outware.cerberus.data.makeJiraRequestAddress
import au.com.outware.cerberus.data.model.JiraIssue
import au.com.outware.cerberus.data.model.JiraIssueQueryResponse
import au.com.outware.cerberus.data.model.JiraSearchRequest
import au.com.outware.cerberus.exceptions.GenericHttpException
import au.com.outware.cerberus.exceptions.HttpAuthenticationException
import au.com.outware.cerberus.exceptions.JsonDeserialisationException
import com.google.gson.Gson

fun getJiraUrlFromTicket(ticket: String): String {
    return makeJiraRequestAddress("/browse/$ticket")
}

fun getJiraTickets(): List<JiraIssue> {
    val ticketsNumbers = getTicketsFromCommitHistory()
    return getJiraTicketDetails(ticketsNumbers)
}

fun getJiraTicketDetails(tickets: List<String>): List<JiraIssue> {
    if (tickets.isEmpty()) return emptyList()

    val ticketCsv = tickets.joinToString(",")

    val client = JiraClient("/rest/api/2/search")

    val responseCode = client.post(JiraSearchRequest("key IN ($ticketCsv)", "false"))

    return when (responseCode) {
        200 -> {
            try {
                Gson().fromJson(client.responseReader, JiraIssueQueryResponse::class.java).issues
            } catch (t: Throwable) {
                throw JsonDeserialisationException("Failed to deserialise Jira query response with error: ${t.message}")
            }
        }
        in (400..499) -> {
            throw HttpAuthenticationException("Authentication failed. HTTP response code: $responseCode")
        }
        else -> {
            throw GenericHttpException("Querying for ($ticketCsv) failed. HTTP response code: $responseCode")
        }
    }
}