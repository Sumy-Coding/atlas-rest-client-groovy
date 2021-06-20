package com.anma

import com.anma.models.Contents
import com.anma.services.PageService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Main {

    static void main(String[] args) {

        //http://localhost:8712/rest/api/content/search?cql=space=TEST%20AND%20label=test

//        final String CONF_URL = "https://bass.netcracker.com"
        def CONF_URL = "http://localhost:8712"

        def toFind = "lorem dolor"
        def toReplace = "REPLACED"

        def id = 6324225

        // GET page
//        println(PageService.getPage(id))

        // GET children
//        println(PageService.getChildren(id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, "admin", "admin", id).each {println(it.title)})

        // GET spacepages by label
//        println(PageService.getSpacePagesByLabel(CONF_URL, "admin", "admin", "TEST", "test").each {println(it.title)})

        // GET descendants by label
//        println(PageService.getDescendantsWithLabel(CONF_URL, "admin", "admin", id, "test").each {println(it.title)})

        // PUT
//        println(PageService.updatePage(CONF_URL, "admin", "admin",6324225, toFind, toReplace))

        // PUT children
//        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // Add labels to page
        PageService.addLabelsToPage(CONF_URL, "admin", "admin", id, ["added_1", "added_2"])


    }

}
