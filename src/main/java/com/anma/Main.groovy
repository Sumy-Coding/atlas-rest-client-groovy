package com.anma

import com.anma.models.Space
import com.anma.services.PageService
import com.anma.services.RandomGen
import com.anma.services.SpaceService
import com.anma.services.TokenService

import java.time.Duration
import org.apache.commons.lang3.RandomUtils;
import java.time.temporal.ChronoUnit

class Main {

    static void main(String[] args) {

// ====== DATA
//        def username = System.getenv("CONF_USER")
//        def password = System.getenv("CONF_PASS")
//        final String duCONF_URL = "https://confluence-datacenter.du.ae"
//        final String bassURL = "https://bass.netcracker.com"
//        final String bassdevqaURL = "https://bassdevqa.netcracker.com"        // DEVQA
//        final String bhtURL = "https://beastiehut.atlassian.net/wiki"   // bh Cloud
        final String anmaURL = "https://anma.atlassian.net/wiki"            // anma Cloud
        final def local714CONF_URL = "http://localhost:7141"                    // localhost
        final def local715CONF_URL = "http://localhost:7150"                    // localhost
        final def local810CONF_URL = "http://localhost:8100"                    // localhost
        final String local7190CONF_URL = "http://localhost:7190"                    // localhost
        final String awsCONF_URL = "http://confl-loadb-pxymvhygf6ct-1493255270.us-west-2.elb.amazonaws.com" // AWS DC
        def localTOKEN = TokenService.getToken("admin", "admin")
        def anmaTOKEN = TokenService.getToken(System.getenv("ANMA_CONF_USER"), System.getenv("ANMA_CONF_TOKEN"))
        def bhtTOKEN = TokenService.getToken("beastiehut@gmail.com", System.getenv("BH_TOKEN"))
        def start = System.currentTimeMillis()
//        Space space = SpaceService.getSpace(CONF_URL, TOKEN, key)

        // ******** Operations **********

        PageService pageService = new PageService()
        SpaceService spaceService = new SpaceService()

        // GET page
//        println(PageService.getPage(anmaURL, anmaTOKEN, 511180801))

        // GET children
//        println(PageService.getChildren(anmaURL, anmaTOKEN, 511180801).results)

        // GET descendants
//        PageService.getDescendants(awsCONF_URL, localTOKEN, 5832764).results.each {println(it.title)}

        // ALL
        pageService.getContent(local810CONF_URL, localTOKEN, "page")
                .results.each {println(it.id)}

        // GET space pages
//        pageService.getSpacePages(local714CONF_URL, localTOKEN, 'DEV').results.each {println(it)}

        // GET blogs
//        println(PageService.getSpaceBlogs(CONF_URL, TOKEN, 'TEST').results)

        //=== POST blogpost
//        for (i in 0..<50) {
//            def postDate =
//                    LocalDate.of(2021, RandomUtils.nextInt(1,12), RandomUtils.nextInt(1,30))
//                            .format(DateTimeFormatter.ofPattern("yyyy-MM-DD"))
//            println(pageService.createBlog(CONF_URL, TOKEN, "dev1", postDate, "blog ${i}", RandomGen.getRandomString(30)))
//        }

        // GET space pages by label
//        println(pageService.getSpacePagesByLabel(CONF_URL, "admin", "admin", "TEST", "test").each {println(it.title)})

        // GET descendants by label
//        println(pageService.getDescendantsWithLabel(CONF_URL, "admin", "admin", id, "test").each {println(it.title)})

        // PUT -> update page
//        println(pageService.updatePage(CONF_URL, "admin", "admin",6324225, toFind, toReplace))

        // PUT -> update pages with specific label
//        PageService.getDescendantsWithLabel(CONF_URL, username, password, id, "test").each {
//            println(PageService.updatePage(CONF_URL, username, password, it.id, toFind, toReplace))
//        }

        // ==== PUT children

//        pageService.getChildren(id).results.each {page -> pageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // ==== Add labels to page

//        println(PageService.addLabelsToPage(CONF_URL, TOKEN, 1966081, ["added_1", "added_2"]))

        // GET labels
//        pageService.getPageLabels(CONF_URL, TOKEN, 2490384).results.each {println(it)}

        // ==== Rename Page

        // rest test - 1065015088
//        pageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        pageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        pageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

        // ==== Rename pages

//        pageService.getChildren(CONF_URL, username, password, id).results.each {
//            pageService.findReplacePageTitlePart(CONF_URL, username, password, it.id, "JBTB8", "JBTB1")
//        }

        // ==== replace PAGE INFO

//        pageService.replacePageInfoMacro(CONF_URL, username, password, id)

        // ==== multiple page info

//        pageService.getDescendants(CONF_URL, username, password, id).results.each {
//            pageService.replacePageInfoMacro(CONF_URL, username, password, it.id)
//        }

// ========  Create comment

//        for (i in 1..20) {
//            String randomString = getRandomString(20)
//            println(pageService.createComment(CONF_URL, TOKEN, space.key, [], space.homepage.id, 'page', randomString).body)
//        }

        // == Create comments for children

//        pageService.getChildren(CONF_URL, TOKEN, 1310845).results.each {
//            for (i in 1..30) {
//                String randomString = RandomGen.getRandomString(22)
//                println(pageService.createComment(CONF_URL, TOKEN, "dev62", [], it.id, 'page', randomString).body)
//            }
//        }

        // ======== create pages for Spaces

//        for (i in 2..<80) {
//            Space space = SpaceService.getSpace(awsCONF_URL, localTOKEN, "dev" + i)
//            def homePage = pageService.getPage(awsCONF_URL, localTOKEN, space.homepage.id)
//            for (a in 1..50) {
//                def pageBody = RandomGen.getRandomString(100)
//                def title = "${space.key} Page New " + a
//                println(pageService.createPage(awsCONF_URL, localTOKEN, space.key, homePage.id, title, pageBody).body)
//            }
//        }

// ======= Create PAGES

//        for (i in 1..49) {
//            def pageBody = RandomGen.getRandomString(2)
//            def title = "Groovy Page 2 ${i}"
//            println(pageService.createPage(local7190CONF_URL, localTOKEN, "GRROV", "1900863", title, "lorem lorem").body)
//        }


        // ========== Create Spaces

//        for (i in 2..<100) {
//            println(SpaceService.createSpace(awsCONF_URL, localTOKEN, "dev${i}", "dev${i}"))
//        }

        // === MOVE page
//        println(pageService.movePage(CONF_URL, TOKEN, 1966087, 1966081))

        // ==  COPY page
//        println(pageService.copyPage(CONF_URL, TOKEN, 65603, 1966081, "ababa", true, false, false))

        // ==== copy page between servers
        // 1 page
//        println(pageService.copyPage(local714CONF_URL, localTOKEN, 6160387, 511180801, "",
//                true, true, false, anmaURL, anmaTOKEN))
        // children
//        pageService.copyChildren(local714CONF_URL, localTOKEN, 65603, 509542401, "",
//                true, true, false, anmaURL, anmaTOKEN)
        // ==== COMMENTS
//        println(pageService.getPageComment(local715CONF_URL, localTOKEN, 65611))

//        pageService.addCommentToPage(local715CONF_URL, localTOKEN, 65611,65603,local715CONF_URL, localTOKEN)

        // copy page labels
//        println(pageService.copyPageLabels(CONF_URL, TOKEN, 65603, 1966108))

        // GET Attaches
//        println(pageService.getPageAttachments(CONF_URL, TOKEN, 1966096).results)

        // ATTACH file to page
//        println(pageService.addAttachToPage(CONF_URL, TOKEN, 1966083, 1966081))

        //===  Copy Page attches
//        println(pageService.copyPageAttaches(CONF_URL, TOKEN, 65603, 1966081))

        // === Copy children
//        pageService.copyChildren(CONF_URL, TOKEN, 65603, 5832724, "", true, true, false,
////                "https://beastiehut.atlassian.net/wiki",
//                "",
//                "",
//                ""
////                System.getenv("BH_TOKEN")
//        )

        //bht
//            pageService.copyChildren(CONF_URL, TOKEN, "465862662", "465600539", "", true, true, false,
//                "",
//                "",
//                "")

        // === add Labels to Ancestors
//        pageService.addLabelsToAncestors(CONF_URL, TOKEN, 5242886, ["aaa", "bbb"])

        // DELETE
//        println(PageService.deletePage(CONF_URL, TOKEN, 465829921))
//        pageService.getDescendants(CONF_URL, TOKEN, 465764447).results.each {page ->
//            println(PageService.deletePage(CONF_URL, TOKEN, page.id))
//        }



        // END
//        println(Duration.ofMillis(27726).toSeconds())
        def takenMillis = System.currentTimeMillis() - start
        long millis = Duration.of(takenMillis, ChronoUnit.MILLIS).toMillis()
        long seconds =  Duration.of(takenMillis, ChronoUnit.MILLIS).toSeconds()
        println(">>>>> Script took ${millis} millis (${seconds} seconds)")

    }

}
