package com.anma.jira.serv

import com.anma.jira.models.Issue
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class IssueService {
    Gson GSON = new GsonBuilder().setPrettyPrinting().create()
    String TOKEN = Base64.encoder.encodeToString("${System.getenv("LOCAL_JIRA_USER")}:${System.getenv("LOCAL_JIRA_PASS")}".bytes)
    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(20)).build()

    public Issue getIssue(String key) {
        def url = "http://localhost:9500/rest/api/2/issue/${key}"
        HttpRequest getIssue = HttpRequest.newBuilder()
            .GET()
        .header("Authorization", "Basic ${TOKEN}")
        .uri(URI.create(url))
        .build()

        def resp = client.send(getIssue, HttpResponse.BodyHandlers.ofString())
        if (resp.statusCode() == 200) {
            return GSON.fromJson(resp.body(), Issue.class)
        }
        return null
    }

    public Issue getSubTasks(String key) {
        def url = "http://localhost:9500/rest/api/2/issue/${key}/subtask"
        HttpRequest getIssue = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", "Basic ${TOKEN}")
                .uri(URI.create(url))
                .build()

        def resp = client.send(getIssue, HttpResponse.BodyHandlers.ofString())
        if (resp.statusCode() == 200) {
            return GSON.fromJson(resp.body(), Issue.class)
        }
        return null
    }

    /*
        Get issue watchers
        GET /rest/api/2/issue/{issueIdOrKey}/watchers
        Add watcher
        POST /rest/api/2/issue/{issueIdOrKey}/watchers
        Remove watcher
        DELETE /rest/api/2/issue/{issueIdOrKey}/watchers

        PUT /rest/api/2/issue/{issueIdOrKey}/assignee
        POST /rest/api/2/issue
        POST /rest/api/2/issue/bulk

        PUT /rest/api/2/issue/{issueIdOrKey}
        GET /rest/api/2/issue/{issueIdOrKey}/comment


     */

}
