package com.outware.omproject.cerberus.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class NonEssentialTask : DefaultTask() {

    @TaskAction
    fun taskAction() {
        try {
            run()
        } catch (t: Throwable) {
            println(t)
            println("Non essential task (${this.name}) failed. Continuing...")
        }
    }

    abstract fun run()
}