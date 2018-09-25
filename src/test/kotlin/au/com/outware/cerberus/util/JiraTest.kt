package au.com.outware.cerberus.util

import au.com.outware.cerberus.CerberusPlugin
import au.com.outware.cerberus.CerberusPluginExtension
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