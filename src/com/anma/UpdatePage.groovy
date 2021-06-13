package com.anma

import java.net.http.HttpRequest
import java.net.http.HttpResponse

class UpdatePage {

    def updatePage(id) {

//        final String CONF_URL = "https://bass.netcracker.com"
        final String CONF_URL = "http://localhost:8712"
//        def id = 6324225
        def pageVersion = GsonService.httpToGson(GetPage.getPage(id)).version
        def title = GsonService.httpToGson(GetPage.getPage(id)).title
        def body = GsonService.httpToGson(GetPage.getPage(id)).body.view.value

        def toFind = "dolor sit amet"
        def toReplace = "REPLACED"

        String newBody = body.replace(toFind, toReplace);
        String updatedPageBody = "{\n" +
                "    \"version\": {\n" +
                "        \"number\": " + (pageVersion + 1) + "\n" +
                "    },\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"type\": \"page\",\n" +
                "    \"body\": {\n" +
                "        \"storage\": {\n" +
                "            \"value\": \"" + newBody + "\",\n" +
                "            \"representation\": \"storage\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody);
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${CONF_URL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic TOKEN")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse);

    }
}
