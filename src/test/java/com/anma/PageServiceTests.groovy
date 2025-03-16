package com.anma

import com.anma.confl.models.Content
import com.anma.confl.services.PageService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

import java.util.concurrent.CompletableFuture

class PageServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")

    String CONF_URL = System.getenv("CONF_URL") != null ? System.getenv("CONF_URL") : "http://localhost:9002"
    String localTOKEN = TokenService.getToken("admin", "admin")
    String TOKEN = TokenService.getToken(username, password)

    String SPACE_KEY = "DEV"
    String PAGE_ID = "163926"

    PageService pageService = new PageService()

    @Test
    void getPage() {
        PageService pageService = new PageService()

        def page = pageService.getPage(CONF_URL, localTOKEN, "163926")

        println(page)
    }

    @Test
    void getChildren() {
        def contents = pageService.getChildren(CONF_URL, TOKEN, "163926").results

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
                "DEV",
                163926,
                "Groovy dev ${System.currentTimeMillis()}", "Groovy lorem ...")

        println(createdPage)
    }

    @Test
    void createPageAsyncTest() {
        String spaceKey = "DEV"
        def parentPageId = 163926

        def createdPage = pageService.createPageAsync(
                CONF_URL,
                localTOKEN,
                SPACE_KEY,
                PAGE_ID,
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
                    SPACE_KEY,
                    PAGE_ID,
                    "Groovy dev ${System.currentTimeMillis()}",
                    "Groovy lorem ...")

            println(createdPage)
        }
    }

    @Test
    void createPagesAsyncTest() {
        String spaceKey = "KB1"
        def parentPageId = 1277999

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
