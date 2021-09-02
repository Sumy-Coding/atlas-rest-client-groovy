package com.anma.services

import com.anma.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mashape.unirest.http.Unirest

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PageService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static Content getPage(CONF_URL, TOKEN, id) {

        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}?expand=body.storage,version"))   // currently Storage is used for body
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
//        HttpHeaders httpHeaders = HttpHeaders();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        println(response.body())

        return gson.fromJson(response.body(), Content.class)
    }

    static def getChildren(CONF_URL, TOKEN, id) {

        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}/child/page?limit=300"))      // 300 pages limit
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Contents.class)
    }

    /* Using https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content-search */

    /* ?cql=ancestor shows WRONG data - some pages are from other parent !! */
    static def getDescendants(CONF_URL, TOKEN, id) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=ancestor+%3D+%226324225%22"
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}&limit=300"))     // limit = 300 pages
                .header("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        def allPages = []

        Contents contents = gson.fromJson(response.body(), Contents.class)
//        def children = contents.results
//        if (children.size() > 0) {
//            children.each {page ->
//                allPages.add(page)
//                while ()
//            }
//        }
        return contents
    }

    static def getSpacePages(CONF_URL, TOKEN, space) {
        println(">>>>>>> Performing GET Pages request")
        //http://localhost:7130/rest/api/content?type=page&spaceKey=TEST
         com.mashape.unirest.http.HttpResponse<String> response =
                 Unirest.get("${CONF_URL}/rest/api/content?type=page&spaceKey=${space}&limit=300")      // limit = 300 pages
                    .header("Authorization", "Basic ${TOKEN}")
                    .asString()

        Contents contents = gson.fromJson(response.body, Contents.class)

        return contents
    }

    static def getSpacePagesByLabel() {                                 // todo
        println(">>>>>>> Performing GET Pages request")


    }

    static def getSpaceBlogs(CONF_URL, TOKEN, space) {
        println(">>>>>>> Performing GET Pages request")
        //http://localhost:7130/rest/api/content?type=blogpost&spaceKey=TEST
        com.mashape.unirest.http.HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content?type=blogpost&spaceKey=${space}&limit=300")  // limit = 300 blogs
                    .header("Authorization", "Basic ${TOKEN}")
                    .asString()

        return gson.fromJson(response.body, Contents.class)

    }

    static def getPageLabels(CONF_URL, TOKEN, id) {
        println(">>>>>>> Performing GET LABELS request")
        com.mashape.unirest.http.HttpResponse<String> response =
                Unirest.get("${CONF_URL}/rest/api/content/${id}/label")
                    .header("Authorization", "Basic ${TOKEN}")
                    .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    static def deletePageLabels(CONF_URL, TOKEN, id, label) {
        println(">>>>>>> Performing DELETE LABELS request")
        com.mashape.unirest.http.HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}/label/${label}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()

        return gson.fromJson(response.body, Contents.class)
    }

    static def getScrollTemplates() {
        println(">>>>>>> Performing DELETE LABELS request")
        com.mashape.unirest.http.HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}/label/${label}")
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

    static def createPage(CONF_URL, TOKEN, space, parentId, title,body) {
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
        Ancestor[] ancestors = [ ancestor ]
        content.ancestors = ancestors
//        println(gson.toJson(content))

        return Unirest.post("${CONF_URL}/rest/api/content")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(content))
                .asString()
    }

    static def createComment(CONF_URL, TOKEN, space, ancestorsIds, containerId, containerType, body) {
        println(">>>>>>>>> Performing Create Comment request")
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
        /* Performing the PUT request to replace body */
        HttpClient client = HttpClient.newBuilder().build();
//        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody)
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${confURL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        return postResponse.body()

    }

    static def replacePageInfoMacro(CONF_URL, TOKEN, id) {

        def pageVersion = getPage(CONF_URL, TOKEN, id).version.number
        def title = getPage(CONF_URL, TOKEN, id).title
        String body = getPage(CONF_URL, TOKEN, id).body.storage.value
        String macroString = "";
        if (body.contains("<ac:structured-macro ac:name=\"page-info\"")) {
            try {
                macroString = body.substring(body.indexOf("<ac:structured-macro ac:name=\"page-info\""), body.indexOf("tinyurl</ac:parameter></ac:structured-macro>") + 44)
            } catch(Exception e) {
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
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${CONF_URL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        return postResponse.body()
    }

    static def getSpacePagesByLabel(CONF_URL, TOKEN, spaceKey, label) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=space+%3D+%22TEST%22+and+label+%3D+%22test%22"
//        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=space+%3D+${spaceKey}+and+label+%3D+${label}&limit=100"))       // 100 pages limit
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Contents contents = gson.fromJson(response.body(), Contents.class)

        return contents.results

    }

    static def getDescendantsWithLabel(CONF_URL, TOKEN, id, label) {

//        def urlRequst = cql=ancestor+%3D+"6324225"+and+label+%3D+"test"
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}+and+label+%3D+${label}&limit=100"))      // 100 pages limit
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build()
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Contents contents = gson.fromJson(response.body(), Contents.class)

        return contents.results

    }

    /*
        POST /rest/api/content/{id}/label
        https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content/{id}/label-deleteLabel
     */
    static def addLabelsToPage(CONF_URL, TOKEN, id, List<String> labels) {

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

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(labelsArrayJSON)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}/label"))
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .POST(body)
                .build();
        HttpClient client = HttpClient.newBuilder().build()

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString())

        return response
    }

    static def addPageTitlePart(CONF_URL, TOKEN, id,toAdd, position) {

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

        /* Performing the PUT request to replace body */
        HttpClient client = HttpClient.newBuilder().build();
//        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody)
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${CONF_URL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        return postResponse.body()

    }

    static def findReplacePageTitlePart(CONF_URL, username, password, id, find, replace) {

        def page = getPage(CONF_URL, username, password, id)

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
        def TOKEN = new String(Base64.encoder.encode("${username}:${password}".bytes))
        println("**** token is ${TOKEN}")

        /* Performing the PUT request to replace body */
        HttpClient client = HttpClient.newBuilder().build();
//        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(updatedPageBody)
        HttpRequest.BodyPublisher publisher = HttpRequest.BodyPublishers.ofString(pageJSON)
        HttpRequest postReq = HttpRequest.newBuilder()
                .uri(URI.create("${CONF_URL}/rest/api/content/${id}"))
                .PUT(publisher)
                .headers("Authorization", "Basic ${TOKEN}")
                .headers("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postReq, HttpResponse.BodyHandlers.ofString());

        return postResponse.body()

    }

    static def getPageRestrictions() {
        // todo GET /rest/api/content/{id}/restriction/byOperation

    }


    // todo GET /rest/api/space/{spaceKey}/content
    // todo GET /rest/api/content/{id}/child/attachment
    // todo DELETE /rest/api/content/{id}/label/...
}
