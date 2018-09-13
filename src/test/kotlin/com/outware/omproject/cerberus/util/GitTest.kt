package com.outware.omproject.cerberus.util

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.CerberusPluginExtension
import org.junit.Before
import org.junit.Test

class GitTest {

    lateinit var gitLogProvider: GitLogProvider

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
        val mockLogLines = emptyList<String>()
        val expectedResult = emptyList<String>()

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun standardThreeTicketExtraction() {
        val mockLogLines = listOf(
                "[CER-7] Fake Commit One",
                "[CER-8] Fake Commit Two",
                "[CER-9] Fake Commit Three"
        )
        val expectedResult = listOf("CER-7", "CER-8", "CER-9")

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun compoundThreeTicketExtraction() {
        val mockLogLines = listOf(
                "[CER-7][CER-9] Fake Commit One",
                "[CER-8] Fake Commit Two"
        )
        val expectedResult = listOf("CER-7", "CER-9", "CER-8")

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun zeroTicketExtraction() {
        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two"
        )
        val expectedResult = emptyList<String>()

        logLineTestTemplate(mockLogLines, expectedResult)
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
    fun tryTicketExtractionWithNullPattern() {
        CerberusPlugin.properties!!.ticketRegex = null

        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two",
                "[CER-8] Commit Three"
        )
        val expectedResult = emptyList<String>()

        logLineTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun commitExclusionFilter() {
        CerberusPlugin.properties!!.commitExclusionRegex = "^#.*"

        val mockLogLines = listOf(
                "#[CER-7] Fake Commit One",
                "#[CER-8] Fake Commit Two",
                "[CER-9] Fake Commit Three",
                "[CER-10] Fake Commit Four"
        )
        val expectedResult = listOf("CER-9", "CER-10")

        logLineTestTemplate(mockLogLines, expectedResult)
    }
}
