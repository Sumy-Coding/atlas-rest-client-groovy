package com.anma

import java.net.http.HttpRequest
import java.net.http.HttpResponse

class UpdatePage {

    def updatePage(id) {

        def pageVersion = GetPage.getPage(id).version()
        def title = GetPage.getPage(id).title
        def toFind = "dolor sit amet"
        def toReplace = "REPLACED"

        String newBody = bodyValue.getAsString().replace(toFind, toReplace);
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
                .uri(URI.create("https://bass.netcracker.com/rest/api/content/" + pageId))
                .PUT(publisher)
                .headers("Authorization", "Basic TOKEN")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        System.out.println(postResponse);

    }
}
