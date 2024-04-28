package com.anma.confl


import com.anma.confl.services.CommentService
import com.anma.confl.services.PageService
import com.anma.confl.services.SpaceService
import com.anma.srv.RandomGen
import com.anma.srv.TokenService
import org.apache.commons.lang3.RandomUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.time.Duration
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class Main {
    static void main(String[] args) {
        def start = System.currentTimeMillis()

        final Logger LOG = LoggerFactory.getLogger(Main.class)

        if (args.length > 0) {
            //  DATA
            def username = System.getenv("CONF_USER")
            def password = System.getenv("CONF_PASS")
            String CONF_URL = System.getenv("CONF_URL")
            String TOKEN = TokenService.getToken(username, password)

            // ******** Operations **********
            PageService pageService = new PageService()
            SpaceService spaceService = new SpaceService()
            CommentService commentService = new CommentService()

            def action = args[Arrays.asList(args).indexOf("--action") + 1]
            def pageId = args[Arrays.asList(args).indexOf("--id") + 1]
            def parent = args[Arrays.asList(args).indexOf("--parent") + 1]
            def spaceKey = args[Arrays.asList(args).indexOf("--spaceKey") + 1]
            String label = args[Arrays.asList(args).indexOf("--label") + 1]
            String toFind = args[Arrays.asList(args).indexOf("--toFind") + 1]
            String toSet = args[Arrays.asList(args).indexOf("--toSet") + 1]

            if (action != null) {
                LOG.info(">>> $action for Page $pageId")

                switch (action) {
                    case "getPage":
                        def page = pageService.getPage(CONF_URL, TOKEN, pageId)
                        LOG.info(page)
                        break
                    case "getSpace":
                        def space = spaceService.getSpace(CONF_URL, TOKEN, spaceKey)
                        LOG.info(space)
                        break
                    case "getChildren":
                        def contents = pageService.getChildren(CONF_URL, TOKEN, pageId).results
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "descendants":
                        def contents = pageService.getDescendants(CONF_URL, TOKEN, pageId).results
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "getSpacePages":
                        def contents = pageService.getSpacePages(CONF_URL, TOKEN, spaceKey).results
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "pagesByLabel":
                        if (label != null) {
                            def contents = pageService.getSpacePagesByLabel(CONF_URL, TOKEN, spaceKey, label).results
                            contents.each {
                                println(it)
                            }
                        }
                        break
                    case "getDescendantsWithLabel":
                        if (label != null) {
                            def contents = pageService.getDescendantsWithLabel(CONF_URL, TOKEN, spaceKey, label).results
                            contents.each {
                                LOG.info(it)
                            }
                        }
                        break
                    case "getSpaceBlogs":
                        def contents = pageService.getSpaceBlogs(CONF_URL, TOKEN, spaceKey).results
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "createPage":
                        def contents = pageService.getDescendants(CONF_URL, TOKEN, pageId).results
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "updatePage":
                        def contents = pageService.updateContentOnPage(CONF_URL, TOKEN, pageId, toFind, toSet)
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "createBlogs":
                        int amount = Integer.parseInt(args[Arrays.asList(args).indexOf("--amount") + 1])
                        for (i in 0..<amount) {
                            def postDate = LocalDate.of(
                                    Year.now().getValue(),
                                    RandomUtils.nextInt(1, 12),
                                    RandomUtils.nextInt(1, 30)
                            ).format(DateTimeFormatter.ofPattern("yyyy-MM-DD"))
                            def blog = pageService.createBlog(CONF_URL, TOKEN, spaceKey, postDate,
                                    "blog ${i}",
                                    RandomGen.getRandomString(30))
                            LOG.info(blog.toString())
                        }
                        break
                }
            }

            // ==== PUT children
//        pageService.getChildren(id).results.each {page -> pageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

            // ==== Add labels to page
//        pageService.addLabelsToPage(CONF_URL, TOKEN, 1966081, ["added_1", "added_2"])

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

//            pageService.getChildren(CONF_URL, TOKEN, 2752538).results.each {
//                for (i in 1..10) {
//                    String randomString = RandomGen.getRandomString(22)
//                    println(pageService.createComment(CONF_URL, TOKEN, "DDB26", [], it.id, 'page', randomString).body)
//                }
//            }

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

//        for (i in 1..<21) {
//            def pageBody = RandomGen.getRandomString(40)
//            def title = "Test Groovy Page ${System.nanoTime()}"
//            pageService.createPage(
//                    CONF_URL, TOKEN,
//                    "dev14",
//                    "2752538", title, pageBody)
//        }

// ========== Create Spaces
//        for (i in 2..<30) {
//            println(SpaceService.createSpace(CONF_URL, localTOKEN, "dev${i}", "dev${i}"))
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
            def takenMillis = System.currentTimeMillis() - start
            long millis = Duration.of(takenMillis, ChronoUnit.MILLIS).toMillis()
            long seconds = Duration.of(takenMillis, ChronoUnit.MILLIS).toSeconds()
            println(">>>>> Script took ${millis} millis (${seconds} seconds)")
        } else {
            println(">> Please enter the necessary data")
        }

    }
}
