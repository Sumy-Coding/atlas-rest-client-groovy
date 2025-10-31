package com.anma.jira.srv

import com.anma.jira.models.Project
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class ProjectService {
    Gson GSON = new GsonBuilder().setPrettyPrinting().create()
    String TOKEN = Base64.encoder.encodeToString("${System.getenv("JIRA_USER")}:${System.getenv("JIRA_PASS")}".bytes)
    String JIRA_HOST = System.getenv("JIRA_HOST")

    HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20)).build()

    public Project getProject(String idOrKey) {
        def url = "${JIRA_HOST}/rest/api/3/project/${idOrKey}"
        HttpRequest getProject = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", "Basic ${TOKEN}")
                .uri(URI.create(url))
                .build()

        def resp = client.send(getProject, HttpResponse.BodyHandlers.ofString())

        if (resp.statusCode() == 200) {
            return GSON.fromJson(resp.body(), Project.class)
        }
        return null
    }
}