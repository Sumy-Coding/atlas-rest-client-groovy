package com.anma.services

import com.anma.models.Body
import com.anma.models.Content
import com.anma.models.Contents
import com.anma.models.Storage
import com.anma.models.Version
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.thoughtworks.xstream.core.util.Base64Encoder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PageService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static def getPage(id) {
//        final String CONF_URL = "https://bass.netcracker.com"
        final String CONF_URL = "http://localhost:8712"
//        id = 6324225
        def TOKEN = Base64Encoder.newInstance().encode("admin:admin".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}?expand=body.storage,version"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
//        HttpHeaders httpHeaders = HttpHeaders();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Content.class)


    }

    static def getChildren(id) {

        final String CONF_URL = "http://localhost:8712"
        def TOKEN = new Base64Encoder().encode("admin:admin".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}/child/page"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Contents.class)

    }

    static def getDescendants(id) {

        final String CONF_URL = "http://localhost:8712"
        def TOKEN = new Base64Encoder().encode("admin:admin".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}/child/page"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response

    }

    static def updatePage(confURL, username, password, id, toFind, toReplace) {

        final String CONF_URL = confURL

        def pageVersion = getPage(id).version.number
        def title = getPage(id).title
        String body = getPage(id).body.storage.value

//        println("+++ in update")
//        println(pageVersion)
//        println(title)
//        println(body)

        String newBody = body.replace(toFind, toReplace);

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
        version.message = "changed with REST"
        updatedPage.version = version
        updatedPage.title = title
        updatedPage.type = "page"
        Body updBody = new Body()
        Storage storage = new Storage()
        storage.value = newBody
        storage.representation = "storage"
        updBody.storage = storage
        updatedPage.body = updBody
//        println(updatedPage)

        String pageJSON = gson.toJson(updatedPage)  // convert to JSON
        println(pageJSON)

//        String updatedPageBody = "{\n" +
//                "    \"version\": {\n" +
//                "        \"number\": " + (pageVersion + 1) + "\n" +
//                "    },\n" +
//                "    \"title\": \"" + title + "\",\n" +
//                "    \"type\": \"page\",\n" +
//                "    \"body\": {\n" +
//                "        \"storage\": {\n" +
//                "            \"value\": \"" + newBody + "\",\n" +
//                "            \"representation\": \"storage\"\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        println("**** token is ${TOKEN}")

        /* Performing the PUT request to replace body */
        HttpClient client = HttpClient.newBuilder().build();
//        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody)
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${CONF_URL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        return postResponse.body()

    }

}
