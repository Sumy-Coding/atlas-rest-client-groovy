package com.anma.confl


import com.anma.confl.services.CommentService
import com.anma.confl.services.PageService
import com.anma.confl.services.SpaceService
import com.anma.srv.RandomGen
import com.anma.srv.TokenService
import org.apache.commons.lang3.RandomUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture

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
            String pageBody = args[Arrays.asList(args).indexOf("--pageBody") + 1]
            String title = args[Arrays.asList(args).indexOf("--title") + 1]

            if (action != null) {
                LOG.info(">>> $action for content $pageId")

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
                            def contents = pageService.getSpacePagesByLabel(CONF_URL, TOKEN, spaceKey, label)
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
                        def page = pageService.createPage(CONF_URL, TOKEN, spaceKey, parent, title, pageBody)
                        page.thenAccept { p -> LOG.info(">>> Created page $p") }
                        break
                    case "createPages":
                        int amount = Integer.parseInt(args[Arrays.asList(args).indexOf("--amount") + 1])
                        for (i in 0..<amount) {
                            def page = pageService.createPage(CONF_URL, TOKEN, spaceKey, parent, title, pageBody)
                            page.thenAccept { p -> LOG.info(">>> Created page $p") }
                        }
                        break
                    case "updatePage":
                        def contents = pageService.updateContentOnPage(CONF_URL, TOKEN, pageId, toFind, toSet)
                        contents.each {
                            LOG.info(it)
                        }
                        break
                    case "addLabels":
                        def labels = Arrays.asList(args[Arrays.asList(args).indexOf("--labels") + 1].split(","))
                        pageService.addLabelsToPage(CONF_URL, TOKEN, pageId, labels)
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
                    case "renameChildren":
                        pageService.getChildren(CONF_URL, TOKEN, pageId).results.each { page ->
                            pageService.findReplacePageTitlePart(CONF_URL, TOKEN, page.id, toFind, toSet)
                        }
                        break
                }
            }

            // GET labels
//        pageService.getPageLabels(CONF_URL, TOKEN, 2490384).results.each {println(it)}

            // ==== Rename Page
//        pageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        pageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        pageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

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


// ======== create N pages for N Spaces

//        for (i in 2..<80) {
//            Space space = SpaceService.getSpace(local8110CONF_URL, localTOKEN, "dev" + i)
//            def homePage = pageService.getPage(local8110CONF_URL, localTOKEN, space.homepage.id)
//            for (a in 1..50) {
//                def pageBody = RandomGen.getRandomString(100)
//                def title = "${space.key} Page New " + a
//                println(pageService.createPage(local8110CONF_URL, localTOKEN, space.key, homePage.id, title, pageBody).body)
//            }
//        }

// ======= Create N PAGES

//        for (i in 1..<21) {
//            def pageBody = RandomGen.getRandomString(40)
//            def title = "Test Groovy Page ${System.nanoTime()}"
//            pageService.createPage(
//                    CONF_URL, TOKEN,
//                    "dev14",
//                    "2752538", title, pageBody)
//        }

// ========== Create N Spaces
//        for (i in 2..<30) {
//            println(SpaceService.createSpace(CONF_URL, localTOKEN, "dev${i}", "dev${i}"))
//        }

            // === MOVE page
//        pageService.movePage(CONF_URL, TOKEN, 1966087, 1966081)

            // ==  COPY page
//        pageService.copyPage(CONF_URL, TOKEN, 65603, 1966081, "ababa", true, false, false)

            // ==== copy page between servers
            // 1 page
//        pageService.copyPage(local714CONF_URL, localTOKEN, 6160387, 511180801, "",
//                true, true, false, anmaURL, TOKEN)
            // children
//        pageService.copyChildren(local714CONF_URL, localTOKEN, 65603, 509542401, "",
//                true, true, false, anmaURL, TOKEN)

// ===========================================================================================================
// ============================================ COMMENTS =====================================================
//        pageService.getPageComment(local715CONF_URL, localTOKEN, 65611)

//        pageService.addCommentToPage(local715CONF_URL, localTOKEN, 65611,65603,local715CONF_URL, localTOKEN)

//        commentService.getPageComment(local8110CONF_URL, localTOKEN, 1216695)

        // add inline comment
//        commentService.addInlineCommentToPage(1212677, local8110CONF_URL, localTOKEN, "lorem", "primis").status


            // copy page labels
//        pageService.copyPageLabels(CONF_URL, TOKEN, 65603, 1966108)

            // GET Attaches
//        pageService.getPageAttachments(CONF_URL, TOKEN, 1966096).results

            // ATTACH file to page
//        pageService.addAttachToPage(CONF_URL, TOKEN, 1966083, 1966081)

            //===  Copy Page attches
//        pageService.copyPageAttaches(CONF_URL, TOKEN, 65603, 1966081)

            // === Copy children
//        pageService.copyChildren(CONF_URL, TOKEN, 65603, 5832724, "", true, true, false,
////                "https://xxx.atlassian.net/wiki",
//                "",
//                "",
//                ""
////                System.getenv("BH_TOKEN")
//        )

            // DELETE
//        PageService.deletePage(CONF_URL, TOKEN, 465829921)
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
