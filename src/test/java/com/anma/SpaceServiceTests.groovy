package com.anma

import com.anma.confl.models.Space
import com.anma.confl.services.PageService
import com.anma.confl.services.SpaceService
import com.anma.srv.TokenService
import org.junit.jupiter.api.Test

class SpaceServiceTests {

    def username = System.getenv("CONF_USER")
    def password = System.getenv("CONF_PASS")

    String CONF_URL = "http://localhost:8930"
    String localTOKEN = TokenService.getToken("admin", "admin")
    String TOKEN = TokenService.getToken(username, password)


    @Test
    void getSpace() {
        Space space = SpaceService.getSpace(CONF_URL, localTOKEN, "DEV")
        println(space)
    }

    @Test
    void createSpaceTest() {
        def createdSpace = SpaceService.createSpace(CONF_URL, localTOKEN, "dev2", "dev2")
        println(createdSpace)
    }
}
