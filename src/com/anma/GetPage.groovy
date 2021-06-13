package com.anma

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class GetPage {

    final String CONF_URL = "https://bass.netcracker.com"
    long pageId = 1065015088;    // set page ID
    HttpRequest request = HttpRequest.newBuilder(
//                URI.create("https://bassdevqa.netcracker.com/rest/api/content/230366342?expand=body.view"))
            URI.create("${CONF_URL}/rest/api/content/${pageId}?expand=body.view,version"))
            .headers("Authorization", "Basic TOKEN")
            .GET().build();
//        HttpHeaders httpHeaders = HttpHeaders();
    HttpClient client = HttpClient.newBuilder().build();
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
}
