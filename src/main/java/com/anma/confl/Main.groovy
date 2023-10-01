package com.anma.confl

import com.anma.confl.models.Space
import com.anma.confl.services.CommentService
import com.anma.confl.services.PageService
import com.anma.confl.services.SpaceService
import com.anma.srv.RandomGen
import com.anma.srv.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class Main {
    static void main(String[] args) {
        final Logger LOG = LoggerFactory.getLogger(Main.class)

// ====== DATA
        def username = System.getenv("CONF_USER")
        def password = System.getenv("CONF_PASS")

        String CONF_URL = System.getenv("CONF_URL")

        String localTOKEN = TokenService.getToken("admin", "admin")
        String TOKEN = TokenService.getToken(username, password)

        def start = System.currentTimeMillis()

        // ******** Operations **********
        PageService pageService = new PageService()
        SpaceService spaceService = new SpaceService()
        CommentService commentService = new CommentService()

        // GET page
//        println(pageService.getPage(CONF_URL, TOKEN, "78282867"))

        // GET children
//        println(pageService.getChildren(anmaURL, TOKEN, 511180801).results)

        // GET descendants
//        pageService.getDescendants(awsCONF_URL, localTOKEN, 5832764).results.each {println(it.title)}

        // ALL
//        pageService.getContent(local810CONF_URL, localTOKEN, "page")
//                .results.each {println(it.id)}

        // GET space pages
//        pageService.getSpacePages(local714CONF_URL, localTOKEN, 'DEV').results.each {println(it)}

        // GET blogs
//        println(pageService.getSpaceBlogs(CONF_URL, TOKEN, 'TEST').results)

        //=== CREATE blogpost
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
//        pageService.getDescendantsWithLabel(CONF_URL, username, password, id, "test").each {
//            println(PageService.updatePage(CONF_URL, username, password, it.id, toFind, toReplace))
//        }

        // ==== PUT children

//        pageService.getChildren(id).results.each {page -> pageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // ==== Add labels to page

//        println(pageService.addLabelsToPage(CONF_URL, TOKEN, 1966081, ["added_1", "added_2"]))

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

//        Space space = SpaceService.getSpace(CONF_URL, TOKEN, "TEST")
//        def pageId = "822018049"
//        for (i in 1..20) {
//            String randomString = RandomGen.getRandomString(20)
//            println(
//                    pageService.createComment(
//                            CONF_URL, TOKEN, space.key, [],
//                            pageId, 'page', randomString).body)
//        }

        // == Create comments for children

//        pageService.getChildren(local8110CONF_URL, localTOKEN, 1212420).results.each {
//            for (i in 1..30) {
//                String randomString = RandomGen.getRandomString(22)
//                println(pageService.createComment(local8110CONF_URL, localTOKEN, "dev", [], it.id, 'page', randomString).body)
//            }
//        }

        // CREATE page
//        String pageBody = new String(Files.readAllBytes(Path.of("/home/user/confl_html_ex1.html")))
//        println(pageService.createPage(CONF_CLOUD, TOKEN, "TEST", "513966112", "Groovy from PDF", pageBody).body)


        // ======== create pages for Spaces

//        for (i in 2..<80) {
//            Space space = SpaceService.getSpace(local8110CONF_URL, localTOKEN, "dev" + i)
//            def homePage = pageService.getPage(local8110CONF_URL, localTOKEN, space.homepage.id)
//            for (a in 1..50) {
//                def pageBody = RandomGen.getRandomString(100)
//                def title = "${space.key} Page New " + a
//                println(pageService.createPage(local8110CONF_URL, localTOKEN, space.key, homePage.id, title, pageBody).body)
//            }
//        }

// ======= Create PAGES

//        for (i in 1..200) {
//            def pageBody = RandomGen.getRandomString(40)
//            def title = "Test Page ${LocalDate.now()} - ${i}"
//            println(pageService.createPage(
//                    CONF_URL, TOKEN,
//                    "TEST",
//                    "519307432", title, pageBody).body)
//        }


//            for (i in 1..20) {
//                def actions = CompletableFuture.runAsync {
//                    def pageBody = RandomGen.getRandomString(40)
//                    def title = "Test Page ${System.nanoTime()} - ${i}"
//                    println(pageService.createPage(
//                            CONF_URL, TOKEN,
//                            "TEST",
//                            "519308214", title, pageBody).body)
//                }.thenApply {
//                    println(">> ended")
//                }
//
//            }


        for (i in 0..<10) {
            def pageBody = RandomGen.getRandomString(40)
            def title = "Test Page ${System.nanoTime()} - ${i}"
            pageService.createPage(
                    CONF_URL, TOKEN,
                    "TEST",
                    "519309618", title, pageBody)
        }


        // ========== Create Spaces

//        for (i in 2..<100) {
//            println(SpaceService.createSpace(local8110CONF_URL, localTOKEN, "dev${i}", "dev${i}"))
//        }

        // === MOVE page
//        println(pageService.movePage(CONF_URL, TOKEN, 1966087, 1966081))

        // ==  COPY page
//        println(pageService.copyPage(CONF_URL, TOKEN, 65603, 1966081, "ababa", true, false, false))

        // ==== copy page between servers
        // 1 page
//        println(pageService.copyPage(local714CONF_URL, localTOKEN, 6160387, 511180801, "",
//                true, true, false, anmaURL, TOKEN))
        // children
//        pageService.copyChildren(local714CONF_URL, localTOKEN, 65603, 509542401, "",
//                true, true, false, anmaURL, TOKEN)

// ===========================================================================================================
// ============================================ COMMENTS =====================================================
//        println(pageService.getPageComment(local715CONF_URL, localTOKEN, 65611))

//        pageService.addCommentToPage(local715CONF_URL, localTOKEN, 65611,65603,local715CONF_URL, localTOKEN)

//        println(
//          commentService.getPageComment(local8110CONF_URL, localTOKEN, 1216695)
//        )

        // add inline comment
//        println(
//                commentService.addInlineCommentToPage(1212677, local8110CONF_URL, localTOKEN, "lorem", "primis").status
//        )


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
////                "https://xxx.atlassian.net/wiki",
//                "",
//                "",
//                ""
////                System.getenv("BH_TOKEN")
//        )


        // DELETE
//        println(PageService.deletePage(CONF_URL, TOKEN, 465829921))
//        pageService.getDescendants(CONF_URL, TOKEN, 465764447).results.each {page ->
//            println(PageService.deletePage(CONF_URL, TOKEN, page.id))
//        }


        // END
//        println(Duration.ofMillis(27726).toSeconds())
        def takenMillis = System.currentTimeMillis() - start
        long millis = Duration.of(takenMillis, ChronoUnit.MILLIS).toMillis()
        long seconds = Duration.of(takenMillis, ChronoUnit.MILLIS).toSeconds()
        println(">>>>> Script took ${millis} millis (${seconds} seconds)")

    }

}
