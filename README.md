# Cerberus Gradle Plugin

Konrad Biernacki, 2018

## Installation

### buildSrc Plugin

1. If you don't have one already, create a `buildSrc` folder in your target project root directory
2. Copy this project into the `buildSrc` directory  
*eg. `target_project/buildSrc/build.gradle`*

### JAR Plugin

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

1. Add the plugin to your target project `build.gradle`
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

## Tasks

Cerberus adds two tasks into your project classpath

`cerberus_makeReleaseNotes`
- Generate release notes from all changes between the HEAD and a specified SHA-1 (`cerberus.lastSuccessfulCommit`)
- Place those changes into the plugin extension at `cerberus.releaseNotes`

`cerberus_updateTicket`
- Comment on any identified Jira tickets with:
    - Build information
        - `cerberus.buildNumber`
        - `cerberus.buildUrl`
    - HockeyApp upload information
        - `cerberus.hockeyAppUploadUrl`
        - `cerberus.hockeyAppShortVersion`

## Configuration

The plugin extension is documented in `CerberusPluginExtension.kt`
