package com.anma

import com.anma.confl.models.Content
import com.anma.confl.services.PageService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

class PageServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")

    String CONF_URL = System.getenv("CONF_URL")
    String localTOKEN = TokenService.getToken("admin", "admin")
    String TOKEN = TokenService.getToken(username, password)

    PageService pageService = new PageService()

    @Test
    void getPage() {
        PageService pageService = new PageService()

        def page = pageService.getPage(CONF_URL, localTOKEN, "2752571")

        println(page)
    }

    @Test
    void getChildren() {
        def contents = pageService.getChildren(CONF_URL, TOKEN, "2752538").results

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
}
