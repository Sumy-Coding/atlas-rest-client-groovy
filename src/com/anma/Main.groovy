package com.anma

import com.anma.models.Contents
import com.anma.services.PageService
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Main {

    static void main(String[] args) {

        final String CONF_URL = "https://bass.netcracker.com"
//        final def CONF_URL = "http://localhost:8712"

        def toFind = "lorem dolor"
        def toReplace = "REPLACED"
//        def id = 6324225

        // ************* Start ************

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
//        println(">> Enter URL")
//        String CONF_URL = reader.readLine()
        println(">> Enter username")
        String username = reader.readLine()
        println(">> Enter password")
        String password = reader.readLine()
        println(">> Enter page ID")
        long id = reader.readLine().toInteger()

        // ******** Operations **********

        // GET page
//        println(PageService.getPage(CONF_URL, username, password, id))

        // GET children
//        println(PageService.getChildren(id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, "admin", "admin", id).each {println(it.title)})

        // GET space pages by label
//        println(PageService.getSpacePagesByLabel(CONF_URL, "admin", "admin", "TEST", "test").each {println(it.title)})

        // GET descendants by label
//        println(PageService.getDescendantsWithLabel(CONF_URL, "admin", "admin", id, "test").each {println(it.title)})

        // PUT -> update page
//        println(PageService.updatePage(CONF_URL, "admin", "admin",6324225, toFind, toReplace))

        // PUT -> update pages with specific label
//        PageService.getDescendantsWithLabel(CONF_URL, username, password, id, "test").each {
//            println(PageService.updatePage(CONF_URL, username, password, it.id, toFind, toReplace))
//        }

        // PUT children
//        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // Add labels to page
        PageService.addLabelsToPage(CONF_URL, username, password, id, ["added_1", "added_2"])


    }

}
