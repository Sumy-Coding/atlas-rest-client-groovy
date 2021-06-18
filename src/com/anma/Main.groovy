package com.anma

import com.anma.models.Contents
import com.anma.services.PageService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Main {

    static void main(String[] args) {

//        final String CONF_URL = "https://bass.netcracker.com"
        def CONF_URL = "http://localhost:8712"

        def toFind = "ac:name=\"colour\">Yellow"
        def toReplace = "ac:name=\"colour\">Blue"

        // GET page
//        println(PageService.getPage(6324225))

        // GET children
//        println(PageService.getChildren(6324225).results)

        // PUT
        println(PageService.updatePage(CONF_URL,6324225, toFind, toReplace))


    }

}
