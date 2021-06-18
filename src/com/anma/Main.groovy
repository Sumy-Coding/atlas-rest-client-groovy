package com.anma

import com.anma.models.Contents
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Main {

    static void main(String[] args) {

        // GET
//        println(GetPage.getPage(6324225).body())
//        println(GsonService.httpToGson(GetPage.getPage(6324225)).id)

        // PUT
//        println(UpdatePage.updatePage(6324225))


        def pagesString = PageService.getDescendants(6324225).body()
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        def pages = gson.fromJson(pagesString, Contents.class)
        pages.results.each {page -> println(page.title)}

    }

}
