package com.anma

import com.google.gson.Gson
import com.google.gson.JsonObject

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class ConfRest {

    static def getRest() {

        def response = getChildren(1062177161)

        Gson gson = Gson.newInstance()
        JsonObject page = gson.fromJson(response.body(), JsonObject.class)
//        def id =  page.get("id")
        println(page)

    }

    static void main(String[] args) {
        getRest()
    }

    static HttpResponse<String> getChildren(id) {

        def client = HttpClient.newBuilder().build()
        def request = HttpRequest.newBuilder()
                .uri(URI.create("https://bass.netcracker.com/rest/api/content/${id}/child/page"))
                .headers("Authorization", "Basic AAA")
                .GET()
                .build()
        def response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return response
    }

}
