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

class GetPageHttp {

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

        return response
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

}
