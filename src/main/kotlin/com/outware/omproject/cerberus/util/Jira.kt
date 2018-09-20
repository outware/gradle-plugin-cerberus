package com.outware.omproject.cerberus.util

import com.google.gson.Gson
import com.outware.omproject.cerberus.data.JiraClient
import com.outware.omproject.cerberus.data.makeJiraRequestAddress
import com.outware.omproject.cerberus.data.model.JiraIssue
import com.outware.omproject.cerberus.data.model.JiraIssueQueryResponse
import com.outware.omproject.cerberus.data.model.JiraSearchRequest
import com.outware.omproject.cerberus.exceptions.GenericHttpException
import com.outware.omproject.cerberus.exceptions.HttpAuthenticationException
import com.outware.omproject.cerberus.exceptions.JsonDeserialisationException

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