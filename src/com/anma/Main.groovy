package com.anma


import com.anma.services.PageService

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

//=========== DU
//        def username = "Andrii.Maliuta";
//        final String CONF_URL = "https://confluence-datacenter.du.ae"
//===== BASS

//        def username = "beastiehut@gmail.com";
        def username = "admin";
        String password = 'admin'
//        final String CONF_URL = "https://bass.netcracker.com"
//        final String CONF_URL = "https://bassdevqa.netcracker.com"      // DEVQA
//        final String CONF_URL = "https://beastiehut.atlassian.net/wiki"      // Cloud
        final def CONF_URL = "http://localhost:7130"
        def id = 1179674

// ====== DATA
        def toFind = "lorem dolor"
        def toReplace = "REPLACED"

        // ************* Start ************

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))
//        println(">> Enter URL")
//        String CONF_URL = reader.readLine()
//        println(">> Enter username")
//        String username = reader.readLine()
//        println(">> Enter password")
//        String password = reader.readLine()
//        println(">> Enter page ID")
//        long id = reader.readLine().toInteger()

        def TOKEN = new String(Base64.encoder.encode("${username}:${password}".bytes))


        // ******** Operations **********

        // GET page
//        println(PageService.getPage(CONF_URL, TOKEN, id))

        // GET children
//        println(PageService.getChildren(CONF_URL, username, password, id).results)

        // GET descendants
//        println(PageService.getDescendants(CONF_URL, username, password, id).each {println(it.title)})

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

        // PUT children
//        PageService.getChildren(id).results.each {page -> PageService.updatePage(CONF_URL, "admin", "admin", page.id, toFind, toReplace)}

        // Add labels to page
//        PageService.addLabelsToPage(CONF_URL, username, password, id, ["added_1", "added_2"])

        // Rename Page
        // rest test - 1065015088
//        PageService.findReplacePageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", "")
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX.name())
//        PageService.addPageTitlePart(CONF_URL, username, password, id, "[PREFIX] ", TitlePosition.PREFIX)

        // rename pages
//        PageService.getChildren(CONF_URL, username, password, id).results.each {
//            PageService.findReplacePageTitlePart(CONF_URL, username, password, it.id, "JBTB8", "JBTB1")
//        }

        // replace PAGE INFO
//        PageService.replacePageInfoMacro(CONF_URL, username, password, id)

        // multiple page info
//        PageService.getDescendants(CONF_URL, username, password, id).results.each {
//            PageService.replacePageInfoMacro(CONF_URL, username, password, it.id)
//        }

// ========  Create comment
        for (i in 1..30) {
            String randomString = getRandomString(20)
            println(PageService.createComment(CONF_URL, TOKEN, 'DOCS', [], 1900545, 'page', randomString).body)
        }

// ======= Create PAGE
//        for (i in 1..200) {
//            def pageBody = getRandomString(200)
//            def title = "TEST Page RST New " + i
//            println(PageService.createPage(CONF_URL, TOKEN, 'DOCS', 1900545, title, pageBody).body)
//        }
    }



}
