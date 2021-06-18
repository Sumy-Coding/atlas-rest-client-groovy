package com.anma

import com.anma.models.Contents
import com.anma.services.PageService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Main {

    static void main(String[] args) {

//        final String CONF_URL = "https://bass.netcracker.com"
        def CONF_URL = "http://localhost:8712"

        def toFind = "lorem dolor"
        def toReplace = "REPLACED"

        def id = 6324225

        // GET page
//        println(PageService.getPage(id))

        // GET children
//        println(PageService.getChildren(id).results)

        // PUT
//        println(PageService.updatePage(CONF_URL, "admin", "admin",6324225, toFind, toReplace))

        // PUT children
        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}


    }

}
