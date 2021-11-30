package com.anma

import com.anma.models.Space
import com.anma.services.PageService
import com.anma.services.RandomGen
import com.anma.services.SpaceService
import com.anma.services.TokenService

import java.time.Duration
import java.time.LocalDate
import org.apache.commons.lang3.RandomUtils;
import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Main {

    static void main(String[] args) {

// ====== DATA
        //=========== DU

        def username = System.getenv("CONF_USER")
        def password = System.getenv("CONF_PASS")
//        final String CONF_URL = "https://confluence-datacenter.du.ae"
        //========== bh
//        def username = "beastiehut@gmail.com";
//        def username = "andymaliuta@gmail.com";
// DC AWS
//        def username = System.getenv("USERNAME")
//        def password = System.getenv("PASSWORD")
//        def username = "admin"
//        def password = "admin"

//===== BASS
//        final String CONF_URL = "https://bass.netcracker.com"
//        final String CONF_URL = "https://bassdevqa.netcracker.com"      // DEVQA
//        final String CONF_URL = "https://beastiehut.atlassian.net/wiki"      // bh Cloud
//        final String CONF_URL = "https://anma.atlassian.net/wiki"      // anma Cloud
        final def CONF_URL = "http://localhost:7141"                    // localhost
//        final def CONF_URL = "http://confl-loadb-1mob5tjjndhrr-969460925.us-west-2.elb.amazonaws.com"       // AWS DC

        // ************* Start ************
        def TOKEN = TokenService.getToken(username, password)
        def start = System.currentTimeMillis()
//        Space space = SpaceService.getSpace(CONF_URL, TOKEN, key)

        // ******** Operations **********

        // GET page
//        println(PageService.getPage(CONF_URL, TOKEN, 5242886))

        // GET children
//        println(PageService.getChildren(CONF_URL, username, password, id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, username, password, id).each {println(it.title)})

        // GET space pages
//        println(PageService.getSpacePages(CONF_URL, TOKEN, 'TEST').results)

        // GET blogs
//        println(PageService.getSpaceBlogs(CONF_URL, TOKEN, 'TEST').results)

        //=== POST blogpost
//        for (i in 0..<50) {
//            def postDate =
//                    LocalDate.of(2021, RandomUtils.nextInt(1,12), RandomUtils.nextInt(1,30))
//                            .format(DateTimeFormatter.ofPattern("yyyy-MM-DD"))
//            println(PageService.createBlog(CONF_URL, TOKEN, "dev1", postDate, "blog ${i}", RandomGen.getRandomString(30)))
//        }

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

        // ==== PUT children

//        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // ==== Add labels to page

//        println(PageService.addLabelsToPage(CONF_URL, TOKEN, 1966081, ["added_1", "added_2"]))

        // GET labels
//        PageService.getPageLabels(CONF_URL, TOKEN, 2490384).results.each {println(it)}

        // ==== Rename Page

        // rest test - 1065015088
//        PageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

        // ==== Rename pages

//        PageService.getChildren(CONF_URL, username, password, id).results.each {
//            PageService.findReplacePageTitlePart(CONF_URL, username, password, it.id, "JBTB8", "JBTB1")
//        }

        // ==== replace PAGE INFO

//        PageService.replacePageInfoMacro(CONF_URL, username, password, id)

        // ==== multiple page info

//        PageService.getDescendants(CONF_URL, username, password, id).results.each {
//            PageService.replacePageInfoMacro(CONF_URL, username, password, it.id)
//        }

// ========  Create comment

//        for (i in 1..20) {
//            String randomString = getRandomString(20)
//            println(PageService.createComment(CONF_URL, TOKEN, space.key, [], space.homepage.id, 'page', randomString).body)
//        }

        // == Create comments for children

//        PageService.getChildren(CONF_URL, TOKEN, 1310845).results.each {
//            for (i in 1..30) {
//                String randomString = RandomGen.getRandomString(22)
//                println(PageService.createComment(CONF_URL, TOKEN, "dev62", [], it.id, 'page', randomString).body)
//            }
//        }

        //== Create spaces
//        for (i in 1..<30) {
//            println(SpaceService.createSpace(CONF_URL, TOKEN, "dev" + i, "dev" + i))
//        }

        // ======== create pages for Spaces

//        for (i in 1..<30) {
//            Space space = SpaceService.getSpace(CONF_URL, TOKEN, "dev" + i)
//            def homePage = PageService.getPage(CONF_URL, TOKEN, space.homepage.id)
//            for (a in 1..50) {
//                def pageBody = RandomGen.getRandomString(100)
//                def title = "${space.key} Page New " + a
//                println(PageService.createPage(CONF_URL, TOKEN, space.key, homePage.id, title, pageBody).body)
//            }
//        }

// ======= Create PAGES

//        for (i in 1..200) {
//            def pageBody = getRandomString(100)
//            def title = "${space.key} Page New " + i
//            println(PageService.createPage(CONF_URL, TOKEN, key, space.homepage.id, title, pageBody).body)
//        }

        // ========== Create Spaces

//        for (i in 2..<100) {
//            println(SpaceService.createSpace(CONF_URL, TOKEN, "dev${i}", "dev${i}"))
//        }

        // === MOVE page
//        println(PageService.movePage(CONF_URL, TOKEN, 1966087, 1966081))

        // COPY page
//        println(PageService.copyPage(CONF_URL, TOKEN, 65603, 1966081, "ababa", true, false, false))

        // copy page labels
//        println(PageService.copyPageLabels(CONF_URL, TOKEN, 65603, 1966108))

        // GET Attaches
//        println(PageService.getPageAttachments(CONF_URL, TOKEN, 1966096).results)

        // ATTACH file to page

//        println(PageService.addAttachToPage(CONF_URL, TOKEN, 1966083, 1966081))

        //===  Copy Page attches
//        println(PageService.copyPageAttaches(CONF_URL, TOKEN, 65603, 1966081))

        // === Copy children
        PageService.copyChildren(CONF_URL, TOKEN, 2031619, 2031638, "",
                true,true, false,"","","")

        // === add Labels to Ancestors
//        PageService.addLabelsToAncestors(CONF_URL, TOKEN, 5242886, ["aaa", "bbb"])



        // END
        def takenMillis = System.currentTimeMillis() - start
        long seconds = Duration.of(takenMillis, ChronoUnit.MILLIS).seconds
        println(">>>>> Script took ${seconds} seconds")

    }

}
