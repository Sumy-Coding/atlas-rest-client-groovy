package com.anma

import com.anma.models.TitlePosition
import com.anma.services.PageService

class Main {

    static void main(String[] args) {

//=========== DU
//        def username = "Andrii.Maliuta";
//        final String CONF_URL = "https://confluence-datacenter.du.ae"
//===== BASS

        def username = "anma0513";
//        final String CONF_URL = "https://bass.netcracker.com"
        final String CONF_URL = "https://bassdevqa.netcracker.com"      // DEVQA
//        final def CONF_URL = "http://localhost:8712"

// ====== DATA
        def toFind = "lorem dolor"
        def toReplace = "REPLACED"
//        def id = 6324225

        // ************* Start ************

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
//        println(">> Enter URL")
//        String CONF_URL = reader.readLine()
//        println(">> Enter username")
//        String username = reader.readLine()
        println(">> Enter password")
        String password = reader.readLine()
        println(">> Enter page ID")
        long id = reader.readLine().toInteger()

        // ******** Operations **********

        // GET page
//        println(PageService.getPage(CONF_URL, username, password, id))

        // GET children
//        println(PageService.getChildren(CONF_URL, username, password, id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, username, password, id).each {println(it.title)})

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
//        PageService.addLabelsToPage(CONF_URL, username, password, id, ["added_1", "added_2"])

        // Rename Page
        // rest test - 1065015088
//        PageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

        // rename pages
//        PageService.getChildren(CONF_URL, username, password, id).results.each {
//            PageService.findReplacePageTitlePart(CONF_URL, username, password, it.id, "JBTB8", "JBTB1")
//        }

        // replace PAGE INFO
//        PageService.replacePageInfoMacro(CONF_URL, username, password, id)

        // multiple page info
        PageService.getDescendants(CONF_URL, username, password, id).results.each {
            PageService.replacePageInfoMacro(CONF_URL, username, password, it.id)
        }
    }

}
