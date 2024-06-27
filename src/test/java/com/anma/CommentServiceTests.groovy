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

    String CONF_URL = "http://localhost:8930"
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
        commentService.addCommentToPage(CONF_URL, localTOKEN, 1572866)
    }

    @Test
    void addCommentsToSpacePagesTest() {
        String parentPageId = "1572866"
//        def rootPage = pageService.getPage(CONF_URL, localTOKEN, parentPageId)
        pageService.getChildren(CONF_URL, localTOKEN, parentPageId).each {page ->
            commentService.addFooterCommentToPage()
        }
    }
}
