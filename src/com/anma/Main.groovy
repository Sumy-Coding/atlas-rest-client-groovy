package com.anma

import com.anma.models.Space
import com.anma.services.PageService
import com.anma.services.SpaceService

import java.time.Duration
import java.time.LocalTime
import java.time.Period
import java.time.temporal.ChronoUnit
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjusters
import java.time.temporal.TemporalUnit

class Main {

    private static String getRandomString(length) {
        Random random = new Random()
        def lorem = 'Lorem ipsum dolor sit amet consectetur adipiscing elit maecenas interdum pellentesque, ' +
                'sollicitudin hendrerit cubilia primis nisl feugiat placerat tellus mauris'
        def loremArr = lorem.split(' ')
        def randomString = ''
        for (i in 0..<length) {
            randomString = randomString.concat(loremArr[random.nextInt(loremArr.length - 1)]).concat(' ')
        }
        return randomString
    }

    static void main(String[] args) {

// ====== DATA
        //=========== DU

//        def username = "Andrii.Maliuta";
//        final String CONF_URL = "https://confluence-datacenter.du.ae"
        //========== bh
//        def username = "beastiehut@gmail.com";
//        String password = 'RtuJhOJZADwGBrJzofpBFCB8'
//        def username = "andymaliuta@gmail.com";
//        String password = 'VxHRL3rp319SgvhQYpcp0CBA'
// DC AWS
        def username = 'admin'
        def password = 'admin'

//===== BASS
//        final String CONF_URL = "https://bass.netcracker.com"
//        final String CONF_URL = "https://bassdevqa.netcracker.com"      // DEVQA
//        final String CONF_URL = "https://beastiehut.atlassian.net/wiki"      // bh Cloud
//        final String CONF_URL = "https://anma.atlassian.net/wiki"      // anma Cloud
        final def CONF_URL = "http://localhost:7141"                    // localhost
//        final def CONF_URL = "http://confl-loadb-1mob5tjjndhrr-969460925.us-west-2.elb.amazonaws.com"       // AWS DC
        def pageId = 65611

        // ************* Start ************

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
        def TOKEN = new String(Base64.encoder.encode("${username}:${password}".bytes))
        def key = "df1"
        def start = System.currentTimeMillis()
//        Space space = SpaceService.getSpace(CONF_URL, TOKEN, key)

        // ******** Operations **********

        // GET page
//        println(PageService.getPage(CONF_URL, TOKEN, 65603))

        // GET children
//        println(PageService.getChildren(CONF_URL, username, password, id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, username, password, id).each {println(it.title)})

        // GET space pages
//        println(PageService.getSpacePages(CONF_URL, TOKEN, 'TEST').results)

        // GET blogs
//        println(PageService.getSpaceBlogs(CONF_URL, TOKEN, 'TEST').results)

        // GET space pages by label
//        println(PageService.getSpacePagesByLabel(CONF_URL, "admin", "admin", "TEST", "test").each {println(it.title)})

        // GET descendants by label
//        println(PageService.getDescendantsWithLabel(CONF_URL, "admin", "admin", id, "test").each {println(it.title)})

        // PUT -> update page
//        println(PageService.updatePage(CONF_URL, "admin", "admin",6324225, toFind, toReplace))

        // PUT -> update pages with specific label
//        PageService.getDescendantsWithLabel(CONF_URL, username, password, id, "test").each {
//            println(PageService.updatePage(CONF_URL, username, password, it.id, toFind, toReplace))
//        }

        // ==== PUT children

//        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // ==== Add labels to page

//        PageService.addLabelsToPage(CONF_URL, username, password, id, ["added_1", "added_2"])

        // ==== Rename Page

        // rest test - 1065015088
//        PageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

        // ==== Rename pages

//        PageService.getChildren(CONF_URL, username, password, id).results.each {
//            PageService.findReplacePageTitlePart(CONF_URL, username, password, it.id, "JBTB8", "JBTB1")
//        }

        // ==== replace PAGE INFO

//        PageService.replacePageInfoMacro(CONF_URL, username, password, id)

        // ==== multiple page info

//        PageService.getDescendants(CONF_URL, username, password, id).results.each {
//            PageService.replacePageInfoMacro(CONF_URL, username, password, it.id)
//        }

// ========  Create comment

//        for (i in 1..20) {
//            String randomString = getRandomString(20)
//            println(PageService.createComment(CONF_URL, TOKEN, space.key, [], space.homepage.id, 'page', randomString).body)
//        }

        // == Create comments for children

//        PageService.getChildren(CONF_URL, TOKEN, space.homepage.id).results.each {
//            for (i in 1..14) {
//                String randomString = getRandomString(18)
//                println(PageService.createComment(CONF_URL, TOKEN, space.key, [], it.id, 'page', randomString).body)
//            }
//        }

        // ======== create pages for Spaces

        for (i in 1..<80) {
            Space space = SpaceService.getSpace(CONF_URL, TOKEN, "dev" + i)
            def homePage = PageService.getPage(CONF_URL, TOKEN, space.homepage.id)
            for (a in 1..200) {
                def pageBody = getRandomString(100)
                def title = "${space.key} Page New " + a
                println(PageService.createPage(CONF_URL, TOKEN, space.key, homePage.id, title, pageBody).body)
            }
        }

// ======= Create PAGE

//        for (i in 1..200) {
//            def pageBody = getRandomString(100)
//            def title = "${space.key} Page New " + i
//            println(PageService.createPage(CONF_URL, TOKEN, key, space.homepage.id, title, pageBody).body)
//        }

        // ========== Create Spaces

//        for (i in 2..<100) {
//            println(SpaceService.createSpace(CONF_URL, TOKEN, "dev${i}", "dev${i}"))
//        }

        // ========== Create spaces

//        for (i in 1..<30) {
//            println(SpaceService.createSpace(CONF_URL, TOKEN, key + i, key + i))
//        }

        def takenMillis = System.currentTimeMillis() - start
        long seconds = Duration.of(takenMillis, ChronoUnit.MILLIS).seconds
        println(">>>>> Script took ${seconds} seconds")

    }

}
