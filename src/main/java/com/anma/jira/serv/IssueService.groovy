package com.anma.jira.serv

import com.anma.jira.models.Issue
import com.anma.jira.models.Watcher
import com.anma.jira.models.update.Update
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class IssueService {
    Gson GSON = new GsonBuilder().setPrettyPrinting().create()
    String TOKEN = Base64.encoder.encodeToString("${System.getenv("LOCAL_JIRA_USER")}:${System.getenv("LOCAL_JIRA_PASS")}".bytes)
    String JIRA_HOST = System.getenv("JIRA_HOST")
    HttpClient client = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .connectTimeout(Duration.ofSeconds(20)).build()

    public Issue getIssue(String key) {
        def url = "${JIRA_HOST}/rest/api/2/issue/${key}"
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

    public Issue[] getSubTasks(String key) {
        def url = "${JIRA_HOST}/rest/api/2/issue/${key}/subtask"
        HttpRequest getIssue = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", "Basic ${TOKEN}")
                .uri(URI.create(url))
                .build()

        def resp = client.send(getIssue, HttpResponse.BodyHandlers.ofString())
        if (resp.statusCode() == 200) {
            return GSON.fromJson(resp.body(), Issue[].class)
        }
        return null
    }

    public Watcher[] getWatchers(String key) {
        def url = "${JIRA_HOST}/rest/api/2/issue/${key}/watchers"
        HttpRequest getIssue = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", "Basic ${TOKEN}")
                .uri(URI.create(url))
                .build()

        def resp = client.send(getIssue, HttpResponse.BodyHandlers.ofString())
        if (resp.statusCode() == 200) {
            return GSON.fromJson(resp.body(), Watcher[].class)
        }
        return null
    }

    public Issue updateIssue(String key, Update data) {
        // todo
    }

    /*
        Add watcher
        POST ${JIRA_HOST}/rest/api/2/issue/{issueIdOrKey}/watchers
        Remove watcher
        DELETE ${JIRA_HOST}/rest/api/2/issue/{issueIdOrKey}/watchers

        PUT /rest/api/2/issue/{issueIdOrKey}/assignee
        POST /rest/api/2/issue
        POST /rest/api/2/issue/bulk

        PUT /rest/api/2/issue/{issueIdOrKey}
        GET /rest/api/2/issue/{issueIdOrKey}/comment


     */



    /*
                        PROJECT
        Get all projects
        GET /rest/api/2/project
        Create project
        POST /rest/api/2/project
        Update project
        PUT /rest/api/2/project/{projectIdOrKey}
        Delete project
        DELETE /rest/api/2/project/{projectIdOrKey}
        Get project
        GET /rest/api/2/project/{projectIdOrKey}
        Archive project
        PUT /rest/api/2/project/{projectIdOrKey}/archive
        Create avatar from temporary
        POST /rest/api/2/project/{projectIdOrKey}/avatar
        Update project avatar
        PUT /rest/api/2/project/{projectIdOrKey}/avatar
        Delete avatar
        DELETE /rest/api/2/project/{projectIdOrKey}/avatar/{id}
        Store temporary avatar
        POST /rest/api/2/project/{projectIdOrKey}/avatar/temporary
        Store temporary avatar using multi part
        POST /rest/api/2/project/{projectIdOrKey}/avatar/temporary
        Get all avatars
        GET /rest/api/2/project/{projectIdOrKey}/avatars
        Get project components
        GET /rest/api/2/project/{projectIdOrKey}/components
        Restore project
        PUT /rest/api/2/project/{projectIdOrKey}/restore
        Get all statuses
        GET /rest/api/2/project/{projectIdOrKey}/statuses
        Update project type
        PUT /rest/api/2/project/{projectIdOrKey}/type/{newProjectTypeKey}
        Get project versions paginated
        GET /rest/api/2/project/{projectIdOrKey}/version
        Get project versions
        GET /rest/api/2/project/{projectIdOrKey}/versions
     */

}
