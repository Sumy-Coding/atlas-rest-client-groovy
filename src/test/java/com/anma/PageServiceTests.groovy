package com.anma

import com.anma.confl.models.Content
import com.anma.confl.services.PageService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

import java.util.concurrent.CompletableFuture

class PageServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")

    String CONF_URL = System.getenv("CONF_URL") != null ? System.getenv("CONF_URL") : "http://localhost:8930"
    String localTOKEN = TokenService.getToken("admin", "admin")
    String TOKEN = TokenService.getToken(username, password)

    PageService pageService = new PageService()

    @Test
    void getPage() {
        PageService pageService = new PageService()

        def page = pageService.getPage(CONF_URL, localTOKEN, "983046")

        println(page)
    }

    @Test
    void getChildren() {
        def contents = pageService.getChildren(CONF_URL, TOKEN, "983046").results

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
