package com.anma.services

import com.anma.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mashape.unirest.http.HttpMethod
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.request.HttpRequest
import com.mashape.unirest.request.body.MultipartBody
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.file.Files
import java.nio.file.Path

class PageService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static Content getPage(CONF_URL, TOKEN, id) {

        def response = Unirest.get("${CONF_URL}/rest/api/content/${id}?expand=body.storage,version,space")
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Content.class)
    }

    static def getChildren(CONF_URL, TOKEN, id) {

        def response = Unirest.get("${CONF_URL}/rest/api/content/${id}/child/page?limit=300")       // limit = 300 pages
                .header("Authorization", "Basic ${TOKEN}")
                .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    static def pageContains(CONF_URL, TOKEN, id, toFind) {
        def page = getPage(CONF_URL, TOKEN, id)
        return page.body.storage.value.contains(toFind)

    }

    static def getDescendants(CONF_URL, TOKEN, id) {
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

    static def getSpacePages(CONF_URL, TOKEN, space) {
        println(">>>>>>> Performing GET Pages request")
        // todo GET /rest/api/space/{spaceKey}/content
        //http://localhost:7130/rest/api/content?type=page&spaceKey=TEST
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content?type=page&spaceKey=${space}&limit=300")      // limit = 300 pages
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        Contents contents = gson.fromJson(response.body, Contents.class)

        return contents
    }

    static def getSpacePagesByLabel() {                                 // todo
        println(PageService.class.name + " :: " + ">> Performing GET Pages request")


    }

    static def getSpaceBlogs(CONF_URL, TOKEN, space) {
        println(PageService.class.name + " :: " + ">> Performing GET Pages request")
        //http://localhost:7130/rest/api/content?type=blogpost&spaceKey=TEST
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content?type=blogpost&spaceKey=${space}&limit=300")  // limit = 300 blogs
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)

    }

    static def getPageLabels(CONF_URL, TOKEN, long id) {
        println(PageService.class.name + " :: " + ">>> Performing GET LABELS request")
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content/" + id + "/label")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Labels.class)
    }

    static def deletePageLabels(CONF_URL, TOKEN, id, label) {
        println(">>>>>>> Performing DELETE LABELS request")
        // todo DELETE /rest/api/content/{id}/label/...
        HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}/label/${label}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    static def getScrollTemplates(CONF_URL, TOKEN, spaceKey) {
        println(">>>>>>> Performing GET Scroll templates request")
        HttpResponse<String> response =
                Unirest.get("${CONF_URL}/plugins/servlet/scroll-office/api/templates?spaceKey=${spaceKey}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    static def createSpace(CONF_URL, TOKEN, space) {
        Space newSpace = new Space()            // todo

        Unirest.post("${CONF_URL}/rest/api/space")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(newSpace))
                .asString()
    }

    static def createPage(CONF_URL, TOKEN, space, parentId, title, body) {
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

    static def createComment(CONF_URL, TOKEN, space, ancestorsIds, containerId, containerType, body) {
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

    static def updateContentOnPage(confURL, TOKEN, id, toFind, toReplace) {

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

    static def replacePageInfoMacro(CONF_URL, TOKEN, id) {

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

    static def getSpacePagesByLabel(CONF_URL, TOKEN, spaceKey, label) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=space+%3D+%22TEST%22+and+label+%3D+%22test%22"
        def url = "${CONF_URL}/rest/api/content/search?cql=space+%3D+${spaceKey}+and+label+%3D+${label}&limit=100"
        return Unirest.get(url)
                .header("Authorization", "Basic ${TOKEN}")
                .header("Content-Type", "application/json")
                .asString().body

    }

    static def getDescendantsWithLabel(CONF_URL, TOKEN, id, label) {

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

    static def addLabelsToPage(CONF_URL, TOKEN, id, List<String> labels) {  // todo - add Label obj?

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

    static def addPageTitlePart(CONF_URL, TOKEN, id, toAdd, position) {

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

    static def findReplacePageTitlePart(CONF_URL, TOKEN, id, find, replace) {

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

    static def movePage(CONF_URL, TOKEN, pageId, targetParentId) {

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

    static def copyPage(CONF_URL, TOKEN, sourceId, long targetId, String newTitle,
                        boolean copyLabels, boolean copyAttach, boolean copyComments) {
        Content rootPage = getPage(CONF_URL, TOKEN, sourceId)
        Content targetPage = getPage(CONF_URL, TOKEN, targetId)
        if (null == newTitle || newTitle.isEmpty()) {
            newTitle = "Copy of " + rootPage.title
        }
        // copy root
        def body = createPage(CONF_URL, TOKEN, targetPage.space.key, targetId, newTitle, rootPage.body.storage.value).body
        def createdPage = gson.fromJson(body, Content.class)
        // copy lables
        if (copyLabels) {
            copyPageLabels(CONF_URL, TOKEN, rootPage.id, createdPage.id)
        }

        if (copyAttach) {
            copyPageAttaches(CONF_URL, TOKEN, rootPage.id, createdPage.id)
        }

        if (copyComments) {
            copyPageComments(CONF_URL, TOKEN, rootPage.id, createdPage.id)
        }

        return createdPage
    }

    public static void copyPageLabels(CONF_URL, TOKEN, sourcePageId, long targetPageId) {
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

    static def copyPagesBranch(CONF_URL, TOKEN, parentId, targetId, newTitle,
                               copyLabels, copyAttach, boolean copyComments) {
        println(">>>>>>> Performing COPY page BRANCH  request")

        Content rootPage = getPage(CONF_URL, TOKEN, parentId)
        Content targetPage = getPage(CONF_URL, TOKEN, targetId)
        Content[] children = getChildren(CONF_URL, TOKEN, rootPage.id).results
        int length = children.length

//        def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, "", copyLabels, copyAttach, copyComments)
//        println(">> Root page copied ${rootCopy}")

        if (length > 0) {
            while (length > 0) {
                def copies = copyChildren(CONF_URL, TOKEN, parentId, targetPage.id, newTitle, copyLabels, copyAttach, copyComments)
                println(PageService.class.name + " :: " + ">> Descendant page copied ${copies}")
                children.each { child ->
                    def descCopies = copyChildren(CONF_URL, TOKEN, child.id, copies.keySet()[0].id, newTitle, copyLabels, copyAttach, copyComments)
                    children = getChildren(CONF_URL, TOKEN, child.id).results
                    length = children.length
//                    descChilds.each { desc ->
//                        def descCopiesX2 = copyChildren(CONF_URL, TOKEN, desc.id, childCopy.id, "", copyLabels, copyAttach, copyComments)
//                        def descX3 = getChildren(CONF_URL, TOKEN, desc.id).results
//                        length = descX3.length
//                    }
                }
            }
        }

    }

    static def copyChildren(CONF_URL, TOKEN, sourceId, targetId, newTitle,
                            boolean copyLabels, boolean copyAttach, boolean copyComments) {
        Content rootPage = getPage(CONF_URL, TOKEN, sourceId)
        Content targetPage = getPage(CONF_URL, TOKEN, targetId)
        Content[] children = getChildren(CONF_URL, TOKEN, rootPage.id).results

        def rootCopy = copyPage(CONF_URL, TOKEN, rootPage.id, targetPage.id, newTitle, copyLabels, copyAttach, copyComments)
        println(getClass().name + " :: " + " Root page copied ${rootCopy}")

        def copies = []
        children.each { child ->
            def childCopy = copyPage(CONF_URL, TOKEN, child.id, rootCopy.id, newTitle, copyLabels, copyAttach, copyComments)
            println(getClass().name + " :: " + " Root page copied ${childCopy}")
            copies.add(childCopy)
//            def descendants = getChildren(CONF_URL, TOKEN, child.id).results   // descendants
//            int length = descendants.length
//
//            descendants.each { desc ->
//                copyChildren(CONF_URL, TOKEN, sourceId, targetId, newTitle,copyLabels,copyAttach,copyComments)
//                descendants = getChildren(CONF_URL, TOKEN, desc.id).results
//                length = descendants.length
//            }
        }
        return Map.of(rootCopy, copies)
    }

    static def getPageRestrictions() {
        // todo - GET /rest/api/content/{id}/restriction/byOperation

    }

    static def getPageAttachment(CONF_URL, TOKEN, attachId) {
        //  GET /rest/api/content/{id}/child/attachment
        def response = Unirest.get("${CONF_URL}/rest/api/content/" + attachId)
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Content.class)
    }

    static def getPageAttachments(CONF_URL, TOKEN, pageId) {
        //  GET /rest/api/content/{id}/child/attachment
        def response = Unirest.get("${CONF_URL}/rest/api/content/" + pageId + "/child/attachment")
                .header("Authorization", "Basic ${TOKEN}")
                .asString().body
        return gson.fromJson(response, Contents.class)
    }

    static def addAttachToPage(CONF_URL, TOKEN, attachId, targetPageId) {
        //https://community.atlassian.com/t5/Jira-questions/Upload-Attach-API-Token-Java/qaq-p/970011

        def attach = getPageAttachment(CONF_URL, TOKEN, attachId)
        URL fileURL = new URL(attach._links.base + attach._links.download)
        def savedAttach = "src/main/resources/" + attach.title

        def stream = Unirest.get(fileURL.toURI().toString())
                .header("Authorization", "Basic ${TOKEN}")
                .asBinary().body

        ReadableByteChannel readableByteChannel = Channels.newChannel(stream);
        FileOutputStream fileOutputStream = new FileOutputStream(savedAttach);
        FileChannel fileChannel = fileOutputStream.getChannel();
        fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

//        InputStream file = new FileInputStream(new File("res.txt"));

//        HttpClient client = HttpClient.newBuilder().build();
////        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody)
//        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
//        HttpRequest postReq = HttpRequest.newBuilder()
//                .uri(URI.create())
//                .PUT(publisher)
//                .headers("Authorization", "Basic ${TOKEN}")
//                .headers("Content-Type", "application/json")
//                .build();
//        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        File fileUpload = new File(savedAttach)

        def url = "${CONF_URL}/rest/api/content/" + targetPageId + "/child/attachment"
        HttpRequest request = new HttpRequest(HttpMethod.POST, url)
        MultipartBody multipartBody = new MultipartBody(request)
        multipartBody.field("upload", fileUpload)

        return Unirest.post("${CONF_URL}/rest/api/content/${targetPageId}/child/attachment")
                .header("Authorization", "Basic ${TOKEN}")
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

    static def copyPageComments(CONF_URL, TOKEN, sourcePageId, targetPageId) {

        //todo

    }

    static def copyPageAttaches(CONF_URL, TOKEN, sourcePageId, targetPageId) {
        //https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content/{id}/child/attachment-createAttachments

        def page = getPage(CONF_URL, TOKEN, sourcePageId)
        def attachments = getPageAttachments(CONF_URL, TOKEN, sourcePageId).results

        attachments.each {
            addAttachToPage(CONF_URL, TOKEN, it.id, targetPageId)
            try {
                Files.delete(Path.of("src/main/resources/" + it.title))     // delete after copy
            } catch (Exception e) {
                e.printStackTrace()
            }
        }

    }
}
