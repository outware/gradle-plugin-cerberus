package com.outware.omproject.cerberus.util

fun makeReleaseNotes(changes: List<String>, buildUrl: String?): String =
        StringBuilder().apply {
            append("### Changelog\n")

            if (changes.isEmpty()) {
                append("*No changes found.*\n")
            } else {
                changes.forEach {
                    append("- $it\n")
                }
            }

            buildUrl?.let {
                append("\nBuilt by [Jenkins]($it)")
            }
        }.toString()