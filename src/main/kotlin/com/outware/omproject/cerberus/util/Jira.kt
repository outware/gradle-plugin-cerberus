package com.outware.omproject.cerberus.util

import com.google.gson.Gson
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.data.JiraClient
import com.outware.omproject.cerberus.data.model.JiraIssue
import com.outware.omproject.cerberus.data.model.JiraIssueQueryResponse
import com.outware.omproject.cerberus.data.model.JiraSearchRequest
import com.outware.omproject.cerberus.exceptions.HttpAuthenticationException
import com.outware.omproject.cerberus.exceptions.GenericHttpException

fun getBuildTickets(): List<JiraIssue> {
    val ticketsNumbers = extractJiraTicketsFromCommitHistory()
    return fetchJiraTickets(ticketsNumbers)
}

fun fetchJiraTickets(tickets: List<String>): List<JiraIssue> {
    if (tickets.isEmpty()) return emptyList()

    val ticketCsv = tickets.joinToString(",")

    val client = JiraClient("/rest/api/2/search")

    val responseCode = client.post(JiraSearchRequest("key IN ($ticketCsv)", "false"))

    return when (responseCode) {
        200 -> Gson().fromJson(client.responseReader, JiraIssueQueryResponse::class.java).issues
        in (400..499) -> {
            throw HttpAuthenticationException("Authentication failed. HTTP response code: ${client.responseCode}")
        }
        else -> {
            throw GenericHttpException("Querying for ($ticketCsv) failed. HTTP response code: ${client.responseCode}")
        }
    }
}