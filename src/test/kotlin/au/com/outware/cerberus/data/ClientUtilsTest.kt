package au.com.outware.cerberus.data

import au.com.outware.cerberus.CerberusPlugin
import au.com.outware.cerberus.CerberusPluginExtension
import org.junit.Before
import org.junit.Test

class ClientUtilsTest {

    val mockDomain = "https://jira.mock.url"
    val mockEndpoint = "endpoint"

    val mockExpectedJiraAddress = "https://jira.mock.url/endpoint"

    @Before
    fun beforeEach() {
        CerberusPlugin.properties = CerberusPluginExtension()
    }

    @Test(expected = IllegalArgumentException::class)
    fun nullDomain() {
        CerberusPlugin.properties!!.jiraDomain = null

        makeJiraRequestAddress(mockEndpoint)
    }

    @Test
    fun domainTrailingSlash() {
        CerberusPlugin.properties!!.jiraDomain = "$mockDomain/"

        val result = makeJiraRequestAddress(mockEndpoint)

        assert(result == mockExpectedJiraAddress)
    }

    @Test
    fun domainNoTrailingSlash() {
        CerberusPlugin.properties!!.jiraDomain = mockDomain

        val result = makeJiraRequestAddress(mockEndpoint)

        assert(result == mockExpectedJiraAddress)
    }

    @Test
    fun endpointLeadingSlash() {
        CerberusPlugin.properties!!.jiraDomain = mockDomain

        val result = makeJiraRequestAddress("/$mockEndpoint")

        assert(result == mockExpectedJiraAddress)
    }
}