package com.outware.omproject.cerberus.data

import com.google.gson.Gson
import com.outware.omproject.cerberus.CerberusPlugin
import com.outware.omproject.cerberus.exceptions.JiraClientException
import com.outware.omproject.cerberus.exceptions.JsonSerialisationException
import com.outware.omproject.cerberus.util.toBase64
import java.io.BufferedReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext

private const val HTTP_METHOD_POST = "POST"

private const val HEADER_AUTHORIZATION = "Authorization"
private const val HEADER_CONTENT_TYPE = "Content-Type"

private const val CLIENT_REQUEST_CONTENT_TYPE = "application/json; charset=utf-8"

class JiraClient(url: String, trustAllCerts: Boolean = false) {
    var connection: HttpsURLConnection = URL(makeJiraRequestAddress(url)).openConnection() as HttpsURLConnection

    init {
        if (CerberusPlugin.properties?.disableJiraSSLVerify == true || trustAllCerts) {
            connection.sslSocketFactory = SSLContext.getInstance("SSL").apply {
                init(null, arrayOf(X509TrustAllManager()), null)
            }.socketFactory
        }
    }

    val responseReader: BufferedReader
        get() = connection.inputStream.bufferedReader()

    fun post(toSerialise: Any): Int {
        lateinit var jsonSerialisedRequest: String
        try {
            jsonSerialisedRequest = Gson().toJson(toSerialise)
        } catch (t: Throwable) {
            throw JsonSerialisationException("Failed to serialise request to Json: ${t.message}")
        }
        return post(jsonSerialisedRequest)
    }

    fun post(jsonBody: String): Int {
        try {
            connection.apply {
                requestMethod = HTTP_METHOD_POST
                doOutput = true

                setRequestProperty(HEADER_AUTHORIZATION, getJiraBasicAuth())
                setRequestProperty(HEADER_CONTENT_TYPE, CLIENT_REQUEST_CONTENT_TYPE)
            }

            connection.outputStream.write(jsonBody.toByteArray(Charsets.UTF_8))

            return connection.responseCode
        } catch (t: Throwable) {
            throw JiraClientException("Failed to connect to Jira Client: ${t.message}")
        }
    }

    private fun getJiraBasicAuth(): String {
        val jiraUsername = CerberusPlugin.properties?.jiraUsername
        val jiraPassword = CerberusPlugin.properties?.jiraPassword

        return "Basic " + "$jiraUsername:$jiraPassword".toBase64()
    }
}