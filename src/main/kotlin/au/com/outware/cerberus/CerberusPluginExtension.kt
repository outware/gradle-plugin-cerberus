package au.com.outware.cerberus

open class CerberusPluginExtension {

    /**
     * CONFIGURATION PROPERTIES
     */

    /**
     * A list of tasks the Ticket Update task should run after.
     * Use `null` to let gradle do its thing and avoid mandating the order.
     *
     * In groovy, set with:
     * ```groovy
     * shouldUpdateTicketAfter = ['taskNamOne', 'taskNameTwo']
     * ```
     *
     * @default `null`
     */
    var shouldUpdateTicketAfter: List<String>? = null

    /**
     *  Ignore SSL verification failures that throw [sun.security.validator.ValidatorException]
     *  when calling the Jira API (eg. self-signed certificates etc.)
     *
     *  @default `false`
     */
    var disableJiraSSLVerify: Boolean? = false

    /**
     * Jira instance URL. Required to access its restful API and to post comments.
     * eg. `https://jira.mydomain.com/`
     *
     * @default environment variable `CI_USER_JIRA_URL`
     */
    var jiraDomain: String? = System.getenv("CI_USER_JIRA_URL")

    /**
     * Jira account username. Required to access its restful API and to post comments.
     *
     * @default environment variable `CI_USER_JIRA_CREDENTIALS_USR`
     */
    var jiraUsername: String? = System.getenv("CI_USER_JIRA_CREDENTIALS_USR")

    /**
     * Jira account password. Required to access its restful API and to post comments.
     *
     * @default environment variable `CI_USER_JIRA_CREDENTIALS_PSW`
     */
    var jiraPassword: String? = System.getenv("CI_USER_JIRA_CREDENTIALS_PSW")

    /**
     * Last successful Git commit. Required to fetch the  changes since the last build.
     *
     * @default environment variable `GIT_PREVIOUS_SUCCESSFUL_COMMIT`
     */
    var lastSuccessfulCommit: String? = System.getenv("GIT_PREVIOUS_SUCCESSFUL_COMMIT")

    /**
     * Job build URL. Included in Jira comment.
     *
     * @default environment variable `BUILD_URL`
     */
    var buildUrl: String? = System.getenv("BUILD_URL")

    /**
     * Job build number. Included in Jira comment.
     *
     * @default environment variable `BUILD_NUMBER`
     */
    var buildNumber: String? = System.getenv("BUILD_NUMBER")

    /**
     *  A regular expression to match tickets against.
     *  Must be compatible with [java.util.regex.Pattern].
     *
     *  @default `[A-Z]+-\d+`
     */
    var ticketExtractionPattern: String? = "[A-Z]+-\\d+"

    /**
     *  A regular expression to filter commits from ticket extraction.
     *  Must be compatible with [java.util.regex.Pattern].
     *
     *  @default `null`
     */
    var commitIgnorePattern: String? = null

    /**
     *  A regular expression to include matching commits in the changelog without a ticket.
     *  Must be compatible with [java.util.regex.Pattern].
     *
     *  @default `null`
     */
    var commitPassthroughPattern: String? = null

    /**
     * The format git commits should be expressed in to extract tickets and changes from.
     * Documentation can be found here: [https://git-scm.com/docs/git-log#_pretty_formats](https://git-scm.com/docs/git-log#_pretty_formats)
     *
     * @default `%s`
     */
    var gitLogPrettyFormat: String? = "%s"

    /**
     * RUNTIME PROPERTIES
     */

    /**
     * Generated release notes, available after the `cerberus_makeReleaseNotes` task has completed.
     */
    var releaseNotes: String? = null

    /**
     * HockeyApp url to the uploaded artefact. Required to correctly display HockeyApp upload
     * information when commenting on Jira.
     */
    var hockeyAppUploadUrl: String? = null

    /**
     * HockeyApp shortversion of the uploaded aftefact. Required to correctly display HockeyApp
     * upload information when commenting on Jira.
     */
    var hockeyAppShortVersion: String? = null
}