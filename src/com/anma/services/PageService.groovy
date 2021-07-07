package com.anma.services

import com.anma.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.thoughtworks.xstream.core.util.Base64Encoder

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class PageService {

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    static Content getPage(CONF_URL, username, password, id) {
//        id = 6324225
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}?expand=body.storage,version"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
//        HttpHeaders httpHeaders = HttpHeaders();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Content.class)

    }

    static def getChildren(CONF_URL, username, password, id) {

        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/${id}/child/page"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return gson.fromJson(response.body(), Contents.class)

    }

    /* Using https://docs.atlassian.com/ConfluenceServer/rest/7.5.0/#api/content-search */

    static def getDescendants(CONF_URL, username, password, id) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=ancestor+%3D+%226324225%22"
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}"))
                .headers("Authorization", "Basic ${TOKEN}")
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
        return contents.results

    }

    static def updateContentOnPage(confURL, username, password, id, toFind, toReplace) {

        final String CONF_URL = confURL

        def pageVersion = getPage(confURL, username, password, id).version.number
        def title = getPage(confURL, username, password, id).title
        String body = getPage(confURL, username, password, id).body.storage.value

//        println("+++ in update")
//        println(pageVersion)
//        println(title)
//        println(body)

        String newBody = body.replace(toFind, toReplace);

        // create entity for converting to JSON
        Content updatedPage = new Content()
        Version version = new Version()
        version.number = pageVersion + 1
        version.message = "changed with REST"
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
        println(pageJSON)

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
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
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

    static def getSpacePagesByLabel(CONF_URL, username, password, spaceKey, label) {

//        def urlRequst = "http://localhost:8712/dosearchsite.action?cql=space+%3D+%22TEST%22+and+label+%3D+%22test%22"
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=space+%3D+${spaceKey}+and+label+%3D+${label}"))
                .headers("Authorization", "Basic ${TOKEN}")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Contents contents = gson.fromJson(response.body(), Contents.class)

        return contents.results

    }

    static def getDescendantsWithLabel(CONF_URL, username, password, id, label) {

//        def urlRequst = cql=ancestor+%3D+"6324225"+and+label+%3D+"test"
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
        HttpRequest request = HttpRequest.newBuilder(
                URI.create("${CONF_URL}/rest/api/content/search?cql=ancestor+%3D+${id}+and+label+%3D+${label}"))
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
    static def addLabelsToPage(CONF_URL, username, password, id, List<String> labels) {

        def labelsArray = []

        labels.each { label ->
            Label label1 = new Label()
            label1.setPrefix("global")
            label1.setName(label)
//            String labelJson = gson.toJson(label1, Label.class)
            labelsArray.add(label1)
        }

//        JsonArray jsonArray = new JsonArray()
//        jsonArray.add()
        def labelsArrayJSON = gson.toJson(labelsArray, ArrayList.class)
//        println(labelsArrayJSON)

        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(labelsArrayJSON)
        def TOKEN = new String(Base64.encoder.encode("${username}:${password}".bytes))
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

    static def addPageTitlePart(CONF_URL, username, password, id, toAdd, position) {

        def page = getPage(CONF_URL, username, password, id)
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

        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
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
        def TOKEN = new Base64Encoder().encode("${username}:${password}".bytes)
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

}
