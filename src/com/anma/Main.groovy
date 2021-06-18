package com.anma

import com.anma.models.Contents
import com.anma.services.PageService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Main {

    static void main(String[] args) {

        // GET
//        println(GetPage.getPage(6324225).body())
//        println(GsonService.httpToGson(GetPage.getPage(6324225)).id)

        // PUT
//        println(UpdatePage.updatePage(6324225))


        def pagesString = PageService.getChildren(6324225).body()
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        def pages = gson.fromJson(pagesString, Contents.class)
        pages.results.each {page -> println(page.title)}

    }

}
