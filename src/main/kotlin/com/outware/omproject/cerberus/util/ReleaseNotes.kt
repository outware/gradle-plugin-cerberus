package com.outware.omproject.cerberus.util

fun makeReleaseNotes(changes: List<String>, buildUrl: String?): String =
        mutableListOf("### Changelog").apply {
            if (changes.isEmpty()) {
                add("*No changes found.*")
            } else {
                addAll(changes.map { "- $it" })
            }

            buildUrl?.let {
                add("\nBuilt by [Jenkins]($it)")
            }
        }.joinToString("\n")