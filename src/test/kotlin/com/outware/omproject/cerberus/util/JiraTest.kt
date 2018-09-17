package com.outware.omproject.cerberus.util

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.CerberusPluginExtension
import org.junit.Test

class JiraTest {

    val mockJiraDomain = "https://jira.mock.url"

    init {
        CerberusPlugin.properties = CerberusPluginExtension()
        CerberusPlugin.properties!!.jiraDomain = mockJiraDomain
    }

    @Test
    fun basicJiraUrlBuilder() {
        val result = getJiraUrlFromTicket("ABC-123")
        val expectedResult = "https://jira.mock.url/browse/ABC-123"

        assert(result == expectedResult)
    }
}