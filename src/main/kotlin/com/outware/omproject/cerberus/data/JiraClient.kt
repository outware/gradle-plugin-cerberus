package com.outware.omproject.cerberus.data

import com.google.gson.Gson
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.util.toBase64
import java.io.BufferedReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

class JiraClient(url: String, trustAllCerts: Boolean = false) {
    var connection: HttpsURLConnection = URL(makeJiraEndpoint(url)).openConnection() as HttpsURLConnection

    init {
        if (CerberusPlugin.properties?.disableJiraSSLVerify == true || trustAllCerts) {
            connection.sslSocketFactory = SSLContext.getInstance("SSL").apply {
                init(null, arrayOf(X509TrustAllManager()), null)
            }.socketFactory
        }
    }

    val responseCode: Int
        get() = connection.responseCode

    val responseReader: BufferedReader
        get() = connection.inputStream.bufferedReader()

    val responseString: String
        get() = responseReader.readText()

    val errorResponseReader: BufferedReader
        get() = connection.errorStream.bufferedReader()

    val errorResponseString: String
        get() = errorResponseReader.readText()

    fun post(toSerialise: Any): Int = post(Gson().toJson(toSerialise))

    fun post(jsonBody: String): Int {
        connection.apply {
            requestMethod = "POST"
            doOutput = true

            setRequestProperty("Authorization", "Basic ${getJiraBasicAuth()}")
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }

        connection.outputStream.write(jsonBody.toByteArray(Charsets.UTF_8))

        return connection.responseCode
    }

    private fun getJiraBasicAuth(): String {
        val jiraUsername = CerberusPlugin.properties?.jiraUsername
        val jiraPassword = CerberusPlugin.properties?.jiraPassword

        return "$jiraUsername:$jiraPassword".toBase64()
    }
}