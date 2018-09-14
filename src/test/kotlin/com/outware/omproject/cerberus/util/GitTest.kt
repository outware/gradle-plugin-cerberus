package com.outware.omproject.cerberus.util

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.CerberusPluginExtension
import org.junit.Before
import org.junit.Test

class GitTest {

    lateinit var gitLogProvider: GitLogProvider

    val mockThreeTicketLogLines = listOf(
            "[CER-8] Fake Commit One",
            "[CER-9] Fake Commit Two",
            "[CER-10] Fake Commit Three"
    )

    val mockExpectedThreeTickets = listOf("CER-8", "CER-9", "CER-10")
    val mockExpectedEmptyResult = emptyList<String>()

    @Before
    fun beforeEach() {
        CerberusPlugin.properties = CerberusPluginExtension()
        gitLogProvider = mock()
    }

    private fun logLineTestTemplate(mockLogLines: List<String>, expectedResult: List<String>) {
        whenever(gitLogProvider.getLogLines()).thenReturn(mockLogLines)

        val result = extractJiraTicketsFromCommitHistory(gitLogProvider)

        assert(result == expectedResult)
    }

    @Test
    fun emptyGitLog() {
        val emptyLogLines = emptyList<String>()

        logLineTestTemplate(emptyLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun nullTicketRegex() {
        CerberusPlugin.properties!!.ticketRegex = null

        logLineTestTemplate(mockThreeTicketLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun invalidTicketRegex() {
        CerberusPlugin.properties!!.ticketRegex = ""

        logLineTestTemplate(mockThreeTicketLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun standardThreeTicketExtraction() {
        logLineTestTemplate(mockThreeTicketLogLines, mockExpectedThreeTickets)
    }

    @Test
    fun compoundThreeTicketExtraction() {
        val mockLogLines = listOf(
                "[CER-8][CER-9] Fake Commit One",
                "[CER-10] Fake Commit Two"
        )
        val expectedResult = listOf("CER-8", "CER-9", "CER-10")

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun zeroTicketExtraction() {
        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two"
        )

        logLineTestTemplate(mockLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun someTicketExtraction() {
        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two",
                "[CER-8] Commit Three"
        )
        val expectedResult = listOf("CER-8")

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun commitExclusionFilter() {
        CerberusPlugin.properties!!.commitExclusionRegex = "^#.*"

        val mockLogLines = listOf(
                "#[CER-8] Fake Commit One",
                "#[CER-9] Fake Commit Two",
                "[CER-10] Fake Commit Three",
                "[CER-11] Fake Commit Four"
        )
        val expectedResult = listOf("CER-10", "CER-11")

        logLineTestTemplate(mockLogLines, expectedResult)
    }
}
