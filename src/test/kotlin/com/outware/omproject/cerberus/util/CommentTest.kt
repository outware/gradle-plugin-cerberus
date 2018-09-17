package com.outware.omproject.cerberus.util

import org.junit.Test

class CommentTest {

    val mockJenkinsBuildNumber = "451"
    val mockJenkinsBuildUrl = "https://build.url"

    val mockHockeyArtefactVersionName = "2.0-RC"
    val mockHockeyArtefactUrl = "https://hockey.url"

    @Test
    fun withJenkinsAndHockeyLabelsAndUrls() {
        val result = buildComment(mockJenkinsBuildNumber, mockJenkinsBuildUrl, mockHockeyArtefactVersionName, mockHockeyArtefactUrl)

        val expectedResult = """
            Jenkins: [Build #451|https://build.url]
            HockeyApp: [Version 2.0-RC|https://hockey.url]
        """.trimIndent()

        assert(result == expectedResult)
    }

    @Test
    fun withJenkinsAndHockeyLabels() {
        val result = buildComment(buildNumber = mockJenkinsBuildNumber, versionName = mockHockeyArtefactVersionName)

        val expectedResult = """
            Jenkins: Build #451
            HockeyApp: Version 2.0-RC
        """.trimIndent()

        assert(result == expectedResult)
    }

    @Test
    fun withJenkinsLabelAndUrl() {
        val result = buildComment(buildNumber = mockJenkinsBuildNumber, buildUrl = mockJenkinsBuildUrl)

        val expectedResult = """
            Jenkins: [Build #451|https://build.url]
        """.trimIndent()

        assert(result == expectedResult)
    }

    @Test
    fun withJenkinsLabel() {
        val result = buildComment(buildNumber = mockJenkinsBuildNumber)

        val expectedResult = """
            Jenkins: Build #451
        """.trimIndent()

        assert(result == expectedResult)
    }

    @Test
    fun withHockeyLabelAndUrl() {
        val result = buildComment(versionName = mockHockeyArtefactVersionName, hockeyUrl = mockHockeyArtefactUrl)

        val expectedResult = """
            HockeyApp: [Version 2.0-RC|https://hockey.url]
        """.trimIndent()

        assert(result == expectedResult)
    }

    @Test
    fun withHockeyLabel() {
        val result = buildComment(versionName = mockHockeyArtefactVersionName)

        val expectedResult = """
            HockeyApp: Version 2.0-RC
        """.trimIndent()

        assert(result == expectedResult)
    }
}