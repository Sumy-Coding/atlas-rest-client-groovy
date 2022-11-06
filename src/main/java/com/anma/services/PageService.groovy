package com.anma.services

import com.anma.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mashape.unirest.http.HttpMethod
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.HttpRequest
import com.mashape.unirest.request.body.MultipartBody
import org.apache.commons.logging.Log
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path

class PageService {

    Gson gson = new GsonBuilder().setPrettyPrinting().create()
    final Logger LOGGER = LoggerFactory.getLogger(PageService.class)

    Content getPage(CONF_URL, TOKEN, id) {

        LOGGER.info("Getting page ${id}")
        def response = Unirest.get("${CONF_URL}/rest/api/content/${id}?expand=body.storage,version,space,ancestors")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Content.class)
    }

    public Contents getContent(String CONF_URL, String TOKEN, String type) {
        def response = Unirest.get("${CONF_URL}/rest/api/content?type=${type}")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()
        return gson.fromJson(response.body, Contents.class)
    }

    def getChildren(CONF_URL, TOKEN, id) {

        def response = Unirest.get("${CONF_URL}/rest/api/content/${id}/child/page?limit=300")       // limit = 300 pages
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    def pageContains(CONF_URL, TOKEN, id, toFind) {
        def page = getPage(CONF_URL, TOKEN, id)
        return page.body.storage.value.contains(toFind)

    }

    def getDescendants(CONF_URL, TOKEN, id) {
        /* ?cql=ancestor shows WRONG data - some pages are from other parent !! */
        /* Using https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content-search */

        def Url = "${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}&limit=300"
//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=ancestor+%3D+%226324225%22"


        def response = Unirest.get(Url)
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        Contents contents = gson.fromJson(response.body, Contents.class)

        return contents
    }

    def getSpacePages(CONF_URL, TOKEN, space) {
//        println(">>>>>>> Performing GET Pages request")
        LOGGER.info(">>>>>>> Performing GET Pages request")
        // todo GET /rest/api/space/{spaceKey}/content
        //http://localhost:7130/rest/api/content?type=page&spaceKey=TEST
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content?type=page&spaceKey=${space}&limit=300")      // limit = 300 pages
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        Contents contents = gson.fromJson(response.body, Contents.class)

        return contents
    }

    def getSpacePagesByLabel() {                                 // todo
        println(PageService.class.name + " :: " + ">> Performing GET Pages request")


    }

    def getSpaceBlogs(CONF_URL, TOKEN, space) {
        println(PageService.class.name + " :: " + ">> Performing GET Pages request")
        //http://localhost:7130/rest/api/content?type=blogpost&spaceKey=TEST
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content?type=blogpost&spaceKey=${space}&limit=300")  // limit = 300 blogs
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)

    }

    def getPageLabels(CONF_URL, TOKEN, id) {
        println(PageService.class.name + " :: " + ">>> Performing GET LABELS request")
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content/" + id + "/label")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Labels.class)
    }

    def deletePageLabels(CONF_URL, TOKEN, id, label) {
        println(">>>>>>> Performing DELETE LABELS request")
        // todo DELETE /rest/api/content/{id}/label/...
        HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}/label/${label}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    def deletePage(CONF_URL, TOKEN, id) {
        println(">>>>>>> Performing DELETE PAGE request")
        HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return response.body
    }

    def getScrollTemplates(CONF_URL, TOKEN, spaceKey) {
        println(">>>>>>> Performing GET Scroll templates request")
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/plugins/servlet/scroll-office/api/templates?spaceKey=${spaceKey}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    def createSpace(CONF_URL, TOKEN, space) {
        Space newSpace = new Space()            // todo

        Unirest.post("${CONF_URL}/rest/api/space")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(newSpace))
                .asString()
    }

    def createPage(CONF_URL, TOKEN, space, parentId, title, body) {
        println(">>>>>>> Performing CREATE PAGE request")
        Unirest.setTimeouts(0, 0);
        def headers = Map.of("Content-Type", "application/json", "Authorization", "Basic ${TOKEN}")

        def content = new Content()
        content.title = title
        content.type = 'page'
        content.status = 'current'
        Space space1 = new Space()
        content.space = space1
        space1.key = space
//        Container container = new Container()   // Can be skipped
//        container.type = 'page'
//        container.id = pare
//        content.container = container
        Ancestor ancestor = new Ancestor()
        Body body1 = new Body()
        Storage storage = new Storage()
        body1.storage = storage
        storage.representation = 'storage'
        storage.value = body
        content.body = body1
//        Version version = new Version()
//        version.message = 'test'
//        version.number = 1
//        content.version = version
        ancestor.id = parentId.toString()
        Ancestor[] ancestors = [ancestor]
        content.ancestors = ancestors
//        println(gson.toJson(content))

        return Unirest.post("${CONF_URL}/rest/api/content")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(content))
                .asString()
    }

    def createComment(CONF_URL, TOKEN, space, ancestorsIds, containerId, containerType, body) {
        println(">>>>>>>>> Performing CREATE COMMENT request")
        Unirest.setTimeouts(0, 0);
        def headers = Map.of("Content-Type", "application/json", "Authorization", "Basic ${TOKEN}")
        def content = new Content()
        content.title = 'comment'
        content.type = 'comment'
        content.status = 'current'
        Space space1 = new Space()
        content.space = space1
        space1.key = space
        Container container = new Container()
        container.type = containerType
        container.id = containerId
        Body body1 = new Body()
        Storage storage = new Storage()
        body1.storage = storage
        storage.representation = 'storage'
        storage.value = body
        content.body = body1
        content.container = container
//        println(gson.toJson(content))

        return Unirest.post("${CONF_URL}/rest/api/content")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(content))
                .asString()
    }

    def createBlog(CONF_URL, TOKEN, space, postingDay, title, body) {
        // postingDay	string
        //the posting day of the blog post. Required for blogpost type. Format: yyyy-mm-dd. Example: 2013-02-13
        println(PageService.class.name + " :: >> Performing CREATE Blogpost request")

        Unirest.setTimeouts(0, 0);
//        def headers = Map.of("Content-Type", "application/json", "Authorization", "Basic ${TOKEN}")
        def content = new Content()
        content.title = title
        content.type = 'blogpost'
        content.status = 'current'
        Space space1 = new Space()
        content.space = space1
        space1.key = space
        Body body1 = new Body()
        Storage storage = new Storage()
        body1.storage = storage
        storage.representation = 'storage'
        storage.value = body
        content.body = body1
//        println(gson.toJson(content))

        return Unirest.post("${CONF_URL}/rest/api/content")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(content))
                .asString()
    }

    def updateContentOnPage(confURL, TOKEN, id, toFind, toReplace) {

        def pageVersion = getPage(confURL, TOKEN, id).version.number
        def title = getPage(confURL, TOKEN, id).title
        String body = getPage(confURL, TOKEN, id).body.storage.value

        String newBody = body.replace(toFind, toReplace);

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
//        version.message = "changed with REST"
        version.message = ""
        updatedPage.version = version
        updatedPage.title = title
        updatedPage.type = "page"
        Body updBody = new Body()
        Storage storage = new Storage()
        storage.value = newBody
        storage.representation = "storage"
        updBody.storage = storage
        updatedPage.body = updBody
//        println(updatedPage)

        String pageJSON = gson.toJson(updatedPage)  // convert to JSON

/*        String updatedPageBody = "{\n" +
                "    \"version\": {\n" +
                "        \"number\": " + (pageVersion + 1) + "\n" +
                "    },\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"type\": \"page\",\n" +
                "    \"body\": {\n" +
                "        \"storage\": {\n" +
                "            \"value\": \"" + newBody + "\",\n" +
                "            \"representation\": \"storage\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
*/

        def url = "${confURL}/rest/api/content/${id}"
        def response = Unirest.put(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString()


        return response.body

    }

    def replacePageInfoMacro(CONF_URL, TOKEN, id) {

        def pageVersion = getPage(CONF_URL, TOKEN, id).version.number
        def title = getPage(CONF_URL, TOKEN, id).title
        String body = getPage(CONF_URL, TOKEN, id).body.storage.value
        String macroString = "";
        if (body.contains("<ac:structured-macro ac:name=\"page-info\"")) {
            try {
                macroString = body.substring(body.indexOf("<ac:structured-macro ac:name=\"page-info\""), body.indexOf("tinyurl</ac:parameter></ac:structured-macro>") + 44)
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

        String newBody = body.replace(macroString, title);

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
        version.message = "changed page info macro"
        updatedPage.version = version
        updatedPage.title = title
        updatedPage.type = "page"
        Body updBody = new Body()
        Storage storage = new Storage()
        storage.value = newBody
        storage.representation = "storage"
        updBody.storage = storage
        updatedPage.body = updBody

        String pageJSON = gson.toJson(updatedPage)  // convert to JSON
        println(pageJSON)

        /* Performing the PUT request to replace body */

        def url = "${CONF_URL}/rest/api/content/${id}"
        def response = Unirest.put(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString()

        return response.body
    }

    def getSpacePagesByLabel(CONF_URL, TOKEN, spaceKey, label) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=space+%3D+%22TEST%22+and+label+%3D+%22test%22"
        def url = "${CONF_URL}/rest/api/content/search?cql=space+%3D+${spaceKey}+and+label+%3D+${label}&limit=100"
        return Unirest.get(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString().body

    }

    def getDescendantsWithLabel(CONF_URL, TOKEN, id, label) {

//        def urlRequst = cql=ancestor+%3D+"6324225"+and+label+%3D+"test"

        def url = "${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}+and+label+%3D+${label}&limit=100"
        return Unirest.get(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString().body

    }

    /*
        POST /rest/api/content/{id}/label
        https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content/{id}/label-deleteLabel
     */

    def addLabelsToPage(CONF_URL, TOKEN, id, List<String> labels) {  // todo - add Label obj?

        def labelsArray = []

        labels.each { label ->
            Label label1 = new Label()
            label1.prefix = "global"
            label1.setName(label)
//            String labelJson = gson.toJson(label1, Label.class)
            labelsArray.add(label1)
        }

//        JsonArray jsonArray = new JsonArray()
//        jsonArray.add()
        def labelsArrayJSON = gson.toJson(labelsArray, ArrayList.class)
//        println(labelsArrayJSON)

        return Unirest.post("${CONF_URL}/rest/api/content/${id}/label")
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .body(labelsArrayJSON)
                .asString().body

    }

    def addLabelsToAncestors(CONF_URL, TOKEN, id, List<String> labels) {

        def page = getPage(CONF_URL, TOKEN, id)
        addLabelsToPage(CONF_URL, TOKEN, page.id, labels)
        Content nextParent
        def firstParent = getPage(CONF_URL, TOKEN, page.ancestors[page.ancestors.length - 1].id)
        if (null != firstParent) {
            addLabelsToPage(CONF_URL, TOKEN, firstParent.id, labels)
            nextParent = getPage(CONF_URL, TOKEN, firstParent.ancestors[firstParent.ancestors.length - 1].id)
            while (null != nextParent) {
                addLabelsToPage(CONF_URL, TOKEN, nextParent.id, labels)
                def length = nextParent.ancestors.length
                if (length > 0) {
                    nextParent = getPage(CONF_URL, TOKEN, nextParent.ancestors[length - 1].id)
                }
            }
        }
    }

    def addPageTitlePart(CONF_URL, TOKEN, id, toAdd, position) {

        def page = getPage(CONF_URL, TOKEN, id)
        def pageVersion = page.version.number
        String title = page.title
        def newTitle = ""
        if (position.equals(TitlePosition.POSTFIX.name())) {
            newTitle = title.concat(toAdd)
        } else if (position.equals(TitlePosition.PREFIX.name())) {
            newTitle = toAdd + title
        } else {
            newTitle = title
        }

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
        version.message = "changed with REST"
        updatedPage.version = version
        updatedPage.title = newTitle        // updated the title
        updatedPage.type = "page"

        String pageJSON = gson.toJson(updatedPage)  // convert to JSON
        println(pageJSON)

        def url = "${CONF_URL}/rest/api/content/${id}"
        return Unirest.put(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString().body


    }

    def findReplacePageTitlePart(CONF_URL, TOKEN, id, find, replace) {

        def page = getPage(CONF_URL, TOKEN, id)

        def pageVersion = page.version.number
        String title = page.title
        def newTitle = title.replace(find, replace)

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
        version.message = "changed with REST"
        updatedPage.version = version
        updatedPage.title = newTitle        // updated the title
        updatedPage.type = "page"

        String pageJSON = gson.toJson(updatedPage)  // convert to JSON
        println(pageJSON)

/*        String updatedPageBody = "{\n" +
                "    \"version\": {\n" +
                "        \"number\": " + (pageVersion + 1) + "\n" +
                "    },\n" +
                "    \"title\": \"" + title + "\",\n" +
                "    \"type\": \"page\"\n" +
                "}";
*/
        def url = "${CONF_URL}/rest/api/content/${id}"
        return Unirest.put(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString().body

    }

    def movePage(CONF_URL, TOKEN, pageId, targetParentId) {

        Content content = getPage(CONF_URL, TOKEN, pageId)
        Ancestor ancestor = new Ancestor()
        ancestor.id = targetParentId
        content.ancestors = [ancestor]
        content.version.number += 1

        println(gson.toJson(content))

        return Unirest.put("${CONF_URL}/rest/api/content/" + pageId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(content))
                .asString()
                .body
    }

    def copyPage(CONF_URL, TOKEN, sourceId, targetId, newTitle, boolean copyLabels, boolean copyAttach,
                 boolean copyComments, tgtServer, extTOKEN) {
        Content rootPage
        Content targetPage
//        def extTOKEN = TokenService.getToken(targetUser, targetPass)
        Content createdPage

        if (!tgtServer.isEmpty()) {
            rootPage = getPage(CONF_URL, TOKEN, sourceId)
            targetPage = getPage(tgtServer, extTOKEN, targetId)
            if (null == newTitle || newTitle.isEmpty()) {
                newTitle = "Copy of " + rootPage.title
            }
            def respBody = createPage(tgtServer, extTOKEN, targetPage.space.key, targetId, newTitle, rootPage.body.storage.value).body
            createdPage = gson.fromJson(respBody, Content.class)
            if (copyLabels) {           // better change just TOKEN based on conditions as all the rest is same
                copyPageLabels(CONF_URL, TOKEN, rootPage.id, createdPage.id, tgtServer, extTOKEN)
            }
            if (copyAttach) {
                copyPageAttaches(CONF_URL, TOKEN, rootPage.id, createdPage.id, tgtServer, extTOKEN)
            }
            if (copyComments) {
                copyPageComments(CONF_URL, TOKEN, rootPage.id, createdPage.id, tgtServer, extTOKEN)
            }
        } else {
            rootPage = getPage(CONF_URL, TOKEN, sourceId)
            targetPage = getPage(CONF_URL, TOKEN, targetId)
            if (null == newTitle || newTitle.isEmpty()) {
                newTitle = "Copy of " + rootPage.title
            }
            def body = createPage(CONF_URL, TOKEN, targetPage.space.key, targetId, newTitle, rootPage.body.storage.value).body
            createdPage = gson.fromJson(body, Content.class)
            if (copyLabels) {
                copyPageLabels(CONF_URL, TOKEN, rootPage.id, createdPage.id, tgtServer, extTOKEN)
            }

            if (copyAttach) {
                copyPageAttaches(CONF_URL, TOKEN, rootPage.id, createdPage.id, tgtServer, extTOKEN)
            }

            if (copyComments) {
                copyPageComments(CONF_URL, TOKEN, rootPage.id, createdPage.id)
            }
        }

        // copy root
//        def body = createPage(CONF_URL, TOKEN, targetPage.space.key, targetId, newTitle, rootPage.body.storage.value).body
//        def createdPage = gson.fromJson(body, Content.class)
        // copy lables


        return createdPage
    }

    public void copyPageLabels(CONF_URL, TOKEN, sourcePageId, targetPageId, tgtURL, tgtToken) {
        if (null != tgtURL && null != tgtToken) {
            def labels = getPageLabels(CONF_URL, TOKEN, sourcePageId).results
            if (labels != null) {
                if (labels != null || labels.length > 0) {
                    labels.each {
                        addLabelsToPage(tgtURL, tgtToken, targetPageId, [it.name])    // todo - not good
                        println(">> Labels added to page " + targetPageId)
                    }
                }
            }
        } else {
            def labels = getPageLabels(CONF_URL, TOKEN, sourcePageId).results
            if (labels != null) {
                if (labels != null || labels.length > 0) {
                    labels.each {
                        addLabelsToPage(CONF_URL, TOKEN, targetPageId, [it.name])    // todo - not good
                        println(">> Labels added to page " + targetPageId)
                    }
                }
            }
        }

    }

    /*
     def copyPagesBranch(CONF_URL, TOKEN, parentId, targetId, newTitle,
                               copyLabels, copyAttach, boolean copyComments) {
        println(">>>>> Performing COPY page BRANCH  request")

        Content rootPage = getPage(CONF_URL, TOKEN, parentId)
        Content targetPage = getPage(CONF_URL, TOKEN, targetId)
        Content[] children
        def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels, copyAttach, copyComments)
        children = getChildren(CONF_URL, TOKEN, rootPage.id).results
        int length = children.length
        while (length > 0) {
            children.each {child ->
                def childCopies = copyChildren(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments)
                while (getChildren(CONF_URL, TOKEN, child.id).results > 0) {
                    def descCopies = copyChildren(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels, copyAttach, copyComments)
                }
//                children =
//                        length = children.length
            }
        }
    }
     */

    // used for copying DESCENDANTS
    def copyChildren(CONF_URL, TOKEN, sourceId, targetId, newTitle,
                     boolean copyLabels, boolean copyAttach, boolean copyComments,
                     String targetServer, extTOKEN) {

        LOGGER.info("Copying descendants of ${sourceId} to ${targetId}")
        Content rootPage
        Content targetPage
        Content[] children
        if (!targetServer.isEmpty()) {
//            extTOKEN = TokenService.getToken(targetUser, targetPass)
            rootPage = getPage(CONF_URL, TOKEN, sourceId)   // can be same for both
            targetPage = getPage(targetServer, extTOKEN, targetId)
            children = getChildren(CONF_URL, TOKEN, rootPage.id).results
            def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels,
                    copyAttach, copyComments, targetServer, extTOKEN)
            if (children != null) {
                for (i in 0..<children.length) {
                    def child = children[i]
                    copyChildren(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments,
                            targetServer, extTOKEN)
                    copyPage(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments,
                            targetServer, extTOKEN)
                }
            }
        } else {
            rootPage = getPage(CONF_URL, TOKEN, sourceId)   // can be same for both
            targetPage = getPage(CONF_URL, TOKEN, targetId)
            children = getChildren(CONF_URL, TOKEN, rootPage.id).results
            def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels,
                    copyAttach, copyComments, targetServer, extTOKEN)
            if (children != null) {
                for (i in 0..<children.length) {
                    def child = children[i]
                    copyChildren(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments,
                            targetServer, extTOKEN)
                    copyPage(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments,
                            targetServer, extTOKEN)
                }
            }
        }


//        def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels, copyAttach, copyComments)
//        println(PageService.class.name + CodeLines.lineNumber + " :: " + " Root page copied ${rootCopy}")


//        def copies = []
//        while (children != null) {
//            children.each { child ->
//            def childCopy = copyPage(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments)
//            def descs = getChildren(CONF_URL, TOKEN, child.id)
//            def childLength = descs.results
//            while (childLength > 0) {
//                descs.each {desc ->
//                    def copy = copyPage(CONF_URL, TOKEN, desc.id, childCopy.id, newTitle, copyLabels, copyAttach, copyComments)
//                    childLength = child
//                }
//            }
//            println(PageService.class.name + " :: " + " Child ${child} copied")
        //println(getClass().name + " :: " + " Root page copied ${childCopy}")
//            copies.add(childCopy)

        return Optional<Map>.empty()
    }

    def getPageRestrictions() {
        // todo - GET /rest/api/content/{id}/restriction/byOperation

    }

    def getPageAttachment(CONF_URL, TOKEN, attachId) {

        def response = Unirest.get("${CONF_URL}/rest/api/content/" + attachId)
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Content.class)
    }

    def getPageAttachments(CONF_URL, TOKEN, pageId) {
        //  GET /rest/api/content/{id}/child/attachment
        def response = Unirest.get("${CONF_URL}/rest/api/content/" + pageId + "/child/attachment")
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Contents.class)
    }

    //todo - pass Content as param and get attach as Content in getPageAttachments()
    def addAttachToPage(CONF_URL, TOKEN, attachId, targetPageId, tgtURL, tgtTOKEN) {
        //https://community.atlassian.com/t5/Jira-questions/Upload-Attach-API-Token-Java/qaq-p/970011

        println("Getting ${attachId} attach")
        def attach = getPageAttachment(CONF_URL, TOKEN, attachId)
        URL fileURL = new URL(attach._links.base + attach._links.download)
        def savedAttach = "src/main/resources/" + attach.title

        def stream = Unirest.get(fileURL.toURI().toString())
                .header("Authorization", "Basic ${TOKEN}")
                .asBinary().body

        ReadableByteChannel readableByteChannel = Channels.newChannel(stream);
        def fileOutputStream = new FileOutputStream(savedAttach);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        println("Created ByteChannel")
//        InputStream file = new FileInputStream(new File("res.txt"));

        File fileUpload = new File(savedAttach);

        String url = "${tgtURL}/rest/api/content/" + targetPageId + "/child/attachment";   // ext
        HttpRequest request = new HttpRequest(HttpMethod.POST, url);
        MultipartBody multipartBody = new MultipartBody(request);
        multipartBody.field("upload", fileUpload);
        println("Created POST method for adding ${attachId} attachment to ${targetPageId} page")

        return Unirest.post("${tgtURL}/rest/api/content/${targetPageId}/child/attachment")  // ext
                .header("Authorization", "Basic ${tgtTOKEN}")
                .header("X-Atlassian-Token", "nocheck")
//                .field("upload", new File("copy.edn"))
                .field("file", fileUpload)
                .asString().body

        // == Apache
//        HttpClient httpClient = HttpClientBuilder.create().build();
//        HttpPost postRequest = new HttpPost(url);
//        postRequest.setHeader("Authorization", "Basic " + TOKEN);
//        postRequest.setHeader("X-Atlassian-Token","nocheck");
//        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
//        entity.addPart("file", new FileBody(fileUpload));
//        postRequest.setEntity( entity.build());
////        HttpResponse response = httpClient.execute(postRequest);
//        return httpClient.execute(postRequest).asType(String.class)

    }

    // add existing comment to page
    def addCommentToPage(CONF_URL, TOKEN, commId, tgtPageId, tgtURL, tgtTOKEN) {

        println("Adding comment ${commId} to page ${tgtPageId}")

        Content comment = getPageComment(CONF_URL, TOKEN, commId)

        Content newComment = new Content()
        newComment.title = comment.title
        newComment.type = 'comment'
        newComment.status = 'current'
//        Container container = new Container()   // Can be skipped
//        container.type = 'page'
//        container.id = pare
//        content.container = container
//        Ancestor ancestor = new Ancestor()
        Body commBody = new Body()
        Storage storage = new Storage()
        commBody.storage = storage
        storage.representation = 'storage'
        storage.value = comment.body.storage.value
        newComment.body = commBody
//        Container container = new Container()
        newComment.container = getPage(CONF_URL, TOKEN, tgtPageId)
//        Version version = new Version()
//        version.message = 'test'
//        version.number = 1
//        content.version = version
//        ancestor.id = parentId.toString()
//        Ancestor[] ancestors = [ancestor]
//        content.ancestors = ancestors

        def commJson = gson.toJson(newComment)

        return Unirest.post("${tgtURL}/rest/api/content")  // ext
                .header("Authorization", "Basic ${tgtTOKEN}")
                .body(commJson)
                .asString()

    }

    def getPageComment(CONF_URL, TOKEN, commId) {

        def expand = "body.storage,version"
        def response = Unirest.get("${CONF_URL}/rest/api/content/${commId}?expand=${expand}")
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Content.class)
    }

    def getPageComments(CONF_URL, TOKEN, sourceId) {
        //  GET /rest/api/content/{id}/child/comment
//        Content rootPage = getPage(CONF_URL, TOKEN, sourceId)
        def response = Unirest.get("${CONF_URL}/rest/api/content/" + sourceId + "/child/comment")
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Contents.class)
    }

    @Deprecated
    def copyPageComments(CONF_URL, TOKEN, sourceId, targetId, tgtURL, tgtTOKEN) {

//        Content rootPage = getPage(CONF_URL, TOKEN, sourceId)
//        Content targetPage = getPage(CONF_URL, TOKEN, targetId)
        Content[] comments = getPageComments(CONF_URL, TOKEN, sourceId).results

        if (comments.length > 0) {
            try {
                comments.each { comm ->
                    addCommentToPage(CONF_URL, TOKEN, comm.id, targetId, tgtURL, tgtTOKEN)  // add comments
                    LOGGER.info("Added comment ${comm.id} to ${targetId} page")
                }
            } catch (Exception e) {
                e.printStackTrace()
            }
        } else {
            LOGGER.info("${sourceId} page has 0 comments")
        }

    }


    def copyPageAttaches(CONF_URL, TOKEN, sourcePageId, targetPageId, tgtURL, tgtTOKEN) {
        if (null != tgtURL && null != tgtTOKEN) {
            println(" === copying ${sourcePageId} page attaches to ${targetPageId}")
//            def page = getPage(CONF_URL, TOKEN, sourcePageId)
            def attachments = getPageAttachments(CONF_URL, TOKEN, sourcePageId).results

            if (attachments.length > 0) {
                try {
                    attachments.each { attach ->
                        addAttachToPage(CONF_URL, TOKEN, attach.id, targetPageId, tgtURL, tgtTOKEN)  // add
                        try {
                            println("Deleting files from /SRC")
                            Files.delete(Path.of("src/main/resources/" + attach.title))     // delete after copy
                        } catch (Exception e) {
                            e.printStackTrace()
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace()
                }
            } else {
                LOGGER.info("${sourcePageId} page has 0 attaches")
            }
        }


    }
}
