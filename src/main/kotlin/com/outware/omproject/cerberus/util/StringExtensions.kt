package com.outware.omproject.cerberus.util

import java.util.*

fun String.toBase64(): String = Base64.getEncoder().encodeToString(this.toByteArray())