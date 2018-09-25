package au.com.outware.cerberus.util

import org.junit.Test

class ReleaseNotesTest {

    val mockThreeChanges = listOf(
            "[TECH] Change One",
            "[TECH] Change Two",
            "[TECH] Change Three")

    val mockBuildUrl = "https://build.url"

    @Test
    fun withChangesAndBuildUrl() {
        val expectedResult = """
            ### Changelog
            - [TECH] Change One
            - [TECH] Change Two
            - [TECH] Change Three

            Built by [Jenkins](https://build.url)
        """.trimIndent()

        val result = makeReleaseNotes(mockThreeChanges, mockBuildUrl)

        assert(result == expectedResult)
    }

    @Test
    fun withNoChangesOrBuildUrl() {
        val expectedResult = """
            ### Changelog
            *No changes found.*
        """.trimIndent()

        val result = makeReleaseNotes(emptyList(), null)

        assert(result == expectedResult)
    }

    @Test
    fun withChanges() {
        val expectedResult = """
            ### Changelog
            - [TECH] Change One
            - [TECH] Change Two
            - [TECH] Change Three
        """.trimIndent()

        val result = makeReleaseNotes(mockThreeChanges, null)

        assert(result == expectedResult)
    }

    @Test
    fun withBuildUrl() {
        val expectedResult = """
            ### Changelog
            *No changes found.*

            Built by [Jenkins](https://build.url)
        """.trimIndent()

        val result = makeReleaseNotes(emptyList(), mockBuildUrl)

        assert(result == expectedResult)
    }
}