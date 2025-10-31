package com.anma.confl.cloud.v2

import com.anma.confl.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kong.unirest.Unirest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.net.http.HttpClient
import java.net.http.HttpRequest

public class CloudPageService {

    final Logger LOG = LoggerFactory.getLogger(CloudPageService.class)

    Gson gson = new GsonBuilder().setPrettyPrinting().create()

    HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build()

    public ConfluencePage getPageById(String CONF_URL, String TOKEN, String pageId) {
        def response = Unirest.get("${CONF_URL}/api/v2/pages/${pageId}?body-format=storage")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()
        return gson.fromJson(response.body, ConfluencePage.class)
    }


    def getChildren(CONF_URL, TOKEN, pageId) {
        def response = Unirest.get("${CONF_URL}/pages/${pageId}/direct-children")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    def pageContains(CONF_URL, TOKEN, id, toFind) {
        def page = getPageById(CONF_URL, TOKEN, id)
        return page.body.storage.value.contains(toFind)
    }

    public String getDescendants(CONF_URL, TOKEN, pageId) {
        def url = "${CONF_URL}/api/v2/pages/${pageId}/descendants"
        def response = Unirest.get(url)
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, String.class)
    }


    def createPage(CONF_URL, TOKEN, spaceId, parentId, title, body) {
        String reqBody = "{\n" +
                "  \"spaceId\": \"${spaceId}\",\n" +
                "  \"status\": \"current\",\n" +
                "  \"title\": \"${title}\",\n" +
                "  \"parentId\": \"${parentId}\",\n" +
                "  \"body\": {\n" +
                "    \"representation\": \"storage\",\n" +
                "    \"value\": \"${body}\"\n" +
                "  }\n" +
                "}";

        HttpRequest request = HttpRequest.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .uri(URI.create("${CONF_URL}/api/v2/pages"))
                .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .build()

        def response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString())

        return response.body()

    }


}
