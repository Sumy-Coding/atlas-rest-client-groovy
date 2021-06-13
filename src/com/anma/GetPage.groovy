package com.anma

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

class GetPage {

    static def getPage(id) {
//        final String CONF_URL = "https://bass.netcracker.com"
        final String CONF_URL = "http://localhost:8712"
        id = 6324225
        long pageId = id;    // set page ID
        def TOKEN = Base64Encoder.newInstance().encode("admin:admin".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${pageId}?expand=body.view,version"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
//        HttpHeaders httpHeaders = HttpHeaders();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response
    }

    static void main(String[] args) {
        println(getPage(6324225).body())

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject jsonObject = gson.fromJson(getPage().body(), JsonObject.class);
        JsonElement id = jsonObject.get("id");
        String title = jsonObject.get("title").getAsString();

        JsonObject body = jsonObject.get("body").getAsJsonObject();
        JsonObject bodyView = body.get("view").getAsJsonObject();
        JsonElement bodyValue = bodyView.get("value");
        long pageVersion = jsonObject.get("version").getAsJsonObject().get("number").getAsLong();

        Document document = Jsoup.parse(bodyValue.getAsString());
        Elements links = document.select("a");
        links.forEach(link -> System.out.println(link.attr("href")));
    }

}
