package com.anma.conf

import com.anma.confl.cloud.v2.CloudPageService
import com.anma.confl.services.PageService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

class PageServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")
    def TOKEN = System.getenv("AST_CLOUD_TOKEN")

    String CONF_URL = System.getenv("CONF_URL") != null ? System.getenv("CONF_URL") : "http://localhost:8930"
//    String TOKEN = TokenService.getToken(username, password)
    String localTOKEN = TokenService.getToken("admin", "admin")

    PageService pageService = new PageService()
    CloudPageService cloudPageService = new CloudPageService()

    @Test
    void getPage() {

        def page = pageService.getPage(CONF_URL, TOKEN, "39026694")

        println(page)
    }

    @Test
    void getPageCloud() {

        def page = cloudPageService.getPageById(CONF_URL, TOKEN, "39026694")

        println(page)
    }

    @Test
    void updatePage() {

        pageService.replaceCalendarMacro(CONF_URL, TOKEN, "331353976")
//        pageService.replaceCalendarMacroStatic(CONF_URL, TOKEN, "213156238")

    }

    @Test
    void getChildren() {
        def contents = pageService.getChildren(CONF_URL, TOKEN, "340093257").results

        contents.each {
            println(it)
        }

    }

    @Test
    void descendants() {
        def contents = pageService.getDescendants(CONF_URL, TOKEN, "2752538").results

        contents.each {
            println(it)
        }

    }

    @Test
    void createPage() {
        def createdPage = pageService.createPage(
                CONF_URL, localTOKEN,
                "dev3",
                1572866,
                "Groovy dev ${System.currentTimeMillis()}", "Groovy lorem ...")

        println(createdPage)
    }

    @Test
    void createPageCloud() {
        def createdPage = cloudPageService.createPage(
                CONF_URL,
                TOKEN,
                "15892733",
                15892829,
                "Groovy dev ${System.currentTimeMillis()}",
                "Groovy lorem ...")

        println(createdPage)
    }

    @Test
    void createPageAsyncTest() {
        String spaceKey = "dev3"
        def parentPageId = 1572866

        def createdPage = pageService.createPageAsync(
                CONF_URL,
                localTOKEN,
                spaceKey,
                parentPageId,
                "Groovy dev ${System.currentTimeMillis()}",
                "Groovy lorem ...")

        createdPage.thenAccept(res -> {
            println(">>> response: ${res.body()}")
        }).get()
    }

    @Test
    void createPages() {
        String spaceKey = "dev3"
        def parentPageId = 1572866

        for (i in 0..<20) {
            def createdPage = pageService.createPage(
                    CONF_URL, localTOKEN,
                    spaceKey,
                    parentPageId,
                    "Groovy dev ${System.currentTimeMillis()}",
                    "Groovy lorem ...")

            println(createdPage)
        }
    }

    @Test
    void createPagesCloud() {
        for (i in 0..<10) {
            def createdPage = cloudPageService.createPage(
                    CONF_URL,
                    TOKEN,
                    "15892733",
                    15892829,
                    "Groovy dev ${System.currentTimeMillis()}",
                    "Groovy lorem ...")

            println(createdPage)
        }
    }

    @Test
    void createPagesAsyncTest() {
        String spaceKey = "dev3"
        def parentPageId = 1572866

        for (i in 0..<20) {
            def createdPage = pageService.createPageAsync(
                    CONF_URL,
                    localTOKEN,
                    spaceKey,
                    parentPageId,
                    "Groovy dev ${System.currentTimeMillis()}",
                    "Groovy lorem ...")

            createdPage.thenAccept(res -> {
                println(">>> response: ${res.body()}")
            }).get()
        }
    }


}
