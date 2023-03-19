package com.anma.services

import com.anma.models.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kong.unirest.HttpResponse
import kong.unirest.Unirest
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andrii Maliuta (https://github.com/AndriiMaliuta)
 */

class CommentService {
    Gson gson = new GsonBuilder().setPrettyPrinting().create()
    final Logger LOGGER = LoggerFactory.getLogger(CommentService.class)
    PageService pageService = new PageService();


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

    // add existing comment to page
    def commentContains(CONF_URL, TOKEN, id, toFind) {
        def comment = getPageComment(CONF_URL, TOKEN, id)
        return comment.body.storage.value.contains(toFind)
    }

    // todo
    def addCommentToPage(CONF_URL, TOKEN, commId, tgtPageId, tgtURL, tgtTOKEN) {
        // add existing comment from one page to another (or to some other Confluence instance)
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

    def addFooterCommentToPage(CONF_URL, TOKEN, tgtPageId, tgtURL, tgtTOKEN) {
        println("Adding footer comment to page ${tgtPageId}")

        Content newComment = new Content()
        newComment.title = ""
        newComment.type = 'comment'
        newComment.status = 'current'
        Body commBody = new Body()
        Storage storage = new Storage()
        commBody.storage = storage
        storage.representation = 'storage'
        storage.value = comment.body.storage.value
        newComment.body = commBody
        newComment.container = getPage(CONF_URL, TOKEN, tgtPageId)

        def commJson = gson.toJson(newComment)

        return Unirest.post("${tgtURL}/rest/api/content")  // ext
                .header("Authorization", "Basic ${tgtTOKEN}")
                .body(commJson)
                .asString()
    }

    def addInlineCommentToPage(tgtPageId, tgtURL, tgtTOKEN, body, selection) {
        println("Adding INLINE comment to page ${tgtPageId}")
        Content page = pageService.getPage(tgtURL, tgtTOKEN, tgtPageId)
        var req = """
{
   "originalSelection":"${selection}",
   "body":"${body}",
   "matchIndex":0,
   "numMatches":3,
   "serializedHighlights":"[[\\"${selection}\\",\\"1\\",52,5]]",
   "authorDisplayName":"admin",
   "authorUserName":"admin",
   "authorAvatarUrl":"/images/icons/profilepics/default.svg",
   "containerId":"${page.id}",
   "containerVersion":"1",
   "parentCommentId":"0",
   "lastFetchTime":"1679237984338",
   "hasDeletePermission":true,
   "hasEditPermission":true,
   "hasResolvePermission":true,
   "resolveProperties":{
      "resolved":false,
      "resolvedTime":0
   },
   "deleted":false
}
"""
        return Unirest.post("${tgtURL}/rest/inlinecomments/1.0/comments")  // ext
                .header("Authorization", "Basic ${tgtTOKEN}")
                .header("Content-Type", "application/json")
                .body(req)
                .asString()
    }

    @Deprecated
    def deleteComment(CONF_URL, TOKEN, id) {
        println(">>>>>>> Performing DELETE COMMENT request")
        HttpResponse<String> response =
                Unirest.delete("${CONF_URL}/rest/api/content/${id}")
                        .header("Authorization", "Basic ${TOKEN}")
                        .asString()
        return response.body
    }

    @Deprecated
    def moveComment(CONF_URL, TOKEN, id, targetParentId) {
        Content comment = getComment(CONF_URL, TOKEN, id)
        Ancestor ancestor = new Ancestor()
        ancestor.id = targetParentId
        comment.ancestors = [ancestor]
        comment.version.number += 1

        return Unirest.put("${CONF_URL}/rest/api/content/" + pageId)
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic ${TOKEN}")
                .body(gson.toJson(comment))
                .asString()
                .body
    }

    def getCommentAttachments() {

    }

    // todo
    def deletePageComments(CONF_URL, TOKEN, id) {
        println(">>>>>>> Performing DELETE COMMENTs request")
    }

    @Deprecated
    def copyPageComments(CONF_URL, TOKEN, sourceId, targetId, tgtURL, tgtTOKEN) {
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

}
