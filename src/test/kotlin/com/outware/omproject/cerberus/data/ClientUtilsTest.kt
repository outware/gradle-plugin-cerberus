package com.outware.omproject.cerberus.data

import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.CerberusPluginExtension
import org.junit.Before
import org.junit.Test

class ClientUtilsTest {

    val mockDomain = "https://jira.mock.url"
    val mockEndpoint = "endpoint"

    val mockExpectedJiraEndpoint = "https://jira.mock.url/endpoint"

    @Before
    fun beforeEach() {
        CerberusPlugin.properties = CerberusPluginExtension()
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullDomain() {
        CerberusPlugin.properties!!.jiraDomain = null

        makeJiraEndpoint(mockEndpoint)
    }

    @Test
    fun domainTrailingSlash() {
        CerberusPlugin.properties!!.jiraDomain = "$mockDomain/"

        val result = makeJiraEndpoint(mockEndpoint)

        assert(result == mockExpectedJiraEndpoint)
    }

    @Test
    fun domainNoTrailingSlash() {
        CerberusPlugin.properties!!.jiraDomain = mockDomain

        val result = makeJiraEndpoint(mockEndpoint)

        assert(result == mockExpectedJiraEndpoint)
    }

    @Test
    fun endpointLeadingSlash() {
        CerberusPlugin.properties!!.jiraDomain = mockDomain

        val result = makeJiraEndpoint("/$mockEndpoint")

        assert(result == mockExpectedJiraEndpoint)
    }
}