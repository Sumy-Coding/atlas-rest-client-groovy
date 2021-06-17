package com.anma

import com.anma.models.Body
import com.anma.models.Content
import com.anma.models.Storage
import com.anma.models.Version
import com.google.gson.Gson
import com.thoughtworks.xstream.core.util.Base64Encoder

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class UpdatePage {

    static def updatePage(id) {

        def toFind = "ac:name=\"colour\">Blue"
        def toReplace = "ac:name=\"colour\">Yellow"

//        final String CONF_URL = "https://bass.netcracker.com"
        final String CONF_URL = "http://localhost:8712"
        def pageVersion = GsonService.httpToGson(GetPage.getPage(id)).version.number
        def title = GsonService.httpToGson(GetPage.getPage(id)).title
        String body = GsonService.httpToGson(GetPage.getPage(id)).body.storage.value

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

        Gson gson = new Gson();
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
        def TOKEN = new Base64Encoder().encode("admin:admin".bytes)
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
