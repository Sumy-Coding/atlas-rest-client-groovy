package com.anma

import com.google.gson.Gson
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


        println(GetPageData.getChildrenIds(6324225))

    }

}
