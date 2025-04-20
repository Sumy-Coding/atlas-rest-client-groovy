package com.anma

import com.anma.confl.models.Contents
import com.anma.confl.models.Space
import com.anma.confl.services.CommentService
import com.anma.confl.services.PageService
import com.anma.confl.services.SpaceService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

class CommentServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")

    String SPACE_KEY = "DEV"
    String PAGE_ID = "163934"

    String CONF_URL = "http://localhost:9003"
    String localTOKEN = TokenService.getToken("admin", "admin")
    String TOKEN = TokenService.getToken(username, password)

    PageService pageService = new PageService()
    CommentService commentService = new CommentService()

    @Test
    void getPageCommentsTest() {
        def comments = commentService.getPageComments(CONF_URL, localTOKEN, 1572866)

        comments.results.each {
            println(it)
        }

    }

    @Test
    void addCommentToPageTest() {
        commentService.addFooterCommentToPage(CONF_URL, localTOKEN, PAGE_ID, "aaaa")
    }

    @Test
    void addNCommentToPageTest() {
        for (i in 0..<20) {
            commentService.addFooterCommentToPage(CONF_URL, localTOKEN, PAGE_ID, "lorem ipsum dolor lorem...")
        }
    }

    @Test
    void addNCommentToPageDescendentsTest() {
//        def root = pageService.getPage(CONF_URL, localTOKEN, PAGE_ID)

        pageService.getDescendants(CONF_URL, localTOKEN, PAGE_ID).results.each {page ->
            commentService.addFooterCommentToPage(CONF_URL, localTOKEN, page.id, "lorem ipsum dolor lorem...")
        }

    }

    @Test
    void addCommentsToSpacePagesTest() {
        String parentPageId = "1572866"
//        def rootPage = pageService.getPage(CONF_URL, localTOKEN, parentPageId)
        pageService.getChildren(CONF_URL, localTOKEN, PAGE_ID).results.each {page ->
            commentService.addFooterCommentToPage(CONF_URL, localTOKEN, page.id, "aaaa")
        }
    }
}
