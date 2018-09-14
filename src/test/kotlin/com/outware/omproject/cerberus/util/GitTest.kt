package com.outware.omproject.cerberus.util

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.CerberusPluginExtension
import com.outware.omproject.cerberus.data.GitLogProvider
import org.junit.Before
import org.junit.Test

class GitTest {

    lateinit var gitLogProvider: GitLogProvider

    val mockPopulatedLogLines = listOf(
            "[TECH] Fake Tech Commit One",
            "[CER-8] Fake Commit One",
            "[CER-9] Fake Commit Two",
            "[TECH] Fake Tech Commit Two",
            "[CER-10] Fake Commit Three"
    )

    val mockExpectedThreeTickets = listOf("CER-8", "CER-9", "CER-10")
    val mockExpectedNoteworthyChanges = listOf("[TECH] Fake Tech Commit One", "[TECH] Fake Tech Commit Two")
    val mockExpectedEmptyResult = emptyList<String>()

    @Before
    fun beforeEach() {
        CerberusPlugin.properties = CerberusPluginExtension()
        gitLogProvider = mock()
    }

    /**
     * Ticket Extraction
     */

    private fun ticketExtractionTestTemplate(mockLogLines: List<String>, expectedResult: List<String>) {
        whenever(gitLogProvider.getLogLines()).thenReturn(mockLogLines)

        val result = extractJiraTicketsFromCommitHistory(gitLogProvider)

        assert(result == expectedResult)
    }

    @Test
    fun emptyGitLog() {
        val emptyLogLines = emptyList<String>()

        ticketExtractionTestTemplate(emptyLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun nullTicketRegex() {
        CerberusPlugin.properties!!.ticketRegex = null

        ticketExtractionTestTemplate(mockPopulatedLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun invalidTicketRegex() {
        CerberusPlugin.properties!!.ticketRegex = ""

        ticketExtractionTestTemplate(mockPopulatedLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun standardThreeTicketExtraction() {
        ticketExtractionTestTemplate(mockPopulatedLogLines, mockExpectedThreeTickets)
    }

    @Test
    fun compoundThreeTicketExtraction() {
        val mockLogLines = listOf(
                "[CER-8][CER-9] Fake Commit One",
                "[CER-10] Fake Commit Two"
        )
        val expectedResult = listOf("CER-8", "CER-9", "CER-10")

        ticketExtractionTestTemplate(mockLogLines, expectedResult)
    }

    @Test
    fun zeroTicketExtraction() {
        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two"
        )

        ticketExtractionTestTemplate(mockLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun someTicketExtraction() {
        val mockLogLines = listOf(
                "Fake Commit One",
                "Fake Commit Two",
                "[CER-8] Commit Three"
        )
        val expectedResult = listOf("CER-8")

        ticketExtractionTestTemplate(mockLogLines, expectedResult)
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

        ticketExtractionTestTemplate(mockLogLines, expectedResult)
    }

    /**
     * Noteworthy Commit Extraction
     */

    private fun changeInclusionTestTemplate(mockLogLines: List<String>, expectedResult: List<String>) {
        whenever(gitLogProvider.getLogLines()).thenReturn(mockLogLines)

        val result = fetchNoteworthyChangesFromCommitHistory(gitLogProvider)

        assert(result == expectedResult)
    }

    @Test
    fun emptyInclusionRegex() {
        CerberusPlugin.properties!!.commitInclusionRegex = ""

        changeInclusionTestTemplate(mockPopulatedLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun nullInclusionRegex() {
        CerberusPlugin.properties!!.commitInclusionRegex = null

        changeInclusionTestTemplate(mockPopulatedLogLines, mockExpectedEmptyResult)
    }

    @Test
    fun commitInclusionFilter() {
        CerberusPlugin.properties!!.commitInclusionRegex = ".*TECH.*"

        changeInclusionTestTemplate(mockPopulatedLogLines, mockExpectedNoteworthyChanges)
    }
}
