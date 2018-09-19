# Cerberus Gradle Plugin

Konrad Biernacki, 2018

## About

Cerberus is a gradle plugin for extracting Jira issues from commit messages and sharing information on its respective Jenkins job and HockeyApp upload. 
It can generate release notes for use by HockeyApp, and produce Jira comments with Jenkins build and HockeyApp artefact information.

Using all of Cerberus' features, you can use your commit history -
``` 
 * HEAD -> Develop
 | \ 
 *   \
 | \ |
 | | * [CER-12] Address PR Feedback
 | | * [CER-12] Fix bug
 | | | 
 | * | [CER-7] Add feature
 | / |
 |   /
 | /
```

to generate release notes using data from Jira and your build -
```
### Changelog
- [CER-12] UI Bug
- [CER-7] Minor Feature

Built by [Jenkins](https://jenkins.url/build/451)
```

and comment on your Jira tickets with build and artefact information
```
Jenkins: [Build #451|https://jenkins.url/build/451]
HockeyApp: [Version 2.0-RC|https://hockey.url/app/d3adcaf3]
```

## Installation

### As a buildSrc Plugin

1. If you don't have one already, create a `buildSrc` folder in your target project's root directory
2. Copy this project into the `buildSrc` directory (so the plugin `build.gradle` file would be found in `target_project/buildSrc/build.gradle`)

### As a JAR Plugin

1. Compile the plugin as a `.jar` file
2. Add the `.jar` to your target project (eg. `target_project/libs/cerberus.jar`)
3. Add the following to your target project's `build.gradle` file:
    ```groovy
    buildscript {
        dependencies {
            classpath files('libs/cerberus.jar')
        }
    }
    ```

## Usage

1. Add the plugin to your target project's `build.gradle`
    ```groovy
    apply plugin: 'com.outware.omproject.cerberus'
    ```
2. Specify any configuration parameters in the Gradle DSL plugin configuration block. A typical implementation could be as follows:
    ```groovy
    cerberus {
        shouldUpdateTicketAfter = ['pushToHockeyApp']
        
        jiraDomain = 'https://jira.mydomain.com'
        jiraUsername = 'cerberus'
        jiraPassword = 'password'
    }
    ```
3. Use Cerberus' tasks in your target build flow

    ```groovy
    task runCi {
        configure {
            group = 'Continuous Integration'
            description = "Main CI Task"
        }
    
        dependsOn('assemble', 
                  'cerberus_makeReleaseNotes', 
                  'pushToHockeyApp', 
                  'cerberus_updateTicket')
    }
    ```

## Gradle Tasks

Cerberus adds the following tasks to your project classpath:

`cerberus_makeReleaseNotes`
- Generate release notes from all changes between the HEAD and a specified SHA-1 (`cerberus.lastSuccessfulCommit`)
- Load those changes into the plugin extension at `cerberus.releaseNotes` as a markdown formatted string

`cerberus_updateTicket`
- Comment on any identified Jira tickets with:
    - Build information
        - `cerberus.buildNumber`
        - `cerberus.buildUrl`
    - HockeyApp upload information
        - `cerberus.hockeyAppUploadUrl`
        - `cerberus.hockeyAppShortVersion`

## Configuration

The plugin extension available through the Gradle DSL is documented at `CerberusPluginExtension.kt`

### Environment Variables

Some configuration parameters are populated from environment variables

| Parameter            | Environment Variable             |
|----------------------|----------------------------------|
| jiraDomain           | `CI_USER_JIRA_URL`               |
| jiraUsername         | `CI_USER_JIRA_CREDENTIALS_USR`   |
| jiraPassword         | `CI_USER_JIRA_CREDENTIALS_PSW`   |
| lastSuccessfulCommit | `GIT_PREVIOUS_SUCCESSFUL_COMMIT` |
| buildUrl             | `BUILD_URL`                      |
| buildNumber          | `BUILD_NUMBER`                   |

If additional (or alternate) environment variables need to be used, then configure them in the plugin DSL block like so:

```groovy
cerberus {
    jiraDomain = System.env.OTHER_JIRA_DOMAIN
    ...
}
```

### Default Values

Some configuration parameters have default values 

| Parameter            | Default Value   |
|----------------------|-----------------|
| ticketRegex          | `"[A-Z]+-\\d+"` |
| gitLogPrettyFormat   | `"%s"`          |
| disableJiraSSLVerify | `false`         |
